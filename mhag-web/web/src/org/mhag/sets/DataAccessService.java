/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Sep-2011 08:19:08
 */
public class DataAccessService implements Serviceable {

    public Long saveArmourSet(SavedSet armourSet) throws PersistenceException {
        return dataAccessor.saveArmourSet(armourSet);
    }

    public void updateArmourSet(SavedSet armourSet) throws PersistenceException {
        dataAccessor.updateArmourSet(armourSet);
    }

    public void setDataAccessor(DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public Page<SavedSet> searchSets(Map<String, String> parameters, String[] skills, int start, int end, String orderBy, String direction) {
        return dataAccessor.searchSets(parameters, skills, start, end, orderBy, direction);
    }

    @Override
    public List<SavedSet> searchSets(Map<String, String> parameters, String[] skills, String orderBy, String direction) {
        return dataAccessor.searchSets(parameters, skills, orderBy, direction);
    }

    public List<ArmourPiece> searchArmourPieces(Map<String, String> parameters, String skillName, String opertor, String value) {
        return dataAccessor.searchSkills(parameters, skillName, opertor, value);
    }

    public void delete(Long id) {
        dataAccessor.delete(id);
    }

    public void deleteAll(Long[] ids) {
        dataAccessor.deleteAll(ids);
    }

    public Long registerUser(String username, String password) {
        return dataAccessor.registerUser(username, password);
    }

    public Long loginUser(String username, String password) {
        return dataAccessor.loginUser(username, password);
    }

    public SavedSet findById(Long id) {
        return dataAccessor.findById(id);
    }

    @Override
    public int[] addVote(Long setId, Long userId, String ipAddress) {
        return dataAccessor.addVote(setId, userId, ipAddress);
    }

    @Override
    public int[] removeVote(Long setId, Long userId, String ipAddress) {
        return dataAccessor.removeVote(setId, userId, ipAddress);
    }

    private DataAccessor dataAccessor;
}
