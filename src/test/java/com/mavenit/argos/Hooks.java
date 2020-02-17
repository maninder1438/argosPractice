package com.mavenit.argos;

import com.mavenit.argos.driver.DriverFactory;
import org.junit.After;
import org.junit.Before;

public class Hooks {

    DriverFactory factory = new DriverFactory();

    @Before
    public void setUp(){
        factory.openBrowser();
        // TODO: 2020-02-08 Remove url from code
        factory.navigateTo("https://www.argos.co.uk/");
        factory.maxiBrowser();
    }

    @After
    public void  tearDown(){
        factory.closeBrowser();
    }




}
