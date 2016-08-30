package org.mhag.builder;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.mhag.ArmorWrapper;
import org.mhag.MhagFacade;
import org.mhag.model.Armor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bronwen.
 * Date: 05/03/12
 * Time: 14:46
 */
public class DatabaseUtils {

    public static void main(String[] args) throws Exception {
        MysqlConnectionPoolDataSource dataSource = buildDataSource();
        JdbcTemplate template = new JdbcTemplate(dataSource);

        // for each version 0 = tri, 1 = mh3rd, 3 = triG & triU, MH4 = 4, MHGen = 5
        int version = 5;
        MhagFacade mhagFacade = getFacadeForVersion(version);
        Set<ArmorWrapper> all = getArmours(mhagFacade);
        insertData(template, all, version);

    }

    private static void insertData(JdbcTemplate template, Set<ArmorWrapper> all, int version) {
        for (ArmorWrapper wrapper : all) {
            Armor armor = wrapper.getArmor();
            String[] skillNames = armor.getSkillName();
            int[] points = armor.getSkillPoint();
            for (int i = 0; i < armor.getNumSkill(); i++) {
                template.update("insert into armour_skills (armour_name, armour_piece, skill_name, skill_value, mh_version, " +
                        "rank, blade_or_gunner, num_slots, gender, armour_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        armor.getArmorName(), armor.getBodyPart(), skillNames[i], points[i], version, armor.getRank(),
                        armor.getBladeOrGunner(), armor.getNumSlot(), armor.getGender(), armor.getArmorID());
            }

        }
    }

    private static Set<ArmorWrapper> getArmours(MhagFacade mhagFacade) {
        Set<ArmorWrapper> all = new HashSet<ArmorWrapper>();
        all.addAll(mhagFacade.getAllArmours(0));
        all.addAll(mhagFacade.getAllArmours(1));
        all.addAll(mhagFacade.getAllArmours(2));
        all.addAll(mhagFacade.getAllArmours(3));
        all.addAll(mhagFacade.getAllArmours(4));
        return all;
    }

    private static MhagFacade getFacadeForVersion(int version) throws Exception {
        MhagFacade mhagFacade = new MhagFacade();
        mhagFacade.setVersion(version);
        mhagFacade.afterPropertiesSet();
        return mhagFacade;
    }

    private static MysqlConnectionPoolDataSource buildDataSource() {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUser("mhagadmin");
        dataSource.setPassword("mhagadmin");
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("mhag");
        return dataSource;
    }
}
