/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 07-Aug-2011 18:57:50
 */
public class CharmPoint implements Serializable {

    public CharmPoint(int skillPoint, int skillId, String key) {
        this.skillPoint = skillPoint;
        this.skillId = skillId;
        this.key = key;
    }

    public int getSkillPoint() {
        return skillPoint;
    }

    public int getSkillId() {
        return skillId;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CharmPoint that = (CharmPoint) o;

        if (skillId != that.skillId) return false;
        if (skillPoint != that.skillPoint) return false;
        if (!key.equals(that.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = skillPoint;
        result = 31 * result + skillId;
        result = 31 * result + key.hashCode();
        return result;
    }

    private int skillPoint;
    private int skillId;
    private String key;
}
