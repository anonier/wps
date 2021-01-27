package com.web.wps.logic.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.wps.config.Context;
import com.web.wps.enums.Enums;
import com.web.wps.logic.dto.*;
import com.web.wps.logic.entity.*;
import com.web.wps.logic.mapper.FileServiceMapper;
import com.web.wps.logic.service.*;
import com.web.wps.oauth.CmacAgent;
import com.web.wps.oauth.CmacAgentSession;
import com.web.wps.propertie.*;
import com.web.wps.util.*;
import com.web.wps.util.Exception.RestException;
import com.web.wps.util.file.FileUtil;
import com.web.wps.util.upload.ResFileDTO;
import com.web.wps.util.upload.UploadFileLocation;
import com.web.wps.util.upload.oss.OSSUtil;
import com.web.wps.util.upload.qn.QNUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileServiceMapper, WpsFile> implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Resource
    private WpsUtil wpsUtil;
    @Resource
    private WpsProperties wpsProperties;
    @Resource
    private OSSUtil ossUtil;
    @Resource
    private UserService userService;
    @Resource
    private PartnerService partnerService;
    @Resource
    private FileService fileService;
    @Resource
    private RedirectProperties redirect;
    @Resource
    private QNUtil qnUtil;
    @Resource
    private UploadProperties uploadProperties;
    @Resource
    private ConvertProperties convertProperties;
    @Resource
    private CmacAgentSession cmacAgentSession;

    @Value("${oss.bucket_name}")
    private String bucketName;

    @Value("${oss.disk_name}")
    private String diskName;

    /**
     * 获取token
     *
     * @return
     */
    public String getAccessToken(String apiKey, String apiSecret, HttpServletRequest request, HttpServletResponse response) {
        CmacAgent agent = this.check(apiKey, apiSecret);
        agent.setSessionId(request.getSession().getId());
        cmacAgentSession.save(request, agent, response, (int) TimeUnit.HOURS.toSeconds(Enums.enums.TWO.value));
        return request.getSession().getId();
    }

    CmacAgent check(String apiKey, String apiSecret) {
        CmacAgent agent = new CmacAgent();
        QueryWrapper<WpsPartner> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(WpsPartner::getApiKey, apiKey);
        WpsPartner partner = partnerService.getOne(wrapper);
        if (partner.getApiSecret().equals(apiSecret) && Enums.enums.ZERO.value.equals(partner.getStatus())) {
            return agent;
        }
        throw new RestException("鉴权参数错误");
    }

    /**
     * 获取预览用URL
     *
     * @param fileUrl    文件url
     * @param checkToken 是否校验token
     */
    public Token getViewUrl(String fileUrl, boolean checkToken) {
        Token t = new Token();

        String fileType = FileUtil.getFileTypeByPath(fileUrl);
        // fileId使用uuid保证出现同样的文件而是最新文件
        UUID randomUUID = UUID.randomUUID();
        String uuid = randomUUID.toString().replace("-", "");
        String userId = Context.getUserId();

        Map<String, String> values = new HashMap<String, String>() {
            {
                put("_w_appid", wpsProperties.getAppid());
                if (checkToken) {
                    put("_w_tokentype", "1");
                }
                put(redirect.getKey(), redirect.getValue());
                put("_w_userid", "-1");
                put("_w_filepath", fileUrl);
                put("_w_filetype", "web");
            }
        };

        String wpsUrl = wpsUtil.getWpsUrl(values, fileType, uuid);

        t.setToken(uuid);
        t.setExpires_in(600);
        t.setWpsUrl(wpsUrl);

        return t;
    }

    /**
     * 获取预览用URL
     *
     * @param fileId     文件id
     * @param userId     用户id
     * @param checkToken 是否校验token
     */
    @Override
    public Token getViewUrl(String fileId, String userId, boolean checkToken) {
        WpsFile fileEntity = this.getById(fileId);
        if (fileEntity != null) {
            Token t = new Token();
            String fileName = fileEntity.getName();
            String fileType = FileUtil.getFileTypeByName(fileName);

            UUID randomUUID = UUID.randomUUID();
            String uuid = randomUUID.toString().replace("-", "");

            Map<String, String> values = new HashMap<String, String>() {
                {
                    put("_w_appid", wpsProperties.getAppid());
                    if (checkToken) {
                        put("_w_tokentype", "1");
                    }
                    put(redirect.getKey(), redirect.getValue());
                    put("_w_filepath", fileName);
                    put("_w_userid", userId);
                    put("_w_filetype", "db");

                }
            };

            String wpsUrl = wpsUtil.getWpsUrl(values, fileType, fileEntity.getId());

            t.setToken(uuid);
            t.setExpires_in(600);
            t.setWpsUrl(wpsUrl);
            return t;
        }
        return null;
    }

    /**
     * 获取预览用URL
     *
     * @param filePath 文件路径
     * @param userId   用户id
     * @param type     请求预览文件类型
     */
    @Override
    public Map<String, Object> getFileInfo(String userId, String filePath, String type) {
        String fileId = Context.getFileId();

        // 获取文件信息
        WpsFile fileEntity = this.getById(fileId);

        //获取user
        WpsLog wps = userService.getById(userId);
        UserDTO user = new UserDTO();
        BeanUtils.copyProperties(wps, user);
        user.setPermission("write");

        // 构建fileInfo
        FileDTO file = new FileDTO();
        BeanUtils.copyProperties(fileEntity, file);
        file.setDownload_url(fileEntity.getDownloadUrl());

        return new HashMap<String, Object>() {
            {
                put("file", file);
                put("user", user);
            }
        };
    }

    /**
     * 文件重命名
     *
     * @param fileName 文件名
     * @param userId   用户id
     */
    @Override
    public void fileRename(String fileName, String userId) {
        String fileId = Context.getFileId();
        WpsFile file = this.getById(fileId);
        if (file != null) {
            file.setName(fileName);
            file.setUpdateName(userId);
            Date date = new Date();
            file.setUpdateTime(date);
            this.updateById(file);
        }
    }

    /**
     * 新建文件
     *
     * @param file   文件
     * @param userId 用户id
     */
    @Override
    public Map<String, Object> fileNew(MultipartFile file, String userId) {
        ResFileDTO resFileDTO;
        if (uploadProperties.getFileLocation().equalsIgnoreCase(UploadFileLocation.QN)) {
            resFileDTO = qnUtil.uploadMultipartFile(file);
        } else {
            resFileDTO = ossUtil.uploadMultipartFile(file);
        }
        String fileName = resFileDTO.getFileName();
        String fileUrl = resFileDTO.getFileUrl();
        int fileSize = (int) file.getSize();
        Date date = new Date();
        // 保存文件
        WpsFile f = WpsFile.builder()
                .fileId(null)
                .name(fileName)
                .version(1)
                .size(fileSize)
                .createName(userId)
                .updateName(userId)
                .createTime(date)
                .updateTime(date)
                .downloadUrl(fileUrl).build();
        this.save(f);
        // 处理返回
        Map<String, Object> map = new HashMap<>();
        map.put("redirect_url", this.getViewUrl(f.getId(), userId, false).getWpsUrl());
        map.put("user_id", userId);
        return map;
    }

    /**
     * 保存文件
     *
     * @param mFile  文件
     * @param userId 用户id
     */
    @Override
    public Map<String, Object> fileSave(MultipartFile mFile, String userId) {
        Date date = new Date();
        // 上传
        WpsLog user = userService.getById(userId);
        WpsFile wpsFile = fileService.getById(user.getWpsFileId());
        QueryWrapper<WpsPartner> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(WpsPartner::getPartnerKey, wpsFile.getPartnerKey());
        WpsPartner partner = partnerService.getOne(wrapper);
        String s = HttpRequest.httpPostUpload(mFile, partner.getApiUrl(), userId);
        if (StringUtils.isBlank(s)) {
            throw new RestException("文件上传请求失败");
        }
        JSONObject data = JSON.parseObject(JSON.parseObject(s).get("data").toString());
        ResFileDTO resFileDTO = (ResFileDTO) net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(data), ResFileDTO.class);
        int size = (int) resFileDTO.getFileSize();

        String fileId = Context.getFileId();
        WpsFile file = this.getById(fileId);
        FileDTO fileInfo = new FileDTO();

        // 更新当前版本
        file.setVersion(file.getVersion() + 1);
        file.setDownloadUrl(resFileDTO.getFileUrl());
        file.setUpdateName(userId);
        file.setUpdateTime(date);
        file.setSize(size);
        this.updateById(file);

        // 返回当前版本信息
        BeanUtils.copyProperties(file, fileInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("file", fileInfo);
        return map;
    }

    /**
     * 文件格式转换回调
     */
    @Override
    public void convertCallBack(HttpServletRequest request) {
        try {
            BufferedReader buf = request.getReader();
            String str;
            StringBuilder data = new StringBuilder();
            while ((str = buf.readLine()) != null) {
                data.append(str);
            }
            logger.info("文件转换callBask取得data={}", data);
            if (data.length() > 0) {
                JSONObject dataJson = JSON.parseObject(data.toString());
                if (dataJson.get("Code") != null) {
                    String code = (String) dataJson.get("Code");
                    String taskId = (String) dataJson.get("TaskId");
                    String url = getConvertQueryRes(taskId);
                    if (!StringUtils.isEmpty(url) && code.equalsIgnoreCase(HttpStatus.OK.getReasonPhrase())) {
                        //
                        System.out.println(url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件转换查询
     *
     * @param taskId 任务id，由convertFil接口生成
     */
    public String getConvertQueryRes(String taskId) {
        String headerDate = Common.getGMTDate();
        String downLoadUrl = "";
        try {
            //请求参数
            String contentMd5 = Common.getMD5(null); //请求内容数据的MD5值，用null作入参
            String url = convertProperties.getQuery() + "?TaskId=" + taskId + "&AppId=" + convertProperties.getAppid();
            String authorization = SignatureUtil.getAuthorization("GET", url, contentMd5, headerDate, convertProperties.getAppid(), convertProperties.getAppsecret()); //签名

            //header参数
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put(HttpHeaders.CONTENT_TYPE, Common.CONTENTTYPE);
            headers.put(HttpHeaders.DATE, headerDate);
            headers.put(HttpHeaders.CONTENT_MD5, contentMd5);//文档上是 "Content-Md5"
            headers.put(HttpHeaders.AUTHORIZATION, authorization);

            //开始调用
            String result = HttpUtil.get(url, headers);
            if (!StringUtils.isEmpty(result)) {
                JSONObject dataJson = JSON.parseObject(result);
                String code = dataJson.get("Code").toString();
                if (code.equals("OK")) {
                    if (dataJson.get("Urls") != null) { //实际上返回这个参数
                        downLoadUrl = (dataJson.get("Urls")).toString();
                        // 源["xxx"]转换
                        JSONArray jsonArray = JSONArray.parseArray(downLoadUrl);
                        downLoadUrl = jsonArray.get(0).toString();
                    } else if (dataJson.get("Url") != null) {//文档是返回这个参数
                        downLoadUrl = dataJson.get("Url").toString();
                    }
                    //成功
                } else {
                    String errorMsg = "文件格式转换失败";
                    if (dataJson.get("Message") != null) {
                        String message = dataJson.get("Message").toString();
                        errorMsg = errorMsg + message;
                    }
                    //失败
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("recordWPSConvertResult处理出错，错误={}", e.getMessage(), e);
        }
        return downLoadUrl;
    }

    @Override
    @Transactional
    public FileRtDto modify(FileModifyDto dto) {
        WpsFile file = this.getById(dto.getFileId());
        if (file == null) {
            throw new RestException("文件不存在");
        }
        FileRtDto rtDto = new FileRtDto();
        QueryWrapper<WpsLog> userWrapper = new QueryWrapper<>();
        userWrapper.lambda().eq(WpsLog::getUserId, dto.getAccount()).eq(WpsLog::getWpsFileId, dto.getFileId());
        WpsLog user = userService.getOne(userWrapper);
        if (org.springframework.util.StringUtils.isEmpty(user)) {
            user = new WpsLog();
            user.setName(dto.getUserName());
            user.setUserId(dto.getAccount());
            user.setWpsFileId(dto.getFileId());
            user.setStatus(Enums.logs.CREATE.value);
            userService.save(user);
        } else {
            user.setStatus(Enums.logs.MODIFY.value);
        }
        file.setDownloadUrl(dto.getDownloadUrl());
        file.setUpdateName(user.getName());
        this.updateById(file);
        userService.updateById(user);
        BeanUtils.copyProperties(file, rtDto);
        Token token;
        try {
            token = this.getViewUrl(file.getId(), user.getId(), true);
        } catch (Exception e) {
            throw new RestException("获取Url失败");
        }
        rtDto.setToken(token);
        return rtDto;
    }

    @Override
    @Transactional
    public FileRtDto saveFile(FileNewDto fileNewDto) {
        Date date = new Date();
        //保存数据到数据库
        WpsFile wpsFile = WpsFile.builder()
                .name(fileNewDto.getFileName())
                .fileId(fileNewDto.getFileId()).downloadUrl(fileNewDto.getDownloadUrl())
                .createTime(date).updateTime(date)
                .size(fileNewDto.getSize()).updateName(fileNewDto.getUserName())
                .createName(fileNewDto.getUserName()).version(1).partnerKey(fileNewDto.getPartnerKey()).build();
        try {
            this.save(wpsFile);
        } catch (Exception e) {
            throw new RestException("文件数据插入数据库失败！");
        }
        WpsLog wpsLog;
        try {
            wpsLog = new WpsLog(fileNewDto.getUserName(), fileNewDto.getAccount());
            wpsLog.setWpsFileId(wpsFile.getId());
            wpsLog.setStatus(Enums.logs.CREATE.value);
            //插入用户到数据库
            userService.save(wpsLog);
        } catch (Exception e) {
            throw new RestException("用户信息插入数据库失败！");
        }
        FileRtDto fileRtDto = new FileRtDto();
        BeanUtils.copyProperties(wpsFile, fileRtDto);
        //获得的文件id和用户id 再得到预览URL
        Token token;
        try {
            token = getViewUrl(wpsFile.getId(), wpsLog.getId(), true);
        } catch (Exception e) {
            throw new RestException("获取url失败");
        }
        fileRtDto.setToken(token);
        return fileRtDto;
    }
}