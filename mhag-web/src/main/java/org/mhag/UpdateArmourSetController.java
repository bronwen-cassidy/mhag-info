/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mhag.sets.*;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-Oct-2011 09:32:34
 */
public class UpdateArmourSetController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "id");
        if (id != null) {
            String data = ServletRequestUtils.getStringParameter(request, "setdata", null);
            String setName = ServletRequestUtils.getStringParameter(request, "setName", null);
            int weponSlots = ServletRequestUtils.getIntParameter(request, "wepSlots", 0);
            String[] skills = ServletRequestUtils.getStringParameters(request, "skillList[]");
            try {
                if (skills.length > 1) {
                    data = data.substring(data.indexOf("s=") + 2);
                    SavedSet s = new SavedSet(setName, data);
                    s.setNumWeaponSlots(weponSlots);
                    s.setId(id);
                    for (String skill : skills) {
                        s.addSkill(new SetSkill(skill));
                    }
                    service.updateArmourSet(s);
                    response.getOutputStream().write(id.toString().getBytes());
                } else {
                    response.getOutputStream().write("Your set needs to have at least 2 skills.".getBytes());
                }
            } catch (NotEnoughSkillsException e) {
                logger.debug("less than 2 skills was attempted in an update");
                response.getOutputStream().write("Your set needs to have at least 2 skills.".getBytes());
            } catch (Throwable e) {
                logger.error(e.getMessage());
                response.getOutputStream().write("error".getBytes());
            }
        }
        return null;
    }

    public void setService(Serviceable service) {
        this.service = service;
    }

    private Serviceable service;
    private static Log logger = LogFactory.getLog(UpdateArmourSetController.class);
}
