package org.launchcode.capstone.models.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String path = request.getRequestURI();
        if (!path.equals("/user/login") && !path.equals("/user/register")) {
            for (Cookie c : cookies) {
                if (c.getName().equals("name")) {
                    if (c.getValue() == null) {
                        response.sendRedirect("/user/login");
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            response.sendRedirect("/user/login");
            return false;
        }
        return true;
    }

}
