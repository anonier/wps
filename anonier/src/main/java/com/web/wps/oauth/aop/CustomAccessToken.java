package com.web.wps.oauth.aop;

import com.web.wps.oauth.ActionResult;
import lombok.Data;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

/**
 * 自定义{@link /oauth/token }输出结果
 * @author yibi
 */
@Data
public class CustomAccessToken {
    private int errorNo;
    private String type;
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;

    public static CustomAccessToken of(OAuth2AccessToken oAuth2AccessToken){
        CustomAccessToken response = new CustomAccessToken();
        response.errorNo = 0;
        response.type = ActionResult.SUCCESS;
        response.access_token = oAuth2AccessToken.getValue();
        response.token_type = oAuth2AccessToken.getTokenType();
        response.refresh_token = oAuth2AccessToken.getRefreshToken().getValue();
        response.expires_in = oAuth2AccessToken.getExpiresIn();
        response.scope = OAuth2Utils.formatParameterList(oAuth2AccessToken.getScope());
        return response;
    }
}
