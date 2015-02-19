/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 31-Aug-2011 17:40:11
 */
public class ArmourSet {

    public void setHeadArmour(ArmorWrapper armorWrapper) {
        this.headArmour = armorWrapper;
    }

    public ArmorWrapper getHeadArmour() {
        return headArmour;
    }

    public ArmorWrapper getChestArmour() {
        return chestArmour;
    }

    public void setChestArmour(ArmorWrapper chestArmour) {
        this.chestArmour = chestArmour;
    }

    public ArmorWrapper getArmArmour() {
        return armArmour;
    }

    public void setArmArmour(ArmorWrapper armArmour) {
        this.armArmour = armArmour;
    }

    public ArmorWrapper getWaistArmour() {
        return waistArmour;
    }

    public void setWaistArmour(ArmorWrapper waistArmour) {
        this.waistArmour = waistArmour;
    }

    public ArmorWrapper getLegArmour() {
        return legArmour;
    }

    public void setLegArmour(ArmorWrapper legArmour) {
        this.legArmour = legArmour;
    }

    public void setHeadJewels(List<JewelWrapper> headJewels) {
        this.headJewels = headJewels;
    }

    public void setChestJewels(List<JewelWrapper> chestJewels) {
        this.chestJewels = chestJewels;
    }

    public void setArmJewels(List<JewelWrapper> armJewels) {
        this.armJewels = armJewels;
    }

    public void setWaistJewels(List<JewelWrapper> waistJewels) {
        this.waistJewels = waistJewels;
    }

    public void setLegJewels(List<JewelWrapper> legJewels) {
        this.legJewels = legJewels;
    }

    public List<JewelWrapper> getHeadJewels() {
        return headJewels;
    }

    public List<JewelWrapper> getChestJewels() {
        return chestJewels;
    }

    public List<JewelWrapper> getArmJewels() {
        return armJewels;
    }

    public List<JewelWrapper> getWaistJewels() {
        return waistJewels;
    }

    public List<JewelWrapper> getLegJewels() {
        return legJewels;
    }

    public void setWepJewels(List<JewelWrapper> wepJewels) {
        this.wepJewels = wepJewels;
    }

    public List<JewelWrapper> getWepJewels() {
        return wepJewels;
    }

    public void setCharmJewels(List<JewelWrapper> charmJewels) {
        this.charmJewels = charmJewels;
    }

    public List<JewelWrapper> getCharmJewels() {
        return charmJewels;
    }

    public void setCharmSkills(List<CharmSkill> charmSkills) {
        this.charmSkills = charmSkills;
    }

    public List<CharmSkill> getCharmSkills() {
        return charmSkills;
    }

    public void setNumCharmSlots(int numCharmSlots) {
        this.numCharmSlots = numCharmSlots;
    }

    public int getNumCharmSlots() {
        return numCharmSlots;
    }

    public int getNumWepSlots() {
        int numSlots = 0;
        if (wepJewels != null) {
            for (JewelWrapper wepJewel : wepJewels) {
                numSlots += wepJewel.getNumSlot();
            }
        }
        return numSlots;
    }

    public CharmSkill getFirstCharmSkill() {
        if(charmSkills == null || charmSkills.isEmpty()) return null;
        return charmSkills.get(0);
    }

    public CharmSkill getSecondCharmSkill() {
        if(charmSkills == null || charmSkills.size() < 2) return null;
        return charmSkills.get(1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private ArmorWrapper headArmour;
    private ArmorWrapper chestArmour;
    private ArmorWrapper armArmour;
    private ArmorWrapper waistArmour;
    private ArmorWrapper legArmour;
    private List<JewelWrapper> headJewels;
    private List<JewelWrapper> chestJewels;
    private List<JewelWrapper> armJewels;
    private List<JewelWrapper> waistJewels;
    private List<JewelWrapper> legJewels;
    private List<JewelWrapper> wepJewels;
    private List<JewelWrapper> charmJewels;
    private List<CharmSkill> charmSkills;
    private int numCharmSlots;
    private String name;
    private Long id;
}
