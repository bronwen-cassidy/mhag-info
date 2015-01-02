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
 * @since 28-Sep-2011 18:14:26
 */
public class ArmourSetFormController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // next todo don't use the url use the armour panel instead
        String data = ServletRequestUtils.getStringParameter(request, "setdata", null);
        String setName = ServletRequestUtils.getStringParameter(request, "setName", null);
        String owner = ServletRequestUtils.getStringParameter(request, "owner", null);
        int weponSlots = ServletRequestUtils.getIntParameter(request, "wepSlots", 0);
        Long userId = ServletRequestUtils.getLongParameter(request, "uid", -1);

        String[] skills = ServletRequestUtils.getStringParameters(request, "skillList[]");
        
        try {
            if (skills.length > 1) {
                data = data.substring(data.indexOf("s=") + 2);
                SavedSet s = new SavedSet(setName, data, owner);
                s.setNumWeaponSlots(weponSlots);
                String address = request.getRemoteAddr();
                s.setIpAddress(address);
                if(userId != -1) s.setUserId(userId);

                for (String skill : skills) {
                    s.addSkill(new SetSkill(skill));
                }

                Long id = service.saveArmourSet(s);
                response.getOutputStream().write(id.toString().getBytes());
            } else {
                response.getOutputStream().write("Error: Your set needs to have at least 2 skills.".getBytes());
            }
        } catch(NotEnoughSkillsException e) {
            logger.debug("couldn't save skills not enough to be a valid set requires at least 2 sets ");
            response.getOutputStream().write("Error: Your set needs to have at least 2 skills.".getBytes());
        } catch(SetExistsException e) {
            logger.debug("duplicat set created ");
            response.getOutputStream().write("Error: Your set is a duplicate in every way of an existing set.".getBytes());
        } catch (Throwable e) {
            logger.error("An error occured during the handleRequest method.", e);
            response.getOutputStream().write("Error ".getBytes());
        }
        return null;
    }

    public void setService(Serviceable service) {
        this.service = service;
    }

    private Serviceable service;
    static Log logger = LogFactory.getLog(ArmourSetFormController.class);

}
