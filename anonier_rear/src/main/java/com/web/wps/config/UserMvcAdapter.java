package com.web.wps.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserMvcAdapter implements WebMvcConfigurer {

    private static String[] URL_PATTERNS = new String[]{"/v1/**","/oauth/**"};

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserHandlerAdapter()).addPathPatterns(URL_PATTERNS);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}