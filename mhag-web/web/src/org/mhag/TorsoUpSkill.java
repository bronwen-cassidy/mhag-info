/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.mhag.model.Skill;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Aug-2011 12:45:29
 */
public class TorsoUpSkill extends Skill {

    public TorsoUpSkill(int skillId, String bodyPart) {
        this.skillId = skillId;
        this.bodyPart = bodyPart;
    }

    @Override
    public String getSkillName() {
        return skillName;
    }

    @Override
    public int getSkillID() {
        return skillId;
    }

    @Override
    public String toString() {
        return bodyPart + ": " + skillName;
    }

    private int skillId = -1;
    private String skillName = "Torso Up";
    private String bodyPart;
}
