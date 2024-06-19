package com.assess.backend.config;

import com.assess.backend.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器并添加拦截路径
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/login"); // 放行的请求
    }
}
