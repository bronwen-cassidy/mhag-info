/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.mhag.sets.Serviceable;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Delete called when viewing a row it just removed the row and does not need to return a view
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-Oct-2011 09:32:34
 */
public class DeleteArmourSetController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long id = ServletRequestUtils.getLongParameter(request, "id");
        long[] ids = ServletRequestUtils.getLongParameters(request, "ids");

        if (ids != null && ids.length > 0) {
            Long[] oIds = MhagUtils.convertToLong(ids);
            try {
                service.deleteAll(oIds);
            } catch (Exception e) {
                response.getOutputStream().write("error".getBytes());    
            }
        }
        else if (id != null) {

            try {
                service.delete(id);
            } catch (Throwable e) {
                response.getOutputStream().write("error".getBytes());
            }
        }

        return null;
    }

    public void setService(Serviceable service) {
        this.service = service;
    }

    private Serviceable service;
}