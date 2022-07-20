package cn.dubhe.keycloak.extension.credential;

import org.keycloak.Config.Scope;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * BCrypt密码加密
 *  
 * @author PinWei Wan
 * @since 17.0.1
 */
public class BCryptPasswordHashProviderFactory implements PasswordHashProviderFactory {
    public static final String PROVIDER_ID = "bcrypt";
    private static PasswordHashProvider SINGLETON = new BCryptPasswordHashProvider();

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
