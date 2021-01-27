package com.web.wps.logic.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.wps.logic.entity.WpsLog;
import com.web.wps.logic.mapper.UserServiceMapper;
import com.web.wps.logic.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserServiceMapper, WpsLog> implements UserService {

    @Override
    public Map<String, Object> userInfo(JSONObject reqObj) {
        List<String> ids = null;

        if (reqObj != null) {
            if (reqObj.containsKey("ids")) {
                ids = JSONArray.parseArray(reqObj.getString("ids"), String.class);
            }
        }

        Map<String, Object> map = new HashMap<>();
        List<WpsLog> users = new ArrayList<>();
        if (ids != null && !ids.isEmpty()) {
            WpsLog user;
            for (String id : ids) {
                user = this.getById(id);
                if (user != null) {
                    users.add(user);
                }
            }
        }
        map.put("users", users);

        return map;
    }
}