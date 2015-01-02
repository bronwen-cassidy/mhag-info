/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Sep-2011 11:35:24
 */
public class SetSkill extends DefaultDomainObject implements Comparable<SetSkill> {

    public SetSkill() {
    }

    public SetSkill(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSavedSetId(Long savedSetId) {
        this.savedSetId = savedSetId;
    }

    public Long getSavedSetId() {
        return savedSetId;
    }

    public int compareTo(SetSkill o) {
        return name.compareTo(o.getName());
    }

    public String toString() {
        return "SetSkill{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetSkill setSkill = (SetSkill) o;

        if (name != null ? !name.equals(setSkill.name) : setSkill.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    private String name;
    private Long savedSetId;
}
