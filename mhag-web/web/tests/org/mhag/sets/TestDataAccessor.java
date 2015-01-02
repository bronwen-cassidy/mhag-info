/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Oct-2011 14:29:35
 */
public class TestDataAccessor {

    @BeforeClass
    public static void once() {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUser("mhagadmin");
        dataSource.setPassword("mhagadmin");
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("mhag");
        dataAccessor = new DataAccessor();
        dataAccessor.setDataSource(dataSource);
    }

    @Test
    public void testSearchSetsAllResults() throws Exception {
        List<SavedSet> sets = new ArrayList<SavedSet>(dataAccessor.searchSets(new HashMap<String, String>(), new String[0], "id", "asc"));
        Collections.sort(sets);
        assertTrue(sets.size() > 0);
        final SavedSet savedSet = sets.get(0);
        // confirm the state of the saved set
        assertTrue(!savedSet.getSkills().isEmpty());
    }

    @Test
    public void testSearchSetsOwner() throws Exception {
        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("owner", "Tifa");
        List<SavedSet> sets = new ArrayList<SavedSet>(dataAccessor.searchSets(parameters, new String[0], "id", "asc"));
        Collections.sort(sets);
        assertFalse(sets.isEmpty());
    }

    @Test
    public void testSearchSetsSkillMatch() throws Exception {
        final HashMap<String, String> parameters = new HashMap<String, String>();
        final String[] skills = new String[] {"Sharpness +1"};

        List<SavedSet> sets = new ArrayList<SavedSet>(dataAccessor.searchSets(parameters, skills, "id", "asc"));
        Collections.sort(sets);
        assertFalse(sets.isEmpty());
        final SavedSet savedSet = sets.get(0);
        Set<SetSkill> setSkills = savedSet.getSkills();
        assertTrue(setSkills.contains(new SetSkill(skills[0])));
    }

    @Test
    public void testSearchSetsSkillMatchMany() throws Exception {
        final HashMap<String, String> parameters = new HashMap<String, String>();
        final String[] skills = new String[] {"Sharpness +1", "Evasion +2"};

        List<SavedSet> sets = new ArrayList<SavedSet>(dataAccessor.searchSets(parameters, skills, "id", "asc"));
        Collections.sort(sets);
        assertFalse(sets.isEmpty());
        final SavedSet savedSet = sets.get(0);
        Set<SetSkill> setSkills = savedSet.getSkills();
        assertTrue(setSkills.contains(new SetSkill(skills[0])) || setSkills.contains(new SetSkill(skills[1])));
    }

    @Test
    public void testAddLogic() throws Exception {
        Map<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("owner", "Fred");
        parameters.put("label", "Testing");
        StringBuilder sqlBuf = new StringBuilder();

        int index = 1;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sqlBuf.append("s.").append(entry.getKey()).append(" like '%").append(entry.getValue()).append("%' ");
            if (parameters.size() > index++) sqlBuf.append("and ");
        }

        assertEquals("s.owner like '%Fred%' and s.label like '%Testing%' ", sqlBuf.toString());
    }

    @Test
    public void testCodeExistsNo() throws Exception {
        String code = "Darasu's%20GS/Hammer%20Set%20:%20H.B.H.69.1.121.C.87.1.120.A.68.1.120.W.76.2.120.120.L.2" +
                "0.1.120.X.2.120.111.Y.0.1.10.5.0";
        assertFalse(dataAccessor.setExists(code));
    }

    @Test
    public void testCodeExistsYes() throws Exception {
        String code = "SomeRandomNameIdontCareAbount:%20H.B.H.69.1.121.C.87.1.120.A.68.1.120.W.76.2.120.120.L.2" +
                "0.1.120.X.2.120.111.Y.0.1.13.6.0";
        assertTrue(dataAccessor.setExists(code));
    }

    @Test
    public void testCodeExistsYesBadName() throws Exception {
        String code = "SomeRandom:Name:With:colons:%20H.B.H.69.1.121.C.87.1.120.A.68.1.120.W.76.2.120.120.L.2" +
                "0.1.120.X.2.120.111.Y.0.1.13.6.0";
        assertTrue(dataAccessor.setExists(code));
    }

    @Test
    public void testSkillSearchAll() throws Exception {
        dataAccessor.setVersion(0);
        Map<String, String> params = new HashMap<String, String>();
        params.put("rank", "1");
        List<ArmourPiece> reslts = dataAccessor.searchSkills(params, "Hearing", ">", "2");
        assertEquals(6, reslts.size());
    }

    @Test
    public void testSkillSearchPiece() throws Exception {
        dataAccessor.setVersion(0);
        Map<String, String> params = new HashMap<String, String>();
        params.put("rank", "0");
        params.put("armour_piece", "3");
        List<ArmourPiece> reslts = dataAccessor.searchSkills(params, "Hearing", ">", "2");
        assertEquals(4, reslts.size());
    }

    private static DataAccessor dataAccessor;
}
