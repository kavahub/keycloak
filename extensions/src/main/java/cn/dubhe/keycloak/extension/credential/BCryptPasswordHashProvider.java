package cn.dubhe.keycloak.extension.credential;

import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BCrypt密码加密
 * 
 * @author PinWei Wan
 * @since 17.0.1
 */
public class BCryptPasswordHashProvider implements PasswordHashProvider {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void close() {
    }

    @Override
    public boolean policyCheck(PasswordPolicy policy, PasswordCredentialModel credential) {
        return credential.getPasswordCredentialData().getHashIterations() == policy.getHashIterations()
                && BCryptPasswordHashProviderFactory.PROVIDER_ID.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(String rawPassword, int iterations) {
        String encodedPassword = encoder.encode(rawPassword);

        return PasswordCredentialModel.createFromValues(BCryptPasswordHashProviderFactory.PROVIDER_ID, null, iterations, encodedPassword);
    }

    @Override
    public boolean verify(String rawPassword, PasswordCredentialModel credential) {
        return encoder.matches(rawPassword, credential.getPasswordSecretData().getValue());
    }
}
