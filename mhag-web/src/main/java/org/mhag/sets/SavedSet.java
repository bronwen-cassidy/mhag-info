/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

import org.springframework.web.util.JavaScriptUtils;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Sep-2011 11:33:29
 */
public class SavedSet extends DefaultDomainObject implements Comparable<SavedSet> {

    public SavedSet(String name, String code, String owner) {
        this(name, code);
        this.owner = owner;
    }

    public SavedSet(String setName, String data) {
        this.name = setName;
        this.setCode = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetCode() {
        return setCode;
    }

    public String getEscapedName() {
        return JavaScriptUtils.javaScriptEscape(name);
    }

    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }

    public String getDisplaySetCode() {
        return name + " : " + setCode.substring(setCode.indexOf(":%20") + 4);
    }

    public String getCodeValue() {
        return setCode.substring(setCode.indexOf(":%20") + 3);
    }

    public Set<SetSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<SetSkill> skills) {
        this.skills = skills;
    }

    public String getSkillsDisplayValue() {
        if (!skills.isEmpty()) {
            // sort skills to be in alphabetic order

            StringBuilder display = new StringBuilder();
            int index = 1;
            for (SetSkill skill : skills) {
                display.append(skill.getName());
                if (index++ < skills.size()) display.append(", ");
            }
            return display.toString();
        }
        return "";
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void addSkill(SetSkill setSkill) {
        skills.add(setSkill);
    }

    public void calculateWeighting(List<String> searchSkills) {
        double numMatching = 0.0;
        for (SetSkill skill : skills) {
            if (searchSkills.contains(skill.getName())) {
                numMatching++;
            }
        }
        this.weighting = 0;
        if (searchSkills.size() > 0) this.weighting = (double) (numMatching / searchSkills.size()) * 100;

    }

    public double getWeighting() {
        DecimalFormat df = new DecimalFormat("#.##");
        String res = df.format(weighting);
        return new Double(res);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SavedSet savedSet = (SavedSet) o;

        if (id != null ? !id.equals(savedSet.id) : savedSet.id != null) return false;
        if (name != null ? !name.equals(savedSet.name) : savedSet.name != null) return false;
        if (owner != null ? !owner.equals(savedSet.owner) : savedSet.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    public int compareTo(SavedSet o) {
        return new Double(o.weighting).compareTo(weighting);
    }

    public void setNumWeaponSlots(int numWeaponSlots) {
        this.numWeaponSlots = numWeaponSlots;
    }

    public int getNumWeaponSlots() {
        return numWeaponSlots;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getNumUpVotes() {
        return numUpVotes;
    }

    public void setNumUpVotes(int numVotes) {
        this.numUpVotes = numVotes;
    }

    public int getNumDownVotes() {
        return numDownVotes;
    }

    public void setNumDownVotes(int numDownVotes) {
        this.numDownVotes = numDownVotes;
    }

    private String name;
    private String setCode;
    private Set<SetSkill> skills = new LinkedHashSet<SetSkill>();
    private String owner;
    private double weighting;
    private int numWeaponSlots;
    private String ipAddress;
    private Long userId;
    private int numUpVotes;
    private int numDownVotes;
}
