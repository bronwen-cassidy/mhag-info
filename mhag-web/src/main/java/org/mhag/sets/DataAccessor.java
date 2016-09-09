/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Sep-2011 08:18:13
 */
public class DataAccessor {

    public Long registerUser(String username, String password) {

        // check we do not have a duplicate user
        int numUsers = jdbcTemplate.queryForInt("select count(*) from users where username = ?", username);

        if (numUsers > 0) throw new UserExistsException("Cannot add as a user with the same username already exists");

        String[] params = {username, password};

        final String sql = "insert into users (username, password) values (?, ?)";
        SqlParameter[] parameters = new SqlParameter[]{new SqlParameter(Types.VARCHAR),
                new SqlParameter(Types.VARCHAR)};

        Number key = requestKey(params, sql, parameters);

        return key.longValue();
    }

    public Long loginUser(String username, String password) {
        try {
            return jdbcTemplate.queryForLong("select id from users where username = ? and password = ? ", username, password);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public SavedSet findById(Long id) {
        final List<SavedSet> result = jdbcTemplate.query("select * from saved_sets where id = ?", new Object[]{id}, new RowMapper<SavedSet>() {

            public SavedSet mapRow(ResultSet rs, int rowNum) throws SQLException {
                return extractSavedSet(rs);
            }
        });
        return result.isEmpty() ? null : result.get(0);
    }

    public boolean setExists(String code, int gameVersion) {
        String trimmedCode = code;
        if (code.contains(":")) {
            String name = code.substring(0, code.lastIndexOf(":"));
            trimmedCode = code.substring(name.length() + 4);
        }
        String sql = "select count(*) from saved_sets where mh_version=" + gameVersion + " and code like '%" + trimmedCode + "%'";
        int count = jdbcTemplate.queryForInt(sql);
        return count > 0;
    }

    public Long saveArmourSet(SavedSet armourSet, int gameVersion) throws PersistenceException {

        Set<SetSkill> setSkillSet = armourSet.getSkills();
        if(setSkillSet.size() < 2) throw new NotEnoughSkillsException("You must have at least 2 skills to save the set");
        String setCode = armourSet.getSetCode();
        if(setExists(setCode, gameVersion)) throw new SetExistsException("An identical set already exists in the database");
        Object[] params;
        String sql;
        SqlParameter[] parameters;

        if (armourSet.getUserId() == null) {

            params = new Object[]{armourSet.getName(), setCode, armourSet.getOwner(),
                    armourSet.getNumWeaponSlots(), armourSet.getIpAddress(), version};
            sql = "insert into saved_sets (label, code, owner, num_weapon_slots, ip_address, mh_version) values (?, ?, ?, ?, ?, ?)";
            parameters = new SqlParameter[]{new SqlParameter(Types.VARCHAR),
                    new SqlParameter(Types.VARCHAR), new SqlParameter(Types.VARCHAR),
                    new SqlParameter(Types.INTEGER), new SqlParameter(Types.VARCHAR),
                    new SqlParameter(Types.INTEGER)};
        } else {

            params = new Object[]{armourSet.getName(), setCode, armourSet.getOwner(),
                    armourSet.getNumWeaponSlots(), armourSet.getIpAddress(), version, armourSet.getUserId()};
            sql = "insert into saved_sets (label, code, owner, num_weapon_slots, ip_address, mh_version, user_id) values (?, ?, ?, ?, ?, ?, ?)";
            parameters = new SqlParameter[]{new SqlParameter(Types.VARCHAR),
                    new SqlParameter(Types.VARCHAR), new SqlParameter(Types.VARCHAR),
                    new SqlParameter(Types.INTEGER), new SqlParameter(Types.VARCHAR),
                    new SqlParameter(Types.INTEGER), new SqlParameter(Types.INTEGER)};
        }

        Number key = requestKey(params, sql, parameters);

        Long setId = key.longValue();


        for (SetSkill setSkill : setSkillSet) {
            jdbcTemplate.update("insert into set_skills (label, saved_set_id) values (?, ?)", setSkill.getName(), setId);
        }
        return setId;
    }

    public void updateArmourSet(SavedSet armourSet) throws PersistenceException {

        Set<SetSkill> setSkillSet = armourSet.getSkills();
        if(setSkillSet.size() < 2) throw new NotEnoughSkillsException("Need at least 2 skills to save a set");

        Long currentId = armourSet.getId();
        SqlUpdate su = new SqlUpdate();
        su.setJdbcTemplate(jdbcTemplate);

        su.setSql("update SAVED_SETS set label = ?, code = ?, num_weapon_slots = ? where id = " + currentId);

        su.declareParameter(new SqlParameter(Types.VARCHAR));
        su.declareParameter(new SqlParameter(Types.VARCHAR));
        su.declareParameter(new SqlParameter(Types.INTEGER));

        su.compile();
        su.update(armourSet.getName(), armourSet.getSetCode(), armourSet.getNumWeaponSlots());

        jdbcTemplate.update("delete from SET_SKILLS where saved_set_id=" + currentId);

        for (SetSkill setSkill : setSkillSet) {
            jdbcTemplate.update("insert into set_skills (label, saved_set_id) values (?, ?)", setSkill.getName(), currentId);
        }
    }

    public void delete(Long setId) {
        jdbcTemplate.execute("delete from set_skills where saved_set_id = " + setId);
        jdbcTemplate.execute("delete from saved_sets where id = " + setId);
    }

    public void deleteAll(Long[] ids) {
        jdbcTemplate.execute("delete from set_skills where saved_set_id in (" + StringUtils.arrayToCommaDelimitedString(ids) + ")");
        jdbcTemplate.execute("delete from saved_sets where id in (" + StringUtils.arrayToCommaDelimitedString(ids) + ")");
    }

    public Page<SavedSet> searchSets(Map<String, String> parameters, String[] skills, int start, int end, String orderBy, String direction) {

        StringBuilder sqlBuf = buildSearchQuery(parameters, skills, orderBy, direction);
        Set<SavedSet> result = doSearch(sqlBuf);

        List<SavedSet> sorted = new ArrayList<SavedSet>(result);
        List<SavedSet> savedSets;
        if(direction == null) direction = "desc";

        int toIndex = start + end;
        if(toIndex > sorted.size()) toIndex = sorted.size();

        if(orderBy == null) {
            assignSkills(skills, result);
            if ("desc".equals(direction)) {
                Collections.sort(sorted, new Comparator<SavedSet>() {
                    public int compare(SavedSet o1, SavedSet o2) {
                        return o2.compareTo(o1);
                    }
                });
            } else {
                Collections.sort(sorted);
            }
            savedSets = sorted.subList(start, toIndex);
        } else {
            savedSets = sorted.subList(start, toIndex);
            assignSkills(skills, new LinkedHashSet<SavedSet>(savedSets));
        }
        
        Page<SavedSet> page = new Page<SavedSet>();
        page.setData(new LinkedHashSet<SavedSet>(savedSets));
        page.setTotalResults(result.size());
        page.setFirst(start);
        return page;
    }

    public List<SavedSet> searchSets(Map<String, String> parameters, String[] skills, String orderBy, String direction) {
        StringBuilder sqlBuf = buildSearchQuery(parameters, skills, orderBy, direction);
        Set<SavedSet> results = doSearch(sqlBuf);
        assignSkills(skills, results);

        return new ArrayList<SavedSet>(results);
    }

    public int[] addVote(Long setId, Long userId, String ipAddress) {

        int hasVotedUp = countNumVotes(setId, userId, ipAddress, YES);
        int hasVotedDown = countNumVotes(setId, userId, ipAddress, NO);

        SavedSet s = findById(setId);
        int downVotes = s.getNumDownVotes();
        int upVotes = s.getNumUpVotes();

        if(hasVotedUp <= 0) {

            upVotes += 1;
            if(hasVotedDown > 0) {
                downVotes -= 1;
                jdbcTemplate.update("update saved_sets set num_down_votes = " + downVotes + " where id=" + setId);
                removeUserVote(userId, NO, setId, ipAddress);
            }
            jdbcTemplate.update("update saved_sets set num_up_votes = " + upVotes + " where id=" + setId);
            addUserVote(setId, userId, ipAddress, YES);
        }

        return new int[] {upVotes, downVotes};
    }

    public int[] removeVote(Long setId, Long userId, String ipAddress) {
        
        SavedSet s = findById(setId);

        int hasVotedDown = countNumVotes(setId, userId, ipAddress, NO);
        int hasVotedUp = countNumVotes(setId, userId, ipAddress, YES);

        int downVotes = s.getNumDownVotes();
        int upVotes = s.getNumUpVotes();
        
        if (hasVotedDown <= 0) {
            downVotes += 1;
            
            if(hasVotedUp > 0) {
                upVotes -= 1;
                jdbcTemplate.update("update saved_sets set num_up_votes = " + upVotes + " where id=" + setId);
                removeUserVote(userId, YES, setId, ipAddress);
            }
            jdbcTemplate.update("update saved_sets set num_down_votes = " + downVotes + " where id=" + setId);
            addUserVote(setId, userId, ipAddress, NO);
            //downVotes = newVotes;
        }
        return new int[] {upVotes, downVotes};
    }

    //select armour_name from armour_skills a where skill_name='Gathering' and armour_name in (select armour_name from armour_skills b where skill_name='Whim');
    public List<ArmourPiece> searchSkills(Map<String, String> parameters, String skill, String operator, String value) {
        StringBuilder sql = new StringBuilder("select * from armour_skills where mh_version=? and skill_name like ? and skill_value ");
        sql.append(operator).append(" ? ");
        Object[] values = new Object[parameters.size() + 3];
        if(parameters.get(ArmourPiece.BLADE_OR_GUNNER) != null) {
            values = new Object[parameters.size() + 4];
        }
        values[0] = version;
        values[1] = "%" + skill + "%";
        values[2] = value;

        int i = 3;
        for (Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry =  iterator.next();
            boolean blade = ArmourPiece.BLADE_OR_GUNNER.equals(entry.getKey());
            sql.append(" and ");
            if(blade)  sql.append("( ");

            sql.append(entry.getKey()).append(" = ? ");
            values[i++] = entry.getValue();
            if(blade) {
                sql.append(" or blade_or_gunner = ? ) ");
                values[i++] = ArmourPiece.BLADE_AND_GUNNER;
            }
        }
 
        sql.append(" order by skill_value, armour_piece, armour_name");
        return jdbcTemplate.query(sql.toString(), values, new RowMapper<ArmourPiece>() {
            public ArmourPiece mapRow(ResultSet rs, int i) throws SQLException {
                return new ArmourPiece(rs.getInt("id"), rs.getString("armour_name"), rs.getInt("armour_piece"),
                        rs.getString("skill_name"), rs.getInt("skill_value"), rs.getInt("rank"), rs.getInt("num_slots"),
                        rs.getString("blade_or_gunner"), rs.getInt("armour_id"), rs.getString("gender"));
            }
        });
    }

    private void addUserVote(Long setId, Long userId, String ipAddress, String action) {
        jdbcTemplate.update("insert into user_votes (IP_ADDRESS, ACTION, USER_ID, SET_ID) values (?,?,?,?)",
                ipAddress, action, userId, setId);
    }

    private void removeUserVote(Long userId, String action, Long setId, String ipAddress) {
        String whereClause = "IP_ADDRESS = ?";
        if(userId != null) {
            whereClause += " and user_id = " + userId;
        }
        String insertSql = "delete from user_votes where " + whereClause + " and SET_ID = ? and action=?";
        jdbcTemplate.update(insertSql, ipAddress, setId, action);

    }

    private Set<SavedSet> doSearch(StringBuilder sqlBuf) {
    	try {
    		return new LinkedHashSet<SavedSet>(jdbcTemplate.query(sqlBuf.toString(), new RowMapper<SavedSet>() {
    			
    			public SavedSet mapRow(ResultSet rs, int rowNum) throws SQLException {
    				return extractSavedSet(rs);
    			}
    		}));
    	} catch (DataAccessException e) {
    		LOG.error("Couldn't properly execute the following query (probably due to an exception in the RowMapper): \n"+sqlBuf.toString(), e);
    		// FIXME we should really agree on a stronger contract between methods, possibly using non-runtime exceptions
    		throw e;
    	}
    }

    private void assignSkills(String[] skills, Set<SavedSet> result) {
        for (SavedSet set : result) {
            final Long setId = set.getId();
            List<SetSkill> setSkills = jdbcTemplate.query("select * from set_skills where saved_set_id=" + setId + " order by label ", new RowMapper<SetSkill>() {

                public SetSkill mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SetSkill ss = new SetSkill(rs.getString("label"));
                    ss.setId(rs.getLong("id"));
                    ss.setSavedSetId(setId);
                    return ss;
                }
            });
            set.setSkills(new LinkedHashSet<SetSkill>(setSkills));
            set.calculateWeighting(Arrays.asList(skills));
        }
    }

    private StringBuilder buildSearchQuery(Map<String, String> parameters, String[] skills, String orderBy, String direction) {
        StringBuilder sqlBuf = new StringBuilder("select s.* from saved_sets s ");

        if (skills.length > 0) sqlBuf.append(", set_skills ss ");

        sqlBuf.append("where mh_version = ").append(version);
        sqlBuf.append(parameters.isEmpty() ? " " : " and ");
        int index = 1;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sqlBuf.append("s.").append(entry.getKey()).append(" like '%").append(escape(entry.getValue())).append("%' ");
            if (parameters.size() > index++) sqlBuf.append(" and ");
        }

        if (skills.length > 0) {
            sqlBuf.append(" and ( ");
        }

        for (int i = 0; i < skills.length; i++) {
            String skill = skills[i];
            sqlBuf.append("(ss.label = '").append(escape(skill)).append("') ");
            if (i < (skills.length - 1)) sqlBuf.append("or ");
        }
        
        if (skills.length > 0) {
            sqlBuf.append(") and ss.saved_set_id = s.id ");
        }

        if (orderBy != null) {
            if (orderBy.equalsIgnoreCase("num_up_votes")) {
                sqlBuf.append("order by s.").append(orderBy).append(" ").append(direction).append(" ");
            } else {
                sqlBuf.append("order by upper(s.").append(orderBy).append(") ").append(direction).append(" ");
            }
        }
        return sqlBuf;
    }
    
    private SavedSet extractSavedSet(ResultSet rs) throws SQLException {
        SavedSet savedSet = new SavedSet(rs.getString("label"), rs.getString("code"), rs.getString("owner"));
        savedSet.setNumWeaponSlots(rs.getInt("num_weapon_slots"));
        savedSet.setIpAddress(rs.getString("ip_address"));
        savedSet.setId(rs.getLong("id"));
        savedSet.setUserId(rs.getLong("user_id"));
        savedSet.setNumUpVotes(rs.getInt("num_up_votes"));
        savedSet.setNumDownVotes(rs.getInt("num_down_votes"));
        return savedSet;
    }
    
    private Number requestKey(Object[] params, String sql, SqlParameter[] parameters) {
        SqlUpdate su = new SqlUpdate();
        su.setJdbcTemplate(jdbcTemplate);

        su.setSql(sql);

        for (SqlParameter parameter : parameters) {
            su.declareParameter(parameter);
        }
        su.setReturnGeneratedKeys(true);
        su.compile();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        su.update(params, keyHolder);
        return keyHolder.getKey();
    }
    
    private String escape(String input) {
        if (input == null) {
            return input;
        }

        StringBuilder filtered = new StringBuilder(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (c == '\'') {
                filtered.append("\'\'");
            } else {
                filtered.append(c);
            }
        }
        return filtered.toString();
    }
    
    private int countNumVotes(Long setId, Long userId, String ipAddress, String action) {
        String sql = "select count(*) from user_votes where (user_id=? or ip_address=?) and action = ? and " +
                "set_id=? ";
        Object[] args = {userId, ipAddress, action, setId};
        int[] argTypes = {Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.BIGINT};
        return jdbcTemplate.queryForInt(sql, args, argTypes);
    }
    
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void setVersion(int value) {
        this.version = value;
    }

    

    public static final String YES = "YES";
    public static final String NO = "NO";

    private JdbcTemplate jdbcTemplate;
    private int version;
    
    private static final Log LOG = LogFactory.getLog(DataAccessor.class);
}
