/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Sep-2011 08:27:48
 */
public class PersistenceException extends Exception {

    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
