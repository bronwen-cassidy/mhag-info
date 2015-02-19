/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.admin;

import org.mhag.sets.Serviceable;
import org.mhag.sets.UserExistsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-Oct-2011 19:26:32
 */
public class UserLoginController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String register = ServletRequestUtils.getStringParameter(request, "register", null);
        String username = ServletRequestUtils.getStringParameter(request, "username");
        String password = ServletRequestUtils.getStringParameter(request, "password");
        Map<String, Object> model = new HashMap<String, Object>();
        String errorMessage;

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            errorMessage = "Please enter a username and/or password";
            model.put("errorMsg", errorMessage);
            return new ModelAndView("reglogin", "model", model);
        }

        Long userId;
        if ("register".equals(register)) {
            try {
                userId = service.registerUser(username, password);
            } catch (UserExistsException e) {
                errorMessage = "Cannot Register as username must be unique";
                model.put("errorMsg", errorMessage);
                return new ModelAndView("reglogin", "model", model);
            }
        } else {
            userId = service.loginUser(username, password);
        }

        if (userId == null) {
            errorMessage = "error Logging in please check you entered the correct username/password";
            model.put("errorMsg", errorMessage);
        }

        if (userId != null) {
            final HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("userId", userId.toString());
        }

        return new ModelAndView("reglogin", "model", model);
    }

    public void setService(Serviceable service) {
        this.service = service;
    }

    private Serviceable service;
}