package com.github.thomasdarimont.keycloak.auth.identityfirst;

import com.google.auto.service.AutoService;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

@AutoService(AuthenticatorFactory.class)
public class SelectUserAuthenticatorFormFactory implements AuthenticatorFactory {

    private static final String PROVIDER_ID = "auth-select-user";

    @Override
    public String getDisplayType() {
        return "Select User";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Selects a user.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {

        ProviderConfigProperty useAjax = new ProviderConfigProperty();
        useAjax.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        useAjax.setName(SelectUserAuthenticatorForm.USE_AXJAX_CONFIG_PROPERTY);
        useAjax.setLabel("Use AJAX");
        useAjax.setHelpText("Use asynchronous froms submitted via AJAX");
        useAjax.setDefaultValue(true);

        return Arrays.asList(useAjax);
    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new SelectUserAuthenticatorForm(session);
    }

    @Override
    public void init(Config.Scope config) {
        // NOOP
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
