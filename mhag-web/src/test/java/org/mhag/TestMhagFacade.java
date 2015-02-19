/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 31-Jul-2011 10:14:32
 * @version 0.1
 */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMhagFacade {

    @BeforeClass
    public static void setup() {
        mhagFacade = new MhagFacade();
        mhagFacade.setVersion(0);
        try {
            mhagFacade.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        mhagFacade = null;
    }

    @Test
    public void testGetArmours() throws Exception {
        //boolean lowRank, boolean blade, boolean female, int bodyPart
        final List<ArmorWrapper> armorList = mhagFacade.getArmours(0, true, true, 0);
        final List<ArmorWrapper> armorList2 = mhagFacade.getArmours(0, true, true, 0);

        assertEquals(armorList, armorList2);
    }

    @Test
    public void testGetArmoursInfoSet() throws Exception {
        //boolean lowRank, boolean blade, boolean female, int bodyPart
        final List<ArmorWrapper> armorList = mhagFacade.getArmours(0, true, true, 0);
        final ArmorWrapper armor = armorList.get(0);
        assertFalse(armor.getArmorID() == -1);
    }

    @Test
    public void testGetJewelInfo() throws Exception {
        //boolean lowRank, boolean blade, boolean female, int bodyPart
        final List<JewelWrapper> jewels = mhagFacade.getJewels(0, 3, 0);
        for (JewelWrapper jewel : jewels) {
            assertNotNull(jewel.getJewelName());
            assertTrue(jewel.getJewelName().length() > 0);            
        }
    }


    @Test
    public void testBuildResistences() {

        // splits to 0=id or null, 1=numSlots, 2=bodyPart
        String[] armours = {"115:0:0", "7:1:1", "8:1:3", "8:0:4"};
        final Map<String, List<Cell>> map = mhagFacade.buildResistences(armours, 0);
        assertNotNull(map.get("Defence"));
    }

    @Test
    public void testGetCharmPoints() {
        // auto guard id = 2 BBQ = 3 blight res = 4
        mhagFacade.getCharmPoints(1, 4, 0, 1);
    }

    @Test
    public void testBuildSetArmours() throws Exception {
        //H B H 69 1 121 C 87 1 120 A 68 1 120 W 76 2 120 120 L 20 1 120 X 2 120 111 Y 0 1 13 6 0
        String code = "H.B.H.69.1.121.C.87.1.120.A.68.1.120.W.76.2.120.120.L.20.1.120.X.2.120.111.Y.0.1.13.6.0";
        ArmourSet armourSet = mhagFacade.buildArmourSet(code, 0);
        ArmorWrapper headArmour = armourSet.getHeadArmour();
        ArmorWrapper chestArmour = armourSet.getChestArmour();
        assertEquals("Baggi Helm+", headArmour.getArmorName());
        assertEquals("Dober Mail", chestArmour.getArmorName());
    }

    @Test
    public void testBuildSetJewels() throws Exception {
        //H B H 69 1 121 C 87 1 120 A 68 1 120 W 76 2 120 120 L 20 1 120 X 2 120 111 Y 0 1 13 6 0
        String code = "H.B.H.69.1.121.C.87.1.120.A.68.1.120.W.76.2.120.120.L.20.1.120.X.2.120.111.Y.0.1.13.6.0";
        ArmourSet armourSet = mhagFacade.buildArmourSet(code, 1);
        List<JewelWrapper> headJewels = armourSet.getHeadJewels();
        assertEquals(1, headJewels.size());
        assertEquals("Checkmate Jewel", headJewels.get(0).getJewel().getJewelName());
    }

    @Test
    public void testBuildUrl() throws Exception {
        String[] charms = {"60:1:wep:0", "62:1:head:0", "62:1:head:1", "62:1:talisman:0"};
        // splits to 0=id or null, 1=numSlots, 2=bodyPart
        String[] armours = {"-4:1:wep", "115:0:0", "7:1:1", "8:1:3", "8:0:4"};
        String url = mhagFacade.buildUrl("Ziggi's maic + 1 set", charms, armours, 1, true, 3);
        System.out.println("url = " + url);
    }


    private static MhagFacade mhagFacade;
}