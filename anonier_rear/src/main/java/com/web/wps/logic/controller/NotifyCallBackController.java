package com.web.wps.logic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.wps.base.BaseController;
import com.web.wps.base.Response;
import com.web.wps.logic.dto.FileReqDTO;
import com.web.wps.logic.service.FileService;
import com.web.wps.logic.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yb
 * 消息回调接口及用户相关回调接口
 */
@RestController
@RequestMapping("v1/3rd")
public class NotifyCallBackController extends BaseController {

    @Resource
    private FileService fileService;
    @Resource
    private UserService userService;

    /**
     * 获取用户信息
     */
    @PostMapping("user/info")
    public ResponseEntity<Object> userInfo(@RequestBody JSONObject reqObj) {
        logger.info("获取用户信息param:{}", JSON.toJSON(reqObj));
        try {
            Map<String, Object> map =
                    userService.userInfo(reqObj);
            return Response.success(map);
        } catch (Exception e) {
            return Response.bad("获取用户信息异常");
        }
    }

    /**
     * 回调通知
     */
    @PostMapping("onnotify")
    public ResponseEntity<Object> onNotify(@RequestBody JSONObject obj) {
        logger.info("回调通知param:{}", JSON.toJSONString(obj));
        // TODO
        // 返回数据暂不处理
        return Response.success();
    }

    /**
     * 通知此文件目前有哪些人正在协作
     */
    @PostMapping("file/online")
    public ResponseEntity<Object> fileOnline(@RequestBody JSONObject obj) {
        logger.info("通知此文件目前有哪些人正在协作param:{}", JSON.toJSON(obj));
        return Response.success();
    }

    /**
     * 文件重命名
     */
    @PutMapping("file/rename")
    public ResponseEntity<Object> fileRename(@RequestBody FileReqDTO req, String _w_userid) {
        logger.info("文件重命名param:{},userId:{}", JSON.toJSON(req), _w_userid);
        fileService.fileRename(req.getName(), _w_userid);
        return Response.success();
    }

    /**
     * 新建文件
     */
    @PostMapping("file/new")
    public ResponseEntity<Object> fileNew(@RequestBody MultipartFile file, String _w_userid) {
        logger.info("新建文件_w_userid:{}", _w_userid);
        Map<String, Object> res = fileService.fileNew(file, _w_userid);
        return Response.success(res);
    }

    /**
     * 文件格式转换回调--wps官方回掉用
     */
    @PostMapping("file/convertCallback")
    public ResponseEntity<Object> callback(HttpServletRequest request) {
        logger.info("文件转换回掉");
        fileService.convertCallBack(request);
        return Response.success();
    }
}