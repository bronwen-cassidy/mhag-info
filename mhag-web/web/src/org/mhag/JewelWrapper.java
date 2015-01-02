/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.mhag.model.Jewel;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Sep-2011 16:50:04
 */
public class JewelWrapper {

    public JewelWrapper(Jewel jewel, String jewelNameSkill) {
        this.jewel = jewel;
        this.jewelNameSkill = jewelNameSkill;
    }

    public Jewel getJewel() {
        return jewel;
    }

    public boolean isLinked() {
        return jewel.getNumSlot() > 1;
    }

    public int getNumSlot() {
        return jewel.getNumSlot();
    }

    public int getJewelID() {
        return jewel.getJewelID();
    }

    public String getJewelNameSkill() {
        return jewelNameSkill;
    }

    public String getJewelName() {
        return jewel.getJewelName();
    }

    private Jewel jewel;
    private String jewelNameSkill;
}
