/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.mhag.model.Skill;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Aug-2011 16:44:58
 */
public class CharmSkill implements Serializable {

    public CharmSkill(Skill skill, int numSlots) {
        this.skill = skill;
        this.numSlots = numSlots;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getSkillID() {
        return skill.getSkillID();
    }

    public int getNumSlots() {
        return numSlots;
    }

    public String toString() {
        StringBuffer b = new StringBuffer(skill.getSkillName());
        b.append(" ");
        for(int i = 0; i < numSlots; i++) {
            b.append("O");
        }
        return b.toString();
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setCharmPoints(List<CharmPoint> charmPoints) {
        this.charmPoints = charmPoints;
    }

    public List<CharmPoint> getCharmPoints() {
        return charmPoints;
    }

    private Skill skill;
    private int numSlots;
    private int points;
    private List<CharmPoint> charmPoints;
}
