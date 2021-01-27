package com.web.wps.oauth.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author yibi
 */
@Component("customWebResponseExceptionTranslator")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {

        OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomOauthException(oAuth2Exception.getMessage()));
    }
}
