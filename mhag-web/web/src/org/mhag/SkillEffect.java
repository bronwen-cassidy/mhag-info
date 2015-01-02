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
 * @since 12-Oct-2011 11:14:08
 */
public class SkillEffect implements Serializable, Comparable<SkillEffect> {

    public SkillEffect(String skillName, String effectName) {
        this.skillName = skillName;
        this.effectName = effectName;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getEffectName() {
        return effectName;
    }

    public int compareTo(SkillEffect o) {
        return effectName.compareTo(o.getEffectName());
    }

    private String skillName;
    private String effectName;
}
