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
 * @since 02-Aug-2011 12:00:36
 */
public class SkillKey {

    public SkillKey(Skill skill, String part) {
        this.skill = skill;
        this.part = part;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillKey skillKey = (SkillKey) o;

        if(skill.getSkillID() != skillKey.skill.getSkillID()) return false;
        if(!skill.getSkillName().equals(skillKey.skill.getSkillName())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result += skill.getSkillName().hashCode();
        result += skill.getSkillID();
        return result;
    }

    public String toString() {
        return skill.getSkillName();
    }

    public String getSkillName() {
        return skill.getSkillName();
    }

    public String getPart() {
        return part;
    }

    public Skill getSkill() {
        return skill;
    }

    private Skill skill;
    private String part;
}
