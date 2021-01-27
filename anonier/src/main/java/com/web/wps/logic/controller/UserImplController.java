package com.web.wps.logic.controller;

import com.web.wps.base.BaseController;
import com.web.wps.base.Response;
import com.web.wps.logic.dto.FileModifyDto;
import com.web.wps.logic.dto.FileNewDto;
import com.web.wps.logic.service.FileService;
import com.web.wps.oauth.CmacAgent;
import com.web.wps.oauth.CmacAgentSession;
import com.web.wps.util.Exception.RestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author zm
 * 用户实现获取wps可预览URL
 */
@RestController
@RequestMapping("v1/api/file")
public class UserImplController extends BaseController {

    @Resource
    private FileService fileService;
    @Resource
    private CmacAgentSession cmacAgentSession;

    @GetMapping("getToken")
    public ResponseEntity<Object> getToken(@RequestParam(value = "apiKey") String apiKey, @RequestParam(value = "apiSecret") String apiSecret, HttpServletRequest request, HttpServletResponse response) {
        try {
            return Response.success(fileService.getAccessToken(apiKey, apiSecret, request, response));
        } catch (Exception e) {
            return Response.bad("获取token失败！" + e.getMessage());
        }
    }

    /**
     * 修改文件返回预览Url
     *
     * @param dto
     * @return
     */
    @PostMapping("modify")
    public ResponseEntity<Object> modify(@Valid @RequestBody FileModifyDto dto, HttpServletRequest httpServletRequest) {
        CmacAgent agent = cmacAgentSession.get(httpServletRequest);
        if (agent == null) {
            throw new RestException("NO TOKEN");
        }
        try {
            return Response.success(fileService.modify(dto));
        } catch (Exception e) {
            return Response.bad("文件不存在或其他异常！" + e.getMessage());
        }
    }

    /**
     * 新建文档保存返回预览URL
     */
    @PostMapping("saveFile")
    public ResponseEntity<Object> saveFile(@Valid @RequestBody FileNewDto fileNewDto, HttpServletRequest httpServletRequest) {
        CmacAgent agent = cmacAgentSession.get(httpServletRequest);
        if (agent == null) {
            throw new RestException("NO TOKEN");
        }
        try {
            logger.info("saveFile:", fileNewDto);
            return Response.success(fileService.saveFile(fileNewDto));
        } catch (Exception e) {
            return Response.bad("文件不存在或其他异常！" + e.getMessage());
        }
    }
}