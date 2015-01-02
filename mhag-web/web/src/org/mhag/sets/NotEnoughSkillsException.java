package org.mhag.sets;

/**
 * Created by bronwen.
 * Date: 23/10/12
 * Time: 11:21
 */
public class NotEnoughSkillsException extends PersistenceException {
    public NotEnoughSkillsException(String message) {
        super(message);
    }
}
