package com.web.wps.oauth.aop;

import com.web.wps.oauth.ActionResult;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义{@link /oauth/token }输出结果
 * @author yibi
 */
@Component
@Aspect
public class AuthTokenAspect {

    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object proceed = pjp.proceed();
        if (proceed != null) {
            try{
                ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
                if (responseEntity.getStatusCode().is2xxSuccessful() && null != responseEntity.getBody()) {
                    String test = request.getHeader("refresh_auth");
                    if(StringUtils.isBlank(test)){
                        OAuth2AccessToken body = responseEntity.getBody();
                        return ResponseEntity.status(HttpStatus.OK).body(CustomAccessToken.of(body));
                    }else{
                        OAuth2AccessToken body = responseEntity.getBody();
                        return ResponseEntity.status(HttpStatus.OK).body(ActionResult.create(body));
                    }
                }
            }catch (Exception e){
                return proceed;
            }
        }
        return proceed;
    }
}