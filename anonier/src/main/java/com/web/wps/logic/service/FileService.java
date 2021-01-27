package com.web.wps.logic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.wps.logic.dto.*;
import com.web.wps.logic.entity.*;
import com.web.wps.util.Token;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface FileService extends IService<WpsFile> {

    /**
     * 获取token
     *
     * @throws Exception
     */
    String getAccessToken(String apiKey, String apiSecret, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取Wps预览链接
     *
     * @param fileId
     * @param userId
     * @param checkToken
     * @return
     */
    Token getViewUrl(String fileId, String userId, boolean checkToken);

    /**
     * 修改文件
     *
     * @param dto
     * @return
     */
    FileRtDto modify(FileModifyDto dto);

    /**
     * 重命名
     *
     * @param fileName
     * @param userId
     */
    void fileRename(String fileName, String userId);

    /**
     * 新建文件
     *
     * @param file
     * @param userId
     * @return
     */
    Map<String, Object> fileNew(MultipartFile file, String userId);

    /**
     * 保存文件
     *
     * @param fileNewDto
     * @return
     */
    FileRtDto saveFile(FileNewDto fileNewDto);

    /**
     * 文件转换
     *
     * @param request
     */
    void convertCallBack(HttpServletRequest request);

    /**
     * 获取预览URL
     *
     * @param userId
     * @param filePath
     * @param type
     * @return
     */
    Map<String, Object> getFileInfo(String userId, String filePath, String type);

    /**
     * 保存文件
     *
     * @param mFile
     * @param userId
     * @return
     */
    Map<String, Object> fileSave(MultipartFile mFile, String userId);
}