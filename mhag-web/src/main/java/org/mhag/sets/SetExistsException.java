package org.mhag.sets;

/**
 * Created by bronwen.
 * Date: 23/10/12
 * Time: 11:23
 */
public class SetExistsException extends PersistenceException {
    public SetExistsException(String message) {
        super(message);
    }
}
