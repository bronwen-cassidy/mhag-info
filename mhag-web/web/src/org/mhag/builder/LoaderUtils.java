package org.mhag.builder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * User: Bronwen Cassidy
 * Date: 23/01/14
 * Time: 10:50
 */
public class LoaderUtils {

    public static void main(String[] args) throws Exception {
        new LoaderUtils().loadArmour();
        //System.out.println("##########################################");
        //new LoaderUtils().charms();
    }

    private void charms() {
        Map<String, String> mats = loadMaterials("data/Languages/English/components.txt", "data/Languages/Japanese/components.txt");
        Map<String, String> decorations = loadMaterials("data/Languages/English/decorations.txt", "data/Languages/Japanese/decorations.txt");
        Map<String, String> skills = loadMaterials("data/Languages/English/skills.txt", "data/Languages/Japanese/skills.txt");
        Map<String, String[]> data = loadData("data/decorations.txt", ",", 0);
        writeJewelsAndMats(data, decorations, mats, skills);
    }

    private void writeJewelsAndMats(Map<String, String[]> data, Map<String, String> jewelTranslations, Map<String, String> matTranslation, Map<String, String> skillTranslation) {
        Map<String, List<String>> jewelMats = new LinkedHashMap<String, List<String>>();
        Map<String, List<String>> jewelData = new LinkedHashMap<String, List<String>>();
        int index = 0;
        for (Map.Entry<String, String[]> entry : data.entrySet()) {
            String key = entry.getKey();
            String[] info = entry.getValue();

            String engTranslation = jewelTranslations.get(key);
            if(engTranslation == null) {
                System.out.println("************* jap name: " + key + " index: " + index + "  ******************");
                continue;
            }
            // materials data structure
            List<String> matsRow = jewelMats.get(key);
            if(matsRow == null) {
                matsRow = new ArrayList<String>();
                jewelMats.put(key, matsRow);
            }

            // jewel info
            if(jewelData.containsKey(key)) {
                System.out.println("error duplicate key found for " + key + " eng translation " + engTranslation);
            }
            List<String> row = jewelData.get(key);
            if(row == null) {
                row = new ArrayList<String>();
                jewelData.put(key, row);
            }
            row.add(engTranslation);
            matsRow.add(engTranslation);
            row.add(key);
            // high rank low rank
            String rank = Integer.parseInt(info[3].trim()) < 5 ? "L" : "H";
            row.add(rank);
            // slots
            row.add(info[2].trim());
            List<String> japMats = new ArrayList<String>();
            // skill and points
            for (int i = 5; i < info.length; i++) {
                String name = info[i];
                // find the english name in the skill translations
                if(skillTranslation.containsKey(name)) {
                    // doing the skills
                    String engSkill = skillTranslation.get(name);
                    String val = info[i+1];
                    row.add(engSkill);
                    row.add(val);
                    i++;
                } else {
                    // doing the materials
                    String engMat = matTranslation.get(name);
                    String val = info[i+1];
                    matsRow.add(engMat + " x" + val);
                    japMats.add(name + " x" + val);
                    i++;
                }
            }
            matsRow.addAll(japMats);
            index++;
        }

        for (List<String> inf : jewelData.values()) {
            String s = StringUtils.collectionToDelimitedString(inf, " : ");
            System.out.println(s);
        }
        System.out.println("###########################################################");
        for (List<String> inf : jewelMats.values()) {
            String s = StringUtils.collectionToDelimitedString(inf, " : ");
            System.out.println(s);
        }
    }

    public void loadArmour() throws Exception {
        // first read in armour.dat
        // Map<String, String[]> armourData = loadData("data/mh4/armor.dat", ":", 1);

        List<Map<String, String[]>> mh4data = new ArrayList<Map<String, String[]>>(5);
        mh4data.add(loadData("data/head.txt", ",", 0));
        mh4data.add(loadData("data/body.txt", ",", 0));
        mh4data.add(loadData("data/arms.txt", ",", 0));
        mh4data.add(loadData("data/waist.txt", ",", 0));
        mh4data.add(loadData("data/legs.txt", ",", 0));

        // jap to english armour translation just english mapped by index
        List<String> engTranslations = loadTranslations("data/Languages/English/all.txt");

        Map<String, String> skills = loadSkills("data/mh4/skill.dat");
        Map<String, String> mats = loadMaterials("data/Languages/English/components.txt", "data/components.txt");

        mapData(mh4data, engTranslations, skills, mats);

    }

    private List<String> loadTranslations(String file) {
        List<String> armourData = new ArrayList<String>();
        Scanner armourScanner = new Scanner(getClass().getResourceAsStream(file), "UTF-8");
        while (armourScanner.hasNext()) {
            String x = armourScanner.nextLine();
            if (StringUtils.hasText(x)) {
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
            if (StringUtils.hasText(x)) {
                if(x.contains(",")) {
                    x = StringUtils.tokenizeToStringArray(x, ",")[0];
                }
                result.put(x, y);
            }
        }
        return result;
    }

    private Map<String, String> loadSkills(String file) {
        Map<String, String> skills = new HashMap<String, String>();
        Scanner armourScanner = new Scanner(getClass().getResourceAsStream(file), "UTF-8");

        while (armourScanner.hasNext()) {
            String x = armourScanner.nextLine();
            if (StringUtils.hasText(x)) {
                String[] info = StringUtils.tokenizeToStringArray(x, ":");
                for (int i = 0; i < info.length; i++) {
                    String s = info[i].trim();
                    if (!NumberUtils.isDigits(s)) {
                        skills.put(info[i + 1].trim(), s);
                        i++;
                    }
                }
            }
        }
        return skills;
    }

    private void mapData(List<Map<String, String[]>> mh4data, List<String> translations, Map<String, String> skills, Map<String, String> mats) {

        Map<String, List<String>> materials = new LinkedHashMap<String, List<String>>();
        Map<String, List<String>> armourData = new LinkedHashMap<String, List<String>>();

        int translationIndex = 0;

        for (int j = 0; j < mh4data.size(); j++) {

            String part = mapPart(j);

            Map<String, String[]> mh4dataEntry = mh4data.get(j);

            for (Map.Entry<String, String[]> entry : mh4dataEntry.entrySet()) {

                String key = entry.getKey();
                String[] info = entry.getValue();
                // see if it currently exists

                List<String> values = new ArrayList<String>();
                String engArmourName;

                engArmourName = translations.get(translationIndex);
                values.add(engArmourName);
                values.add(key);
                values.add(mapWeapon(info[2]));
                values.add(mapGender(info[1]));
                // part
                values.add(part);
                // defense low and high
                values.add(info[7]);
                values.add(info[8]);
                // number of slots
                values.add(info[4]);
                // resistences
                values.add(info[9]);
                values.add(info[10]);
                values.add(info[12]);
                values.add(info[13]);
                values.add(info[11]);

                List<String> japMats = new ArrayList<String>();
                List<String> items = materials.get(engArmourName);
                // skills and values contunue at an empty cell and set flag to materials
                boolean doingSkills = true;
                for (int i = 14; i < info.length; i++) {

                    String infoVal = info[i];
                    String engSkill = skills.get(infoVal);
                    if (engSkill == null) {
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
                        if(engMats == null) {
                            engMats = "?????";
                        }
                        if(items == null) {
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
                translationIndex++;
            }
        }
        // sort alphabetically
        // sort the arrays
        ComparatorChain chain = new ComparatorChain();

        chain.addComparator(new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                String first = StringUtils.delimitedListToStringArray(o1.get(0), " ")[0];
                String second = StringUtils.delimitedListToStringArray(o2.get(0), " ")[0];
                return first.compareTo(second);
            }
        });

        // defense to split gun head and blade head
        chain.addComparator(new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                Integer a = new Integer(o1.get(5));
                Integer b = new Integer(o2.get(5));
                return b.compareTo(a);
            }
        });

        // compare piece (H, C, A W, L )
        chain.addComparator(new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                Integer a = pieceMapping.get(o1.get(4));
                Integer b = pieceMapping.get(o1.get(4));
                return a.compareTo(b);
            }
        });

        // G B A
        chain.addComparator(new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                Integer a = pieceMapping.get(o1.get(2));
                Integer b = pieceMapping.get(o2.get(2));
                return a.compareTo(b);
            }
        });

        List<List<String>> values = new ArrayList<List<String>> (armourData.values());
        Collections.sort(values, chain);

        for (List<String> strings : values) {
            String s = StringUtils.collectionToDelimitedString(strings, " : ");
            System.out.println(s);
        }

        System.out.println("#############################################################");
        // order the materials the same as the values
        for (List<String> value : values) {
            String name = value.get(0);
            List<String> row = materials.get(name);
            System.out.println(name + " : " + StringUtils.collectionToCommaDelimitedString(row));
        }
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
            if (StringUtils.hasText(x)) {

                String[] d = StringUtils.tokenizeToStringArray(x, token);
                String key = d[keyIndex].trim();

                if (armourData.get(key) != null) {
                    System.out.println("error duplicate key " + key + " for armor piece " + d[0]);
                    System.exit(1);
                }

                if (key.contains("/")) {
                    // split in into 2 entries
                    String[] p = StringUtils.tokenizeToStringArray(key, "/");
                    armourData.put(p[0].trim(), d);
                    armourData.put(p[1].trim(), d);
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
