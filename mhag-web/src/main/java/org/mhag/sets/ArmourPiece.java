package org.mhag.sets;

import org.mhag.MhagFacade;
import org.mhag.model.Mhag;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bronwen.
 * Date: 27/02/13
 * Time: 13:31
 */
public class ArmourPiece implements Serializable {

    public ArmourPiece() {
    }

    public ArmourPiece(int id, String armourName, int piece, String skillName, int skillValue, int rank, int numSlots,
                       String bladeGunner, int armourId, String gender) {
        this.id = id;
        this.armourName = armourName;
        this.numSlots = numSlots;
        this.piece = piece;
        this.skillName = skillName;
        this.skillValue = skillValue;
        this.rank = rank;
        this.bladeGunner = bladeGunner;
        this.armourId = armourId;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArmourName() {
        return armourName;
    }

    public void setArmourName(String armourName) {
        this.armourName = armourName;
    }

    public int getNumSlots() {
        return numSlots;
    }

    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillValue() {
        return skillValue;
    }

    public void setSkillValue(int skillValue) {
        this.skillValue = skillValue;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getBladeGunner() {
        return bladeGunner;
    }

    public void setBladeGunner(String bladeGunner) {
        this.bladeGunner = bladeGunner;
    }

    public int getArmourId() {
        return armourId;
    }

    public void setArmourId(int armourId) {
        this.armourId = armourId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageValue() {
        switch (piece) {
            case 0:
                return MhagFacade.HEAD;
            case 1:
                return MhagFacade.CHEST;
            case 2:
                return MhagFacade.ARMS;
            case 3:
                return MhagFacade.WAIST;
        }
        return MhagFacade.LEGS;
    }

    public String getSelectValue() {
        String val = getImageValue();
        if(piece == 2) val = ARM;
        else if(piece == 4) val = LEG;
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArmourPiece that = (ArmourPiece) o;

        if (piece != that.piece) return false;
        if (rank != that.rank) return false;
        if (skillValue != that.skillValue) return false;
        if (!armourName.equals(that.armourName)) return false;
        if (bladeGunner != null ? !bladeGunner.equals(that.bladeGunner) : that.bladeGunner != null) return false;
        if (!skillName.equals(that.skillName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = armourName.hashCode();
        result = 31 * result + piece;
        result = 31 * result + skillName.hashCode();
        result = 31 * result + skillValue;
        result = 31 * result + rank;
        result = 31 * result + (bladeGunner != null ? bladeGunner.hashCode() : 0);
        return result;
    }

    public boolean isFemale() {
        return "F".equals(gender);
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(String[] skillNames, int[] skillValues) {
        skills.put(skillName, skillValue);
        for (int i = 0; i < skillNames.length; i++) {
            String name = skillNames[i];
            if(!name.equals(skillName) && StringUtils.hasText(skillName)) {
                skills.put(name, skillValues[i]);
            }
        }
    }

    public static final String RANK = "rank";
    public static final String BLADE_AND_GUNNER = "A";
    public static final String HEAD = "0";
    public static final String BLADE_OR_GUNNER = "blade_or_gunner";
    public static final String ARMOUR_PIECE = "armour_piece";
    public static final String ARM = "arm";
    public static final String LEG = "leg";

    private int id;
    private String armourName;
    private int numSlots;
    private int piece;
    private String skillName;
    private int skillValue;
    private int rank;
    private String bladeGunner;
    private int armourId;
    private String gender;

    private Map<String, Integer> skills = new LinkedHashMap<String, Integer>();
}
