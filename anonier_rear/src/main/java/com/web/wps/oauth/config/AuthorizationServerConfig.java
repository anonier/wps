package com.web.wps.oauth.config;

import com.web.wps.oauth.handler.AuthExceptionEntryPoint;
import com.web.wps.oauth.handler.CustomClientCredentialsTokenEndpointFilter;
import com.web.wps.oauth.handler.CustomWebResponseExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author yibi
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private DataSource dataSource;

    @Resource
    private ClientDetailsService clientDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Resource
    private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
        ClientCredentialsTokenGranter tokenGranter = new ClientCredentialsTokenGranter(tokenServices(), clientDetailsService, requestFactory);
        tokenGranter.setAllowRefresh(true);
        endpoints.tokenGranter(tokenGranter);
        endpoints.tokenServices(tokenServices());
        endpoints.tokenStore(tokenStore);
        endpoints.exceptionTranslator(customWebResponseExceptionTranslator);
    }

    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return services;
    }



    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer configurer) {
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(configurer);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(new AuthExceptionEntryPoint());
        configurer.addTokenEndpointAuthenticationFilter(endpointFilter);
        configurer.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }
}