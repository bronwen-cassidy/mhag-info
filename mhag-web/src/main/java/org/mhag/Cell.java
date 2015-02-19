/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Aug-2011 11:43:13
 */
public class Cell implements Comparable<Cell> {

    public Cell(String pieceName) {
        this.pieceName = pieceName;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String toString() {
        return pieceName + ": " + skillName + " = " + skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = this.skillPoints + skillPoints;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getDisplayValue() {

        if(MhagFacade.ACTIVATED_SKILL.equals(pieceName)) {
            if(isTorsoUpSkill()) return "";
            return skillName != null ? skillName : "&mdash;";
        }
        if(skillPoints == 0) return "&mdash;";
        return "" + skillPoints;
    }

    public boolean isSkillDisplay() {
        if(MhagFacade.ACTIVATED_SKILL.equals(pieceName)) {
            if(isTorsoUpSkill()) return false;
            return skillName != null;
        }
        return false;
    }

    private boolean isTorsoUpSkill() {
        return MhagFacade.TORSO_UP.equals(skillName);
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getResPoints() {
        return resPoints;
    }

    public void setResPoints(int resPoints) {
        this.resPoints = resPoints;
    }

    public int getDefence() {
        return defence;
    }

    public String getResDisplayValue() {
        if(resPoints == 0) return "";
        return resPoints + "";
    }

    public String getDefenseDisplayValue() {
        if(defence == 0) return "";
        return defence + "";   
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTorsoUp() {
        return TORSO_UP.equals(type);
    }

    public boolean isTorsoUpPiece() {
        return (isTorsoUpSkill() && !(MhagFacade.TOTAL.equals(pieceName) || MhagFacade.ACTIVATED_SKILL.equals(pieceName) ));
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int compareTo(Cell o) {
        return new Double(getComaprePoints()).compareTo(new Double(o.getComaprePoints()));
    }

    protected double getComaprePoints() {
        double comparePoints = this.skillPoints;
        if(isTorsoUpSkill()) comparePoints = 25;
        else if(skillPoints < -9 && activated) comparePoints = 9.5;
        else if(skillPoints > 9 && !activated) comparePoints = 9;
        return comparePoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (defence != cell.defence) return false;
        if (resPoints != cell.resPoints) return false;
        if (skillPoints != cell.skillPoints) return false;
        if (!pieceName.equals(cell.pieceName)) return false;
        if (skillName != null ? !skillName.equals(cell.skillName) : cell.skillName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pieceName.hashCode();
        result = 31 * result + (skillName != null ? skillName.hashCode() : 0);
        result = 31 * result + skillPoints;
        result = 31 * result + resPoints;
        result = 31 * result + defence;
        return result;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
    }

    private String pieceName;
    private String skillName;
    private String type = "";
    private int skillPoints;
    private int resPoints;
    private int defence;

    public static final String TORSO_UP = " E";

    private boolean activated;
}
