/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.admin;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-Oct-2011 18:53:02
 */
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String username = (String) request.getSession().getAttribute("username");
        String userId = (String) request.getSession().getAttribute("userId");
        if (username != null) {
            request.setAttribute("username", username);
        }
        if(userId != null) request.setAttribute("userId", userId);
        if(userId != null && username != null) request.setAttribute("loggedIn", Boolean.TRUE);
    }
}