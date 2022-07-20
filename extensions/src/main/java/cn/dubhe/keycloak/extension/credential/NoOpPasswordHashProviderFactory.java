package cn.dubhe.keycloak.extension.credential;

import org.keycloak.Config.Scope;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * 明文密码
 *  
 * @author PinWei Wan
 * @since 17.0.1
 */
public class NoOpPasswordHashProviderFactory implements PasswordHashProviderFactory {
    public static final String PROVIDER_ID = "noop";
    private static PasswordHashProvider SINGLETON = new NoOpPasswordHashProvider();

    @Override
    public PasswordHashProvider create(KeycloakSession session) {
        return SINGLETON;
    }

    @Override
    public void init(Scope config) {
       
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        
    }

    @Override
    public void close() {
        
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
    
}
