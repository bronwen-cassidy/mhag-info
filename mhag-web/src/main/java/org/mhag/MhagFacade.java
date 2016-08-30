/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.mhag.model.*;
import org.mhag.sets.Rank;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Jul-2011 21:27:01
 */
public class MhagFacade implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        Mhag mhag = new Mhag(version);
        mhag.setOutLog(System.out);
        mhag.setLogOpt(2);

        mhagData = new MhagData();
        mhagData.readFile(mhag);
        mhagData.dataPreProc();
    }

    public List<ArmorWrapper> getArmours(int rank, boolean blade, boolean female, int bodyPart) {
        final String armourKey = rank + ":" + blade + ":" + female + ":" + bodyPart;
        List<ArmorWrapper> result = armours.get(armourKey);
        if (result != null) return result;

        result = new ArrayList<ArmorWrapper>();
        final int[] pieces = mhagData.getArmorList(rank, blade, female, bodyPart);
        for (int piece : pieces) {
            if (piece == -1) continue;
            result.add(new ArmorWrapper(mhagData.getArmor(bodyPart, piece), female));
        }
        armours.put(armourKey, result);
        return result;
    }
    public List<ArmorWrapper> getAllArmours(int bodyPart) {

        List<Armor> fullArmorList = mhagData.getFullArmorList(bodyPart);
        List<ArmorWrapper> result = new ArrayList<ArmorWrapper>();
        for (Armor armor : fullArmorList) {
            result.add(new ArmorWrapper(armor, armor.ifFemale()));
        }
        return result;
    }

    public List<JewelWrapper> getJewels(int rank, int numSlots, int language) {
        final int[] ids = mhagData.getJewelList(rank, numSlots);

        List<JewelWrapper> result = new ArrayList<JewelWrapper>();

        for (int id : ids) {
            if (id == -1) continue;
            Jewel jewel = mhagData.getJewel(id);
            result.add(new JewelWrapper(jewel, jewel.getJewelNameSkill(mhagData, language)));
        }

        return result;
    }

    public List<CharmSkill> getSkills(int rank, int nSlots, int charmId, String exclude) {

        List<CharmSkill> result = new ArrayList<CharmSkill>();
        int[] skillIds;
        if (exclude != null)
            skillIds = mhagData.getSkillList(rank, nSlots, charmId, exclude);
        else
            skillIds = mhagData.getSkillList(rank, nSlots, charmId);

        for (int skillId : skillIds) {
            final Skill skill = mhagData.getSkill(skillId);
            result.add(new CharmSkill(skill, nSlots));
        }
        return result;
    }

    public List<SkillEffect> getSkillEffects(int language) {
        final Effect[] effectList = mhagData.getEffectList();
        List<SkillEffect> effects = new ArrayList<SkillEffect>();

        for (Effect effect : effectList) {
            if (language == 0) {
                effects.add(new SkillEffect(effect.getSkillName(), effect.getEffectName()));
            } else {
                effects.add(new SkillEffect(effect.getSkillName(), effect.getEffectNameJP()));
            }
        }
        Collections.sort(effects);
        return effects;
    }

    /**
     *
     * @param armours - an array of the armour piece ids
     * @param rank - low = 0 / high = 1 / g-rank = 2
     * @return - the map providing the information for the resistence table
     */
    public Map<String, List<Cell>> buildResistences(String[] armours, int rank) {
        Map<String, List<Cell>> result = initResMap();
        for (String armour : armours) {
            String[] pieces = StringUtils.delimitedListToStringArray(armour, ":");
            int id = Integer.parseInt(pieces[0]);
            if (WEP.equals(pieces[2]) || TALI.equals(pieces[2])) continue;

            int armourPiece = Integer.parseInt(pieces[2]);
            Armor armor = mhagData.getArmor(armourPiece, id);
            final int[] resistences = armor.getResist();

            for (int j = 0; j < resistences.length; j++) {
                int resistence = resistences[j];
                final List<Cell> cells = result.get(resHeaders[j]);
                final Cell cell = cells.get(armourPiece);
                cell.setResPoints(resistence);
            }

            final List<Cell> defCells = result.get(DEFENCE);
            Cell defCell = defCells.get(armourPiece);
            defCell.setDefence(armor.getDefense(rank));
        }

        int[] totals = new int[6];
        int index = 0;
        for (Map.Entry<String, List<Cell>> entry : result.entrySet()) {
            final List<Cell> values = entry.getValue();
            for (int i = 0; i < values.size() - 1; i++) {
                Cell cell = values.get(i);
                if (DEFENCE.equals(entry.getKey())) {
                    totals[index] += cell.getDefence();
                } else {
                    totals[index] += cell.getResPoints();
                }
            }
            final Cell totalCell = values.get(5);
            if (DEFENCE.equals(entry.getKey())) {
                totalCell.setDefence(totals[index]);
            } else {
                totalCell.setResPoints(totals[index]);
            }
            index++;
        }
        return result;
    }

    public Map<String, Object> buildSkills(String[] charms, String[] armours) {

        // maps skill -> part -> details
        Map<SkillKey, Map<String, Cell>> result = new LinkedHashMap<SkillKey, Map<String, Cell>>();
        List<SkillKey> torsoUpKeys = new ArrayList<SkillKey>();
        Map<String, String> armourMaterials = new LinkedHashMap<String, String>();
        Map<String, String> jewelMaterials = new LinkedHashMap<String, String>();

        for (String armour : armours) {
            // splits to 0=id or null, 1=numSlots, 2=bodyPart
            String[] pieces = StringUtils.delimitedListToStringArray(armour, ":");
            if (pieces.length < 1) continue;

            int id = Integer.parseInt(pieces[0]);

            String bp = pieces[2];
            if (WEP.equals(bp)) continue;
            if (TALI.equals(bp)) continue;
            if (CHARM.equals(bp)) {
                // goes under tali
                Skill skill = mhagData.getSkill(id);
                SkillKey k = new SkillKey(skill, TALI);
                Map<String, Cell> column = getColumnRow(result, k);
                Cell cell = column.get(TALI);
                cell.setSkillPoints(Integer.parseInt(pieces[1]));
                cell.setSkillName(skill.getSkillName());
                column.put(TALI, cell);
                continue;
            }

            int bodyPart = Integer.parseInt(bp);
            String part = convert(bodyPart);

            Armor armor = mhagData.getArmor(bodyPart, id);
            armourMaterials.put(part, armor.getItem());
            final int[] skillIds = armor.getSkillID();

            for (int j = 0; j < armor.getNumSkill(); j++) {
                int skillId = skillIds[j];

                String skillName;
                int skillPonits = 0;
                SkillKey k;
                // torso up skill
                if (skillId == -1) {
                    // torso up skill need to copy the torso cell for all skills
                    k = new SkillKey(new TorsoUpSkill(-1, part), part);
                    torsoUpKeys.add(k);
                    skillName = TORSO_UP;

                } else {

                    Skill skill = mhagData.getSkill(skillId);
                    k = new SkillKey(skill, part);
                    skillPonits = armor.getSkillPoint()[j];
                    skillName = armor.getSkillName()[j];
                }

                Map<String, Cell> column = getColumnRow(result, k);
                Cell cell = column.get(part);
                cell.setSkillPoints(skillPonits);
                cell.setSkillName(skillName);
                column.put(part, cell);
            }
        }

        processJewels(charms, result, jewelMaterials);
        //process torso up after the jewels as torso up also ups the jewels
        processTorsoUp(result, torsoUpKeys);
        // process the totals
        processTotals(result);

        Map<SkillKey, Map<String, Cell>> skillKeyMapResult = MhagUtils.sortResults(result);
        Map<String, Object> processed = new HashMap<String, Object>();
        processed.put(SET_SKILLS_KEY, skillKeyMapResult);
        if(mhagData.getIfItem()) {
            processed.put(ARMOR_MATERIALS, armourMaterials);
            processed.put(JEWEL_MATERIALS, jewelMaterials);
        }
        return processed;
    }

    public int[] getCharmPoints(int rank, int charmSkillId, int numSlots, int index) {

        final Skill skill = mhagData.getSkill(charmSkillId);
        return getCharmPoints(rank, numSlots, index, skill);
    }

    private int[] getCharmPoints(int rank, int numSlots, int index, Skill skill) {
        if (skill.getSkillName().equals("Auto-Guard")) {
            // fixed skill point for auto-guard
            return new int[]{10, 10};
        }
        final int max = skill.getMaxSkillPoint(rank, numSlots);
        int min = 0;
        if (index == 2) {
            min = -10;
        }
        return new int[]{max, min};
    }

    private List<CharmPoint> buildPointList(int[] minMaxVals, int skillId) {
        List<CharmPoint> points = new ArrayList<CharmPoint>();
        for (int i = minMaxVals[0]; i >= minMaxVals[1]; i--) {
            String key = skillId + ":" + i + ":charm";
            if (i == 0) key = "---";
            points.add(new CharmPoint(i, skillId, key));
        }
        return points;
    }

    public String buildUrl(String name, String[] charms, String[] armours, int rank, boolean blade, int numTaliSlots) {
        org.mhag.model.Set s = new org.mhag.model.Set();
        Mhag mhag = new Mhag(version);
        mhag.setOutLog(System.out);
        mhag.setLogOpt(2);

        s.setSetName(name);
        s.setRank(rank);
        s.setBlade(blade);

        int charmSkillIndex = 0;
        int numCharmSkills = 0;
        for (String armour : armours) {
            String[] pieces = StringUtils.delimitedListToStringArray(armour, ":");
            String bp = pieces[2];
            if (CHARM.equals(bp)) {
                s.setCharmSkillID(charmSkillIndex, Integer.parseInt(pieces[0]));
                s.setCharmSkillPoint(charmSkillIndex++, Integer.parseInt(pieces[1]));
                numCharmSkills++;
                s.setInUse(6, true);
            } else if (!(WEP.equals(bp) || TALI.equals(bp))) {
                s.setArmorID(Integer.parseInt(bp), Integer.parseInt(pieces[0]));
                s.setInUse(Integer.parseInt(bp), true);
            }
        }

        s.setNumCharmSkill(numCharmSkills);
        s.setNumCharmSlot(numTaliSlots);
        if (numTaliSlots > 0) s.setInUse(6, true);

        int[] numJewels = new int[7];
        for (String charmStr : charms) {
            String[] charmPieces = StringUtils.delimitedListToStringArray(charmStr, ":");
            // todo determine when this would be a case
            if (charmPieces.length < 1) continue;
            String idStr = charmPieces[0];
            String bp = charmPieces[2];
            int id = Integer.parseInt(idStr);
            int bodyPart = getIndex(bp);
            numJewels[bodyPart] = numJewels[bodyPart] + 1;
            s.setInUse(bodyPart, true);
            s.setJewelID(bodyPart, numJewels[bodyPart] - 1, id);
        }

        for (int i = 0; i < numJewels.length; i++) {
            int numJewel = numJewels[i];
            s.setNumJewel(i, numJewel);
        }

        return s.getSetCodeURI(mhag).toString();
    }

    public static int getIndex(String part) {
        for (int i = 0; i < artefacts.length; i++) {
            String artefact = artefacts[i];
            if (artefact.equals(part)) return i;
        }
        return -1;
    }

    public ArmourSet buildArmourSet(String code, int language) {

        Mhag mhag = new Mhag(version);
        mhag.setOutLog(System.out);
        mhag.setLogOpt(2);
        org.mhag.model.Set s = new org.mhag.model.Set();
        String line = StringUtils.replace(code, ".", " ");

        s.setSetFromCode(mhag, line);

        ArmourSet armourSet = new ArmourSet();
        int[] armourIds = s.getArmorID();
        if (s.getInUse(0)) armourSet.setHeadArmour(new ArmorWrapper(mhagData.getArmor(0, armourIds[0]), false));
        if (s.getInUse(1)) armourSet.setChestArmour(new ArmorWrapper(mhagData.getArmor(1, armourIds[1]), false));
        if (s.getInUse(2)) armourSet.setArmArmour(new ArmorWrapper(mhagData.getArmor(2, armourIds[2]), false));
        if (s.getInUse(3)) armourSet.setWaistArmour(new ArmorWrapper(mhagData.getArmor(3, armourIds[3]), false));
        if (s.getInUse(4)) armourSet.setLegArmour(new ArmorWrapper(mhagData.getArmor(4, armourIds[4]), false));

        int[][] jewelIds = s.getJewelID();
        if (s.getNumJewel(0) != 0) armourSet.setHeadJewels(getJewels(jewelIds[0], s.getNumJewel(0), language));
        if (s.getNumJewel(1) != 0) armourSet.setChestJewels(getJewels(jewelIds[1],s.getNumJewel(1), language));
        if (s.getNumJewel(2) != 0) armourSet.setArmJewels(getJewels(jewelIds[2], s.getNumJewel(2),language));
        if (s.getNumJewel(3) != 0) armourSet.setWaistJewels(getJewels(jewelIds[3], s.getNumJewel(3), language));
        if (s.getNumJewel(4) != 0) armourSet.setLegJewels(getJewels(jewelIds[4], s.getNumJewel(4), language));
        if (s.getNumJewel(5) != 0) armourSet.setWepJewels(getJewels(jewelIds[5], s.getNumJewel(5), language)); //weapon in use
        if (s.getInUse(6)) {
            armourSet.setCharmJewels(getJewels(jewelIds[6],s.getNumJewel(6), language));
            armourSet.setCharmSkills(getCharmSkills(s));
        }
        armourSet.setNumCharmSlots(s.getNumCharmSlot());
        return armourSet;
    }

    public ArmorWrapper getArmour(int armourId, int part, boolean female) {
        return new ArmorWrapper(mhagData.getArmor(part, armourId), female);
    }

    private List<JewelWrapper> getJewels(int[] jewelIds, int numJewels, int language) {
        List<JewelWrapper> jw = new ArrayList<JewelWrapper>();
        for (int i = 0; i < numJewels; i++) {
            int jewelId = jewelIds[i];
            if (jewelId >= 0) {
                Jewel jewel = mhagData.getJewel(jewelId);
                jw.add(new JewelWrapper(jewel, jewel.getJewelNameSkill(mhagData, language)));
            }
        }
        return jw;
    }

    private List<CharmSkill> getCharmSkills(org.mhag.model.Set s) {
        List<CharmSkill> result = new ArrayList<CharmSkill>();
        int[] skillIds = s.getCharmSkillID();
        int[] points = s.getCharmSkillPoint();
        int index = 1;
        for (int i = 0; i < skillIds.length; i++) {
            int skillId = skillIds[i];
            final int charmPoint = points[i];
            // if the charm points are 0 this skill has not been set
            if (charmPoint != 0) {
                Skill skill = mhagData.getSkill(skillId);
                CharmSkill charmSkill = new CharmSkill(skill, 0);
                charmSkill.setPoints(charmPoint);
                int[] range = getCharmPoints(s.getRank(), charmSkill.getNumSlots(), index++, skill);
                List<CharmPoint> p = buildPointList(range, skillId);
                charmSkill.setCharmPoints(p);
                result.add(charmSkill);
            }
        }
        return result;
    }

    private Map<String, List<Cell>> initResMap() {
        Map<String, List<Cell>> result = new LinkedHashMap<String, List<Cell>>();
        for (String resHeader : resHeaders) {
            result.put(resHeader, initResList());
        }
        return result;
    }

    private List<Cell> initResList() {
        List<Cell> result = new ArrayList<Cell>();
        for (String armourHeader : armourHeaders) {
            result.add(new Cell(armourHeader));
        }
        result.add(new Cell("Total"));
        return result;
    }

    private void processTotals(Map<SkillKey, Map<String, Cell>> result) {
        for (Map.Entry<SkillKey, Map<String, Cell>> entry : result.entrySet()) {

            final Map<String, Cell> value = entry.getValue();
            final Cell totalCell = value.get(TOTAL);
            totalCell.setSkillName(entry.getKey().getSkillName());
            for (Map.Entry<String, Cell> cellEntry : value.entrySet()) {
                if (!ignoreKey(cellEntry.getKey())) {
                    final Cell cell = cellEntry.getValue();
                    totalCell.setSkillPoints(cell.getSkillPoints());
                }
            }

            // activate skill
            final Cell skillCell = value.get(ACTIVATED_SKILL);

            SkillKey key = entry.getKey();
            final Skill skill = key.getSkill();
            if (skill instanceof TorsoUpSkill) {
                skillCell.setSkillName(TORSO_UP);
                totalCell.setActivated(true);
                continue;
            }
            final int currentPoints = totalCell.getSkillPoints();
            final int num = skill.getNumEffect();
            if (currentPoints >= 10) {
                for (int i = num - 1; i >= 0; i--) {
                    int trigger = skill.getEffectTrigger()[i];
                    if (trigger > 0 && trigger <= currentPoints) {
                        String effectName = skill.getEffectName()[i];
                        skillCell.setSkillName(effectName);
                        totalCell.setActivated(true);
                        break;
                    }
                }

            } else if (currentPoints <= -10) {
                for (int i = 0; i < num; i++) {
                    int trigger = skill.getEffectTrigger()[i];
                    if ((trigger < 0) && (trigger >= currentPoints)) {
                        String effectName = skill.getEffectName()[i];
                        skillCell.setSkillName(effectName);
                        totalCell.setActivated(true);
                        break;
                    }
                }
            }
        }
    }

    private boolean ignoreKey(String key) {
        return TOTAL.equals(key) || ACTIVATED_SKILL.equals(key);
    }

    private void processTorsoUp(Map<SkillKey, Map<String, Cell>> result, List<SkillKey> torsoUpKeys) {

        for (SkillKey key : torsoUpKeys) {
            // get the torsoup part
            String part = key.getPart();
            // get the torso column the chest
            for (Map.Entry<SkillKey, Map<String, Cell>> entry : result.entrySet()) {
                final SkillKey key1 = entry.getKey();
                if (torsoUpKeys.contains(key1)) continue;
                final Map<String, Cell> value = entry.getValue();
                // get the cell for the torso piece
                Cell cell = value.get(CHEST);
                if (cell != null) {
                    // copy the value to the torso
                    Cell torsoCell = value.get(part);
                    if (torsoCell == null) torsoCell = new Cell(part);
                    torsoCell.setSkillName(cell.getSkillName());
                    torsoCell.setSkillPoints(cell.getSkillPoints());
                    torsoCell.setType(Cell.TORSO_UP);
                    value.put(part, torsoCell);
                }
                // get the cell for the torso and copy it's values to the peice represented by torso up
            }
            //result.remove(key);
        }
    }

    private void processJewels(String[] charms, Map<SkillKey, Map<String, Cell>> result, Map<String, String> materials) {
        //54:2:head,122:1:head,103:1:arm,50:0:talisman:42
        for (String charmStr : charms) {
            String[] charmPieces = StringUtils.delimitedListToStringArray(charmStr, ":");
            if (charmPieces.length < 1) continue;
            String idStr = charmPieces[0];
            String bp = charmPieces[2];
            SkillKey k;

            int id = Integer.parseInt(idStr);

            // looking for jewels on armours and the wep
            Jewel jewel = mhagData.getJewel(id);
            materials.put(jewel.getJewelNameShort(), jewel.getItem());
            int numSkills = jewel.getNumSkill();

            for (int j = 0; j < numSkills; j++) {
                int skillId = jewel.getSkillID()[j];
                Skill skill = mhagData.getSkill(skillId);
                k = new SkillKey(skill, bp);
                Map<String, Cell> column = getColumnRow(result, k);

                Cell c = column.get(bp);
                if (c == null) c = new Cell(bp);

                c.setSkillPoints(jewel.getSkillPoint()[j]);
                c.setSkillName(jewel.getSkillName()[j]);
                column.put(bp, c);
                result.put(k, column);
            }
        }
    }

    private Map<String, Cell> getColumnRow(Map<SkillKey, Map<String, Cell>> result, SkillKey k) {
        Map<String, Cell> column = result.get(k);
        if (column == null) {
            column = initColumns();
            result.put(k, column);
        }
        return column;
    }

    private Map<String, Cell> initColumns() {
        Map<String, Cell> result = new LinkedHashMap<String, Cell>();
        for (String header : columnHeaders) {
            result.put(header, new Cell(header));
        }
        return result;
    }

    private String convert(int bodyPart) {
        switch (bodyPart) {
            case 0:
                return "head";
            case 1:
                return CHEST;
            case 2:
                return ARMS;
            case 3:
                return "waist";
            case 4:
                return "legs";
        }
        return "";
    }

    public Rank[] getRankValues() {
        return MhagUtils.getRankValues(version);
    }

    public int getDefaultRank() {
        switch (version) {
            case 0: return 1;
            case 1: return 1;
            case 2: return 1;
            case 3: return 2;
            case 4: return 2;
            case 5: return 1;
            default: return 1;
        }
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Skill[] getSkillList() {
        return mhagData.getSkillList();
    }

    public static final String SET_SKILLS_KEY = "set";
    public static final String ARMOR_MATERIALS = "armorMaterials";
    public static final String JEWEL_MATERIALS = "jewelMaterials";

    private MhagData mhagData;
    private Map<String, List<ArmorWrapper>> armours = new HashMap<String, List<ArmorWrapper>>();
    public static final String TALI = "talisman";
    public static final String HEAD = "head";
    public static final String CHEST = "chest";
    public static final String ARMS = "arms";
    public static final String WAIST = "waist";
    public static final String LEGS = "legs";
    public static final String TORSO_UP = "Torso Up";
    public static final String WEP = "wep";
    public static final String TOTAL = "total";
    public static final String ACTIVATED_SKILL = "skill";
    private static final String[] columnHeaders = {WEP, HEAD, CHEST, ARMS, WAIST, LEGS, TALI, TOTAL, ACTIVATED_SKILL};
    private static final String[] armourHeaders = {HEAD, CHEST, ARMS, WAIST, LEGS};
    private static final String[] artefacts = {HEAD, CHEST, ARMS, WAIST, LEGS, WEP, TALI};
    //Fire/Water/Ice/Thunder/Dragon
    public static final String DEFENCE = "Defence";
    private static final String[] resHeaders = {"fire", "water", "ice", "thunder", "dragon", DEFENCE};

    public static final String CHARM = "charm";
    private int version = 0;
}
