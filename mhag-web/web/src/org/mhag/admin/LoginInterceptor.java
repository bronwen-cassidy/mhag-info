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
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getContextPath() + "/admin/login.htm";
        String userSession = (String) request.getSession().getAttribute("admin_u");
        if (userSession == null) userSession = request.getParameter("admin_u");
        if (userSession == null) {
            // redirect to login and return false so that the next component in the chain is not invoked
            response.sendRedirect(url);
            return false;

        } else {
            String userPassword = (String) request.getSession().getAttribute("admin_p");
            if(userPassword == null) userPassword = request.getParameter("admin_p");
            if (!(MHAG_ADMIN.equals(userPassword) && MHAG_ADMIN.equals(userSession))) {
                response.sendRedirect(url);
                return false;
            } else {
                request.getSession().setAttribute("admin_u", MHAG_ADMIN);
                request.getSession().setAttribute("admin_p", MHAG_ADMIN);
            }
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String userSession = (String) request.getSession().getAttribute("admin_u");
        if (userSession != null) {
            request.setAttribute("adminLogin", Boolean.TRUE);
            request.setAttribute("loggedIn", "MHAG Admin");
        }
    }

    private static final String MHAG_ADMIN = "mhagadmin";
}
