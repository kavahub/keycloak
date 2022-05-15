package cn.springseed.keycloak.springboot.component;

import java.util.Collections;
import java.util.List;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.EnvironmentDependentProviderFactory;
import org.keycloak.provider.ProviderConfigProperty;

/**
 * 抽象的组件注册器
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public abstract class AbstractComponentsRegistrar implements AuthenticatorFactory, EnvironmentDependentProviderFactory {
    @Override
    public Authenticator create(KeycloakSession session) {
        // NOT USED
        return null;
    }

    @Override
    public void init(Config.Scope config) {
        // NOOP
    }
    
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[0];
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }   
    
    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return null;
    }

    @Override
    public void close() {
        // NOOP
    }    
}
