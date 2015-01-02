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
public class VoteArmourSetController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long setId = ServletRequestUtils.getLongParameter(request, "sId");
        Long userId = ServletRequestUtils.getLongParameter(request, "uid");
        String action = ServletRequestUtils.getStringParameter(request, "f");
        String ip = request.getRemoteAddr();
        int[] numVotes;
        if ("add".equals(action)) {
            numVotes = service.addVote(setId, userId, ip);
        } else {
            numVotes = service.removeVote(setId, userId, ip);
        }

        String votes = numVotes[0] + ":" + numVotes[1];
        response.getOutputStream().write(votes.getBytes());

        return null;
    }

    public void setService(Serviceable service) {
        this.service = service;
    }

    private Serviceable service;
}