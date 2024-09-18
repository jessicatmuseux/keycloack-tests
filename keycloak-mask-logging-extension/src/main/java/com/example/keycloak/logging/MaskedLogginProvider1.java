package com.example.keycloak.logging;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.jboss.logging.Logger;

public class MaskedLogginProvider1 implements EventListenerProvider {

    //private static final Logger logger = LoggerFactory.getLogger(MaskedLogginProvider1.class);
    private static final Logger logger = Logger.getLogger(MaskedLogginProvider1.class);
    @Override
    public void onEvent(Event event) {
        if (event.getType() == EventType.LOGIN_ERROR || event.getType() == EventType.LOGIN) {
            //logger.info("Login failed or account disabled: {}", event.toString());
            logger.log(Logger.Level.INFO,"**Deivnino*** Login failed or account disabled: {}"+ event.toString());
            // Extract and format data for Splunk/SIEM or system.out
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {
        // Implement if needed
    }
}