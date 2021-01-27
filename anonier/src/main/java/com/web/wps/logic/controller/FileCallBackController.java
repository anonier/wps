package com.web.wps.logic.controller;

import com.web.wps.base.BaseController;
import com.web.wps.base.Response;
import com.web.wps.logic.service.FileService;
import com.web.wps.oauth.CmacAgent;
import com.web.wps.oauth.CmacAgentSession;
import com.web.wps.util.Exception.RestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author zm
 * 文件相关回调接口
 */
@RestController
@RequestMapping("v1/3rd/file")
public class FileCallBackController extends BaseController {

    @Resource
    private FileService fileService;
    @Resource
    private CmacAgentSession cmacAgentSession;

    /**
     * 获取文件元数据
     */
    @GetMapping("info")
    public ResponseEntity<Object> getFileInfo(String _w_userid, String _w_filepath, String _w_filetype, HttpServletRequest httpServletRequest) {
        CmacAgent agent = cmacAgentSession.get(httpServletRequest);
        if (agent == null) {
            throw new RestException("NO TOKEN");
        }
        logger.info("获取文件元数据userId:{},path:{},type:{}", _w_userid, _w_filepath, _w_filetype);
        try {
            Map<String, Object> map =
                    fileService.getFileInfo(_w_userid, _w_filepath, _w_filetype);
            return Response.success(map);
        } catch (Exception e) {
            return Response.bad("获取文件元数据异常");
        }
    }

    /**
     * 上传文件新版本
     */
    @PostMapping("save")
    public ResponseEntity<Object> fileSave(@RequestBody MultipartFile file, String _w_userid, HttpServletRequest httpServletRequest) {
        CmacAgent agent = cmacAgentSession.get(httpServletRequest);
        if (agent == null) {
            throw new RestException("NO TOKEN");
        }
        logger.info("上传文件新版本");
        Map<String, Object> map = fileService.fileSave(file, _w_userid);
        return Response.success(map);
    }
}