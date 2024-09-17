package com.github.thomasdarimont.keycloak.trustdevice.model.jpa;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;
import org.keycloak.models.KeycloakSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrustedDeviceJpaEntityProvider implements JpaEntityProvider {

    public static final String ID = "jpa-trusted-device-entity";

    private static final List<Class<?>> ENTITIES = Collections.singletonList(TrustedDeviceEntity.class);

    @Override
    public List<Class<?>> getEntities() {
        return ENTITIES;
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/custom-trusted-device-changelog.xml";
    }

    @Override
    public String getFactoryId() {
        return ID;
    }

    @Override
    public void close() {
        // NOOP
    }
}
