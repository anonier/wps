package com.web.wps.oauth.handler;

import com.alibaba.fastjson.JSON;
import com.web.wps.oauth.ActionResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yibi
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ActionResult result = new ActionResult();
        result.setType(ActionResult.ERROR);
        result.setErrorNo(405);
        result.setTitle(authException.getMessage());
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}