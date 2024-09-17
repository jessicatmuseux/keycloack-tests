package com.github.thomasdarimont.keycloak.virtualclients;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.ClientScopeModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.client.ClientStorageProvider;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

public class VirtualClientStorageProvider implements ClientStorageProvider {

    private final KeycloakSession session;
    private final ComponentModel componentModel;
    private final VirtualClientModelGenerator virtualClientModelGenerator;

    public VirtualClientStorageProvider(KeycloakSession session, ComponentModel componentModel) {

        this.session = session;
        this.componentModel = componentModel;
        this.virtualClientModelGenerator = new VirtualClientModelGenerator();
    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public ClientModel getClientById(RealmModel realm, String id) {
        if (!"virtual-clients".equals(realm.getName())) {
            return null;
        }

        return this.virtualClientModelGenerator.createVirtualModel(id, null, realm);
    }

    @Override
    public ClientModel getClientByClientId(RealmModel realm, String clientId) {
        // Fetch clients from remote location...

        if (!"virtual-clients".equals(realm.getName())) {
            return null;
        }

        if (clientId == null) {
            return null;
        }

        String internalClientId = "f:" + componentModel.getId() + ":";
        if (!(clientId.startsWith("f:virtual:") || clientId.startsWith(internalClientId))) {
            return null;
        }

        String generatedId = internalClientId + clientId.substring(clientId.lastIndexOf(':') + 1);

        // dynamically generate dummy clients for testing...
        VirtualClientModel virtualModel = this.virtualClientModelGenerator.createVirtualModel(generatedId, generatedId, realm);

        if (virtualModel.isServiceAccountsEnabled()) {
            UserModel serviceAccount = session.userLocalStorage().getServiceAccount(virtualModel);
            if (serviceAccount == null) {

                UserModel newServiceAccount = createServiceAccountUser(realm, virtualModel);
                // TODO find a way to delete the dangling service account users...
//                RoleModel serviceRole = realm.getRole("service");
//                newServiceAccount.grantRole(serviceRole);
            }
        }

        return virtualModel;
    }

    @Override
    public Stream<ClientModel> searchClientsByClientIdStream(RealmModel realm, String clientId, Integer firstResult, Integer maxResults) {
        // TODO implement search for clients by clientId
        return Stream.empty();
    }

    @Override
    public Stream<ClientModel> searchClientsByAttributes(RealmModel realm, Map<String, String> attributes, Integer firstResult, Integer maxResults) {
        return Stream.empty();
    }

    @Override
    public Map<String, ClientScopeModel> getClientScopes(RealmModel realm, ClientModel client, boolean defaultScopes) {
        return Collections.emptyMap();
    }

    private UserModel createServiceAccountUser(RealmModel realm, ClientModel clientModel) {

        UserModel newServiceAccount = session.userLocalStorage().addUser(realm, "service-account-" + clientModel.getClientId());
        newServiceAccount.setEnabled(true);
        newServiceAccount.setServiceAccountClientLink(clientModel.getId());

        return newServiceAccount;
    }
}
