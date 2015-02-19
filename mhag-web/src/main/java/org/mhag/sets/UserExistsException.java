/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag.sets;

import org.springframework.dao.DataAccessException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Oct-2011 18:26:53
 */
public class UserExistsException extends DataAccessException {

    public UserExistsException(String msg) {
        super(msg);
    }

    public UserExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
