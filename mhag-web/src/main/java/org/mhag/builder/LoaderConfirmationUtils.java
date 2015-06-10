package org.mhag.builder;

import org.springframework.util.StringUtils;

import java.util.*;

/**
 * User: Bronwen Cassidy
 * Date: 23/01/14
 * Time: 10:50
 */
public class LoaderConfirmationUtils {

    public static void main(String[] args) throws Exception {
        new LoaderConfirmationUtils().confirmSkillsExist();
        //System.out.println("##########################################");
        //new LoaderUtils().charms();
    }

    public void confirmSkillsExist() throws Exception {
        // first read in armour.dat
        // Map<String, String[]> armourData = loadData("data/mh4/armor.dat", ":", 1);

        // we need to confirm that we have data in all the files otherwise we end up mapping poison :-(
        final Map<String, String[]> armour = loadData("/data/mh4/armor.dat", ":", 0);

        final Map<String, String[]> armourItems = loadData("/data/mh4/armor_item.dat", ":", 0);

        final Map<String, String[]> skills = loadData("/data/mh4/skill.dat", ":", 0);

        final Map<String, String[]> jewel = loadData("/data/mh4/jewel.dat", ":", 0);
        final Map<String, String[]> jewelMats = loadData("/data/mh4/jewel_item.dat", ":", 0);

        // armours and and armour materials (check armours are present)
        // jewel and jewel items

        mapData(armour, armourItems, skills, 14);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        mapData(jewel, jewelMats, skills, 4);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

    }

    private void mapData(Map<String, String[]> core, Map<String, String[]> match, Map<String, String[]> secondMatch, int startIndex) {

        for (Map.Entry<String, String[]> entry : core.entrySet()) {
            String[] parts = entry.getValue();
            for (int i = startIndex; i <= parts.length-2; i+=2) {
                String part = parts[i];
                if(secondMatch.get(part.trim()) == null) {
                    System.out.println("************** " + part + " ************** missing skill");
                }
            }

            if(match.get(entry.getKey().trim()) == null) {
                System.out.println("################# " + entry.getKey() + " ################ missing materials");    
            }
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
                armourData.put(key, d);
            }
        }
        return armourData;
    }
}
