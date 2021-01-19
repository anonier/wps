package com.web.wps.logic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.wps.base.BaseController;
import com.web.wps.base.Response;
import com.web.wps.logic.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zm
 * 用户相关回调接口
 */
@RestController
@RequestMapping("v1/3rd/user")
public class UserCallBackController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 获取用户信息
     */
    @PostMapping("info")
    public ResponseEntity<Object> userInfo(
            @RequestBody JSONObject reqObj
    ) {
        logger.info("获取用户信息param:{}", JSON.toJSON(reqObj));
        try {
            Map<String, Object> map =
                    userService.userInfo(reqObj);
            return Response.success(map);
        } catch (Exception e) {
            return Response.bad("获取用户信息异常");
        }
    }

}
