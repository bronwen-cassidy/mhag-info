package org.mhag.builder;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * User: Bronwen Cassidy
 * Date: 23/01/14
 * Time: 10:50
 * Translates Athena's ASS into our format. We build the all.txt first which is done in the order of, head/body/arms/waist/legs
 */
public class LoaderUtils {

    private static final String DIVIDER = " : ";

    public static void main(String[] args) throws Exception {
        LoaderUtils loaderUtils = new LoaderUtils();
        //loaderUtils.skills();
        //System.out.println("##########################################");
        //loaderUtils.charms();
        //System.out.println("##########################################");
        loaderUtils.loadArmour();
    }

    private void charms() {
        Map<String, String> mats = loadMaterials("/data/Languages/English/components.txt", "/data/Languages/Japanese/components.txt");
        Map<String, String> decorations = loadMaterials("/data/Languages/English/decorations.txt", "/data/Languages/Japanese/decorations.txt");
        Map<String, String> skills = loadMaterials("/data/Languages/English/skills.txt", "/data/Languages/Japanese/skills.txt");
        Map<String, String[]> data = loadData("/data/decorations.txt", ",", 0);
        writeJewelsAndMats(data, decorations, mats, skills);
    }


    private void skills() {
        Map<String, String> langSkills = loadMaterials("/data/Languages/English/skills.txt", "/data/Languages/Japanese/skills.txt");
        Map<String, String[]> data = loadData("/data/skills.txt", ",", 0);
        writeSkills(data, langSkills);
    }

    private void writeSkills(Map<String, String[]> data, Map<String, String> langSkills) {
        Map<String, StringBuilder> result = new LinkedHashMap<String, StringBuilder>();
        for (Map.Entry<String, String[]> entry : data.entrySet()) {
            String[] values = entry.getValue();
            String japSkillName = entry.getKey();
            if (org.apache.commons.lang.StringUtils.isBlank(japSkillName) || values.length < 2) {
                System.out.println("************************************* BLANK || TORSO UP ************************************");
                continue;
            }

            String japSkillCategory = values[1];
            String engSkillName = langSkills.get(japSkillName);
            String engSkillCategory = langSkills.get(japSkillCategory);
            int numPoints = Integer.parseInt(values[2]);

            int gba = Integer.parseInt(values[3]);
            String gunBladeAll = gba == 0 ? "A" : gba == 1 ? "B" : "G";
            StringBuilder details;
            if (result.containsKey(engSkillCategory)) {
                details = result.get(engSkillCategory);
                details.append(DIVIDER);
            } else {
                details  = new StringBuilder();
                details.append(japSkillCategory).append(DIVIDER);
            }
            details.append(engSkillName).append(DIVIDER).append(japSkillName)
                    .append(DIVIDER).append(numPoints);
            result.put(engSkillCategory, details);
        }

        List<String> lines = new ArrayList<String>();
        for (Map.Entry<String, StringBuilder> entry : result.entrySet()) {
            lines.add(entry.getKey() + DIVIDER + entry.getValue().toString());
        }
        // todo now write the result to a file
        writeData(lines, "./mhag/data/mhgen/skillsxxxxxx.dat");
    }

    private void writeData(List<String> lines, String file) {
        try {
            Path path = Paths.get(file);
            Path parentDir = path.getParent();
            if (!Files.exists(parentDir)) {
                System.out.println("weeeeeeeeeeeeeeeeeeeee " + path.getParent());
                Files.createDirectories(parentDir);
            }
            Files.write(path, lines, StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeJewelsAndMats(Map<String, String[]> data, Map<String, String> decoTranslations, Map<String, String> matTranslation, Map<String, String> skillTranslation) {
        Map<String, List<String>> jewelMats = new LinkedHashMap<String, List<String>>();
        Map<String, List<String>> jewelData = new LinkedHashMap<String, List<String>>();
        int index = 0;
        for (Map.Entry<String, String[]> entry : data.entrySet()) {
            String key = entry.getKey();
            String[] info = entry.getValue();

            String engTranslation = decoTranslations.get(key);
            if (engTranslation == null) {
                System.out.println("************* jap name: " + key + " index: " + index + "  ******************");
                //continue;
                engTranslation = "??????";
            }

            // materials data structure
            List<String> matsRow = jewelMats.get(key);
            if (matsRow == null) {
                matsRow = new ArrayList<String>();
                jewelMats.put(key, matsRow);
            }

            // jewel info
            if (jewelData.containsKey(key)) {
                System.out.println("error duplicate key found for " + key + " eng translation " + engTranslation);
            }
            List<String> row = jewelData.get(key);
            if (row == null) {
                row = new ArrayList<String>();
                jewelData.put(key, row);
            }
            row.add(engTranslation);
            matsRow.add(engTranslation);
            row.add(key);
            // high rank low rank
            int rarity = Integer.parseInt(info[3].trim());
            String rank = rarity < 4 ? "L" : "H";
            row.add(rank);
            // slots
            row.add(info[2].trim());
            List<String> japMats = new ArrayList<String>();
            // skill and points
            for (int i = 6; i < info.length; i++) {
                String name = info[i];
                // find the english name in the skill translations
                if (skillTranslation.containsKey(name)) {
                    // doing the skills
                    String engSkill = skillTranslation.get(name);
                    String val = info[i + 1];
                    row.add(engSkill);
                    row.add(val);
                    i++;
                } else {
                    // doing the materials
                    String engMat = matTranslation.get(name);
                    String val = info[i + 1];
                    matsRow.add(engMat + " x" + val);
                    japMats.add(name + " x" + val);
                    i++;
                }
            }
            matsRow.addAll(japMats);
            index++;
        }


        List<String> lines = new ArrayList<String>();
        for (List<String> inf : jewelData.values()) {
            lines.add(StringUtils.join(inf, DIVIDER));
        }
        // todo now write the result to a file
        writeData(lines, "./mhag/data/mhgen/jewelsxxxxxx.dat");

        System.out.println("###########################################################");

        List<String> lines2 = new ArrayList<String>();
        for (List<String> inf : jewelMats.values()) {
            lines2.add(StringUtils.join(inf, DIVIDER));
        }
        writeData(lines2, "./mhag/data/mhgen/matsxxxxxx.dat");
    }

    private void loadArmour() throws Exception {

        List<Map<String, String[]>> mh4data = new ArrayList<Map<String, String[]>>(5);
        mh4data.add(loadData("/data/head.txt", ",", 0));
        mh4data.add(loadData("/data/body.txt", ",", 0));
        mh4data.add(loadData("/data/arms.txt", ",", 0));
        mh4data.add(loadData("/data/waist.txt", ",", 0));
        mh4data.add(loadData("/data/legs.txt", ",", 0));

        // jap to english armour translation just english mapped by index
        List<Map<String, String>> mhTranslations = new ArrayList<Map<String, String>>(5);
        mhTranslations.add(loadMaterials("/data/Languages/English/head.txt", "/data/Languages/Japanese/head.txt"));
        mhTranslations.add(loadMaterials("/data/Languages/English/body.txt", "/data/Languages/Japanese/body.txt"));
        mhTranslations.add(loadMaterials("/data/Languages/English/arms.txt", "/data/Languages/Japanese/arms.txt"));
        mhTranslations.add(loadMaterials("/data/Languages/English/waist.txt", "/data/Languages/Japanese/waist.txt"));
        mhTranslations.add(loadMaterials("/data/Languages/English/legs.txt", "/data/Languages/Japanese/legs.txt"));

        Map<String, String> skills = loadSkills("/data/mh4/skill.dat");
        Map<String, String> mats = loadMaterials("/data/Languages/English/components.txt", "/data/Languages/Japanese/components.txt");

        mapData(mh4data, mhTranslations, skills, mats);

    }

    private List<String> loadTranslations(String file) {
        List<String> armourData = new ArrayList<String>();
        Scanner armourScanner = new Scanner(getClass().getResourceAsStream(file), "UTF-8");
        while (armourScanner.hasNext()) {
            String x = armourScanner.nextLine();
            if (StringUtils.isNotBlank(x)) {
                armourData.add(x);
            }
        }
        return armourData;
    }

    private Map<String, String> loadMaterials(String engfile, String japFile) {

        Map<String, String> result = new LinkedHashMap<String, String>();
        Scanner japScanner = new Scanner(getClass().getResourceAsStream(japFile), "UTF-8");
        Scanner engScanner = new Scanner(getClass().getResourceAsStream(engfile), "UTF-8");
        while (japScanner.hasNext() && engScanner.hasNext()) {
            String x = japScanner.nextLine().trim();
            String y = engScanner.nextLine().trim();

            if (StringUtils.isNotBlank(x)) {
                if (x.contains(",")) {
                    x = StringUtils.split(x, ",")[0];
                }
                String clean = cleanString(x);
                result.put(clean, y);
            }
        }
        return result;
    }

    private String cleanString(String x) {
        final String bomChar = "\uFEFF";
        StringBuilder clean = new StringBuilder();
        if(x.contains(bomChar)) {
            for (int i = 0; i < x.toCharArray().length; i++) {
                char c = x.toCharArray()[i];
                if(bomChar.toCharArray()[0] != c) {
                    clean.append(c);
                }
            }
        } else {
            clean.append(x);
        }
        return clean.toString();
    }

    private Map<String, String> loadSkills(String file) {
        Map<String, String> skills = new HashMap<String, String>();
        Scanner armourScanner = new Scanner(getClass().getResourceAsStream(file), "UTF-8");

        while (armourScanner.hasNext()) {
            String x = armourScanner.nextLine();
            if (StringUtils.isNotBlank(x)) {
                String[] info = StringUtils.split(x, ":");
                for (int i = 0; i < info.length; i++) {
                    String s = info[i].trim();
                    try {
                        Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        skills.put(info[i + 1].trim(), s);
                    }
                    i++;
                }
            }
        }
        return skills;
    }

    private void mapData(List<Map<String, String[]>> mh4data, List<Map<String, String>> translations, Map<String, String> skills, Map<String, String> mats) {

        Map<String, List<String>> materials = new LinkedHashMap<String, List<String>>();
        Map<String, List<String>> armourData = new LinkedHashMap<String, List<String>>();
        // todo the resistances are wrong, added a g-rank defence which we shouldn't and the has become mixed up with the slot index
        for (int j = 0; j < mh4data.size(); j++) {

            String part = mapPart(j);

            Map<String, String[]> mh4dataEntry = mh4data.get(j);
            Map<String, String> mh4LangTranslation = translations.get(j);

            for (Map.Entry<String, String[]> entry : mh4dataEntry.entrySet()) {

                // japanses name
                String key = entry.getKey();
                String[] info = entry.getValue();
                // see if it currently exists

                List<String> values = new ArrayList<String>();
                String engArmourName;

                engArmourName = mh4LangTranslation.get(key);
                if(engArmourName == null) {
                    System.out.println("****************** null english translation for " + key + " looking in part " + part+" for it ");
                }
                values.add(engArmourName);
                values.add(key);
                values.add(mapWeapon(info[2]));
                values.add(mapGender(info[1]));

                // test rarity what to if it is X

                String rare = info[3];
                int rarity = "X".equals(rare) ? 9 : Integer.parseInt(rare);

                // part
                values.add(part);

                // defense low and high
                if( rarity <= 4 ) {
                    values.add(info[8]);
                }
                else {
                    values.add("---");
                }
                values.add(info[9]);

                // number of slots
                values.add(info[4]);
                // resistences
                values.add(info[10]);
                values.add(info[11]);
                values.add(info[13]);
                values.add(info[14]);
                values.add(info[12]);

                List<String> japMats = new ArrayList<String>();
                List<String> items = materials.get(engArmourName);
                // skills and values contunue at an empty cell and set flag to materials
                boolean doingSkills = true;
                for (int i = 15; i < info.length - 1; i++) {

                    String infoVal = info[i];
                    String engSkill = skills.get(infoVal);
                    if (engSkill == null || StringUtils.isBlank(infoVal)) {
                        doingSkills = false;
                    }
                    if (doingSkills) {
                        String skillVal = info[i + 1];
                        values.add(engSkill);
                        values.add(skillVal);
                        i++;
                    } else {
                        // doing materials separate file
                        String engMats = mats.get(infoVal.trim());
                        if (engMats == null) {
                            engMats = "?????";
                        }
                        if (items == null) {
                            items = new ArrayList<String>();
                            materials.put(engArmourName, items);
                        }
                        String numMats = info[i + 1];
                        engMats += " x" + numMats;

                        items.add(engMats);
                        japMats.add(numMats + " " + infoVal);
                        i++;
                    }
                }
                // add a separator
                items.add("###");
                items.addAll(japMats);
                armourData.put(key, values);
            }
        }
        // sort alphabetically
        // sort the arrays
        ComparatorChain chain = new ComparatorChain();

        chain.addComparator(new Comparator<List<String>>() {
            public int compare(List<String> o1, List<String> o2) {
                String first = StringUtils.split(o1.get(0), " ")[0];
                String second = StringUtils.split(o2.get(0), " ")[0];
                return first.compareTo(second);
            }
        });

        // defense to split gun head and blade head
        chain.addComparator(new Comparator<List<String>>() {
            public int compare(List<String> o1, List<String> o2) {
                Integer a = new Integer(o1.get(7));
                Integer b = new Integer(o2.get(7));
                return b.compareTo(a);
            }
        });

        // compare piece (H, C, A W, L )
        chain.addComparator(new Comparator<List<String>>() {
            public int compare(List<String> o1, List<String> o2) {
                Integer a = pieceMapping.get(o1.get(4));
                Integer b = pieceMapping.get(o2.get(4));
                return a.compareTo(b);
            }
        });

        // G B A
        chain.addComparator(new Comparator<List<String>>() {
            public int compare(List<String> o1, List<String> o2) {
                Integer a = pieceMapping.get(o1.get(2));
                Integer b = pieceMapping.get(o2.get(2));
                return a.compareTo(b);
            }
        });

        List<List<String>> values = new ArrayList<List<String>>(armourData.values());
        Collections.sort(values, chain);

        List<String> lines = new ArrayList<String>();
        for (List<String> strings : values) {
            lines.add(StringUtils.join(strings, DIVIDER));
        }
        // todo now write the result to a file
        writeData(lines, "./mhag/data/mhgen/armourxxxxxx.dat");

        System.out.println("###########################################################");

        List<String> lines2 = new ArrayList<String>();
        // order the materials the same as the values
        for (List<String> value : values) {
            String name = value.get(0);
            List<String> row = materials.get(name);
            lines2.add(name + DIVIDER + StringUtils.join(row, ","));
        }
        writeData(lines2, "./mhag/data/mhgen/armour-matsxxxxxx.dat");
    }

    private String mapPart(int j) {
        switch (j) {
            case 0:
                return "H";
            case 1:
                return "C";
            case 2:
                return "A";
            case 3:
                return "W";
            default:
                return "L";
        }
    }

    private String mapWeapon(String key) {
        if (key.contains("2")) {
            return "G";
        } else if (key.contains("1")) {
            return "B";
        } else {
            return "A";
        }
    }

    private String mapGender(String key) {
        if (key.contains("2")) {
            return "F";
        } else if (key.contains("1")) {
            return "M";
        } else {
            return "A";
        }
    }

    private Map<String, String[]> loadData(String file, String token, int keyIndex) {
        Map<String, String[]> armourData = new LinkedHashMap<String, String[]>();
        Scanner armourScanner = new Scanner(getClass().getResourceAsStream(file), "UTF-8");

        while (armourScanner.hasNext()) {
            String x = armourScanner.nextLine();
            if (StringUtils.isNotBlank(x)) {

                String[] d = StringUtils.split(x, token);
                String key = cleanString(d[keyIndex].trim());

                if (armourData.get(key) != null) {
                    // this will happen when we have male/female need to look at the next index to check which one then concatenate the names m/f i think
                    System.out.println("error duplicate key " + key + " for armor piece " + d[0]);
                    continue;
                }

                if (key.contains("/")) {
                    // split in into 2 entries
                    String[] p = StringUtils.split(key, "/");
                    armourData.put(cleanString(p[0].trim()), d);
                    armourData.put(cleanString(p[1].trim()), d);
                } else {
                    armourData.put(key, d);
                }
            }
        }
        return armourData;
    }

    static Map<String, Integer> pieceMapping = new HashMap<String, Integer>();

    static {
        pieceMapping.put("H", 0);
        pieceMapping.put("C", 1);
        pieceMapping.put("A", 2);
        pieceMapping.put("W", 3);
        pieceMapping.put("L", 4);
        pieceMapping.put("A", 5);
        pieceMapping.put("B", 6);
        pieceMapping.put("G", 7);
    }
}
