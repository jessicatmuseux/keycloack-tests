package com.example.keycloak.logging;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.jboss.logging.Logger;

public class MaskedLoggingProvider implements EventListenerProvider, EventListenerProviderFactory {
    private static final Logger logger = Logger.getLogger(MaskedLoggingProvider.class);

    @Override
    public void onEvent(Event event) {
        if (event.getType() == EventType.LOGIN_ERROR || event.getType() == EventType.LOGIN) {
            String maskedUser = maskUsername(event.getUserId());
            logger.infof("Custom Log: Login attempt for user: %s", maskedUser);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        if (adminEvent.getOperationType() == OperationType.ACTION && adminEvent.getResourceType() == ResourceType.USER) {
            String maskedUser = maskUsername(adminEvent.getResourcePath());
            logger.infof("Custom Log: Admin action performed on user: %s", maskedUser);
        }
    }

    private String maskUsername(String username) {
        if (username == null || username.isEmpty()) {
            return "unknown";
        }
        if (username.contains("@")) {
            String[] parts = username.split("@");
            return maskString(parts[0]) + "@" + parts[1];
        } else {
            return maskString(username);
        }
    }

    private String maskString(String str) {
        if (str.length() <= 2) {
            return "*".repeat(str.length());
        }
        int maskLength = (int) Math.ceil(str.length() * 0.6);
        StringBuilder masked = new StringBuilder(str.substring(0, str.length() - maskLength));
        for (int i = 0; i < maskLength; i++) {
            masked.append("*");
        }
        return masked.toString();
    }

    @Override
    public void close() {}

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new MaskedLoggingProvider();
    }

    @Override
    public void init(org.keycloak.Config.Scope config) {}

    @Override
    public void postInit(KeycloakSessionFactory factory) {}

    @Override
    public String getId() {
        return "masked-logging";
    }
}