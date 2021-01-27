package com.web.wps.logic.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.web.wps.logic.entity.WpsLog;

import java.util.Map;

public interface UserService extends IService<WpsLog> {

    /**
     * 获取用户信息
     * @param reqObj
     * @return
     */
    Map<String, Object> userInfo(JSONObject reqObj);
}