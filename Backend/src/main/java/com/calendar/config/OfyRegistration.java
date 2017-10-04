package com.calendar.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by valishah on 2017-08-31.
 */
public class OfyRegistration implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Register objectify entities.
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // ServletContextListener destroyed.
    }
}
