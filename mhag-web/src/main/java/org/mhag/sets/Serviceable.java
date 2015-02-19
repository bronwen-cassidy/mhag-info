/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

import org.mhag.ArmourSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Sep-2011 08:21:01
 */
public interface Serviceable {

    Long saveArmourSet(SavedSet armourSet) throws PersistenceException;

    void updateArmourSet(SavedSet armourSet) throws PersistenceException;

    Page<SavedSet> searchSets(Map<String, String> parameters, String[] skills, int start, int end, String orderBy, String direction);

    List<SavedSet> searchSets(Map<String, String> parameters, String[] skills, String orderBy, String direction);

    List<ArmourPiece> searchArmourPieces(Map<String, String> parameters, String skillName, String opertor, String value);

    void delete(Long id);

    void deleteAll(Long[] ids);

    Long registerUser(String username, String password);

    Long loginUser(String username, String password);

    SavedSet findById(Long id);

    int[] addVote(Long setId, Long userId, String ipAddress);

    int[] removeVote(Long setId, Long userId, String ipAddress);
}
