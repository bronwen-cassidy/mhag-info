/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.mhag.model.Armor;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Aug-2011 21:24:35
 */
public class ArmorWrapper implements Serializable {

    public ArmorWrapper(Armor armor, boolean female) {
        this.armor = armor;
        this.female = female;
        String armourName = armor.getArmorName();
        this.femaleArmor = armourName;
        this.maleArmor = armourName;
        this.gender = armor.getGender();
        final int pos = armourName.indexOf("/");
        if (pos != -1) {
            this.femaleArmor = armourName.substring(pos + 1).trim();
            this.maleArmor = armourName.substring(0, pos).trim();
        }
    }

    public String getArmorName() {
        String armourName = armor.getArmorName();
        final int pos = armourName.indexOf("/");
        if (pos != -1) {
            if (female) {
                armourName = armourName.substring(pos + 1).trim();
            } else {
                armourName = armourName.substring(0, pos).trim();
            }
        }
        return armourName;
    }

    public String getCurrentArmour() {
        return female ? femaleArmor : maleArmor;
    }

    public String getOtherArmour() {
        return female ? maleArmor : femaleArmor;
    }

    public String getMaleArmor() {
        return maleArmor;
    }

    public String getFemaleArmor() {
        return femaleArmor;
    }

    public int getArmorID() {
        return armor.getArmorID();
    }

    public int getNumSkill() {
        return armor.getNumSkill();
    }

    public int getBodyPart() {
        return armor.getBodyPart();
    }

    public int getNumSlot() {
        return armor.getNumSlot();
    }

    public boolean isFemale() {
        return female;
    }

    public String getGender() {
        return gender;
    }

    public Armor getArmor() {
        return armor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArmorWrapper that = (ArmorWrapper) o;
        return getArmorID() == that.getArmorID() && getBodyPart() == that.getBodyPart();
    }

    @Override
    public int hashCode() {
        int result = getArmorID();
        result = 31 * result + getBodyPart();
        return result;
    }

    public String toString() {
        return getArmorName();
    }

    private Armor armor;
    private boolean female;
    private String femaleArmor;
    private String maleArmor;
    private String gender;
}
