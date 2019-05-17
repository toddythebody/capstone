package org.launchcode.capstone.models.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class CustomInterceptor extends HandlerInterceptorAdapter {

    private String[] paths = {"/user/login", "/user/register", "/"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String path = request.getRequestURI();
        String pathCss = request.getServletPath();
        System.out.println(pathCss);
        if (pathCss.contains("css")) {
            return true;
        }
        if (Arrays.stream(paths).noneMatch(path::equals)) {
            if (WebUtils.getCookie(request, "name") == null) {
                response.sendRedirect("/");
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}
