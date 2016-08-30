/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.mhag.sets.Rank;
import org.mhag.sets.SavedSet;

import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Oct-2011 10:28:17
 */
public class MhagUtils {

    public static Map<SkillKey, Map<String, Cell>> sortResults(Map<SkillKey, Map<String, Cell>> result) {

        Set<Map.Entry<SkillKey, Map<String, Cell>>> entries = result.entrySet();
        List<Map.Entry<SkillKey, Map<String, Cell>>> list = new LinkedList<Map.Entry<SkillKey, Map<String, Cell>>>(entries);

        //sort list based on comparator
        Collections.sort(list, new Comparator<Map.Entry<SkillKey, Map<String, Cell>>>() {
            public int compare(Map.Entry<SkillKey, Map<String, Cell>> o1, Map.Entry<SkillKey, Map<String, Cell>> o2) {
                Cell cellA = o1.getValue().get(MhagFacade.TOTAL);
                Cell cellB = o2.getValue().get(MhagFacade.TOTAL);
                return cellB.compareTo(cellA);
            }
        });

        //put sorted list into map again
        Map<SkillKey, Map<String, Cell>> sortedMap = new LinkedHashMap<SkillKey, Map<String, Cell>>();
        for (Map.Entry<SkillKey, Map<String, Cell>> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static List<SavedSet> findDuplicateSkills(final List<SavedSet> input) {
        List<SavedSet> results = new ArrayList<SavedSet>();
        CollectionUtils.select(input, new Predicate() {
            public boolean evaluate(Object object) {
                SavedSet savedSet = (SavedSet) object;
                final String skillsDisplayValue = savedSet.getSkillsDisplayValue();
                for (SavedSet s : input) {
                    if(s.getId().equals(savedSet.getId())) continue;
                    if(s.getSkillsDisplayValue().equals(skillsDisplayValue)) return true;
                }
                return false;
            }
        }, results);

        Collections.sort(results, new Comparator<SavedSet>() {
            public int compare(SavedSet o1, SavedSet o2) {
                return o1.getSkillsDisplayValue().compareTo(o2.getSkillsDisplayValue());
            }
        });

        return results;
    }

    public static Long[] convertToLong(long[] ids) {
        Long[] result = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            result[i] = ids[i];
        }
        return result;  //To change body of created methods use File | Settings | File Templates.
    }
    
    public static Rank[] getRankValues(int gameVersion) {
        return rankValues.get(gameVersion);
    }

    private static Map<Integer, Rank[]> rankValues = new HashMap<Integer, Rank[]>();

    static {
        rankValues.put(0, new Rank[] {new Rank(1, "High Rank"), new Rank(0, "Low Rank")});
        rankValues.put(1, new Rank[] {new Rank(1, "High Rank"), new Rank(0, "Low Rank")});
        rankValues.put(3, new Rank[] {new Rank(2, "G Rank"), new Rank(1, "High Rank"), new Rank(0, "Low Rank")});
        rankValues.put(4, new Rank[] {new Rank(2, "G Rank"), new Rank(1, "High Rank"), new Rank(0, "Low Rank")});
        rankValues.put(5, new Rank[] {new Rank(1, "High Rank"), new Rank(0, "Low Rank")});
    }
    
}
