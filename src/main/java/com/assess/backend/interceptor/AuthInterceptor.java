package com.assess.backend.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在这里编写拦截器的逻辑
        // 判断请求头中的username和token是否存在且有效
        String phone = request.getHeader("phone");
        String token = request.getHeader("token");

        // 这里可以根据具体逻辑判断token的有效性，比如验证数据库或缓存中的token等

        if (isValid(phone, token)) {
            return true; // 如果验证通过，放行请求
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 返回401未授权状态码
            return false; // 拦截请求
        }
    }

    private boolean isValid(String phone, String token) {
        // 这里可以实现具体的验证逻辑，比如验证token是否有效
        // 返回true表示验证通过，false表示验证失败
        return true; // 临时返回true，实际应根据实际情况进行验证
    }
}
