/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mhag.model.Armor;
import org.mhag.sets.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Sep-2011 18:14:26
 */
public class SkillSearchMultiController extends MultiActionController {

    public ModelAndView skillSearchData(HttpServletRequest request, HttpServletResponse response) {
        int rank = ServletRequestUtils.getIntParameter(request, ArmourPiece.RANK, -1);
        int currentRank = ServletRequestUtils.getIntParameter(request, "currentRank", -1);

        String blade = ServletRequestUtils.getStringParameter(request, "blade", null);
        String currentBlade = ServletRequestUtils.getStringParameter(request, "currentBlade", "B");
        currentBlade = currentBlade.equals("blade") ? "B" : "G";

        String skillName = ServletRequestUtils.getStringParameter(request, "skillName", null);
        String skillValue = ServletRequestUtils.getStringParameter(request, "skillValue", null);
        String operator = ServletRequestUtils.getStringParameter(request, "oper", null);

        String piece = ServletRequestUtils.getStringParameter(request, "piece", null);
        Map<String, String> params = new HashMap<String, String>();
        if(rank != -1) {
            params.put(ArmourPiece.RANK, rank + "");
        }

        operator = evaluateOperator(operator);

        if(!ArmourPiece.BLADE_AND_GUNNER.equals(blade) && !ArmourPiece.HEAD.equals(piece)) params.put(ArmourPiece.BLADE_OR_GUNNER, blade);
        if(StringUtils.hasText(piece)) params.put(ArmourPiece.ARMOUR_PIECE, piece);

        List<ArmourPiece> results = service.searchArmourPieces(params, skillName, operator, skillValue);
        for (ArmourPiece result : results) {
            ArmorWrapper wrapper = mhagFacade.getArmour(result.getArmourId(), result.getPiece(), result.isFemale());
            Armor armor = wrapper.getArmor();
            String[] skillNames = armor.getSkillName();
            int[] skillValues = armor.getSkillPoint();
            result.setSkills(skillNames, skillValues);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("armourPieces", results);
        model.put("currentRank", currentRank);
        model.put("currentBlade", currentBlade);
        model.put("skillName", skillName);
        return new ModelAndView("skillsearch", "model", model);
    }

    private String evaluateOperator(String operator) {
        if("gt".equals(operator)) operator = ">";
        else if("lt".equals(operator)) operator = "<";
        else operator = "=";
        return operator;
    }

    public ModelAndView skillListData(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("skills", mhagFacade.getSkillList());
        return new ModelAndView("skilllist","model", model);
    }


    public void setService(Serviceable service) {
        this.service = service;
    }

    public void setMhagFacade(MhagFacade mhagFacade) {
        this.mhagFacade = mhagFacade;
    }

    private MhagFacade mhagFacade;
    private Serviceable service;
    static Log logger = LogFactory.getLog(SkillSearchMultiController.class);

}
