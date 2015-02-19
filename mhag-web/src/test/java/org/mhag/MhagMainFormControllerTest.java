package org.mhag;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bronwen.
 * Date: 12/02/12
 * Time: 22:13
 */
public class MhagMainFormControllerTest {
    @Before
    public void setUp() throws Exception {
        controller = new MhagMainFormController();
        MhagFacade facade = new MhagFacade();
        facade.setVersion(0);
        facade.afterPropertiesSet();

        controller.setMhagFacade(facade);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testArmourSetHandler() throws Exception {

    }

    @Test
    public void testArmourHandler() throws Exception {

    }

    @Test
    public void testUrlDisplayHander() throws Exception {

    }

    private MhagMainFormController controller;
}
