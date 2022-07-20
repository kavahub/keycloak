package cn.dubhe.keycloak.extension.credential;

import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;

/**
 * 明文密码
 * 
 * @author PinWei Wan
 * @since 17.0.1
 */
public class NoOpPasswordHashProvider implements PasswordHashProvider {
    @Override
    public void close() {
    }

    @Override
    public boolean policyCheck(PasswordPolicy policy, PasswordCredentialModel credential) {
        return credential.getPasswordCredentialData().getHashIterations() == policy.getHashIterations()
                && NoOpPasswordHashProviderFactory.PROVIDER_ID.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(String rawPassword, int iterations) {
        String encodedPassword = encode(rawPassword);

        return PasswordCredentialModel.createFromValues(NoOpPasswordHashProviderFactory.PROVIDER_ID, null, iterations, encodedPassword);
    }

    @Override
    public boolean verify(String rawPassword, PasswordCredentialModel credential) {
        return matches(rawPassword, credential.getPasswordSecretData().getValue());
    }

    private String encode(String rawPassword) {
        return rawPassword;
    }

    private boolean matches(String rawPassword, String value) {
        return rawPassword.equals(value);
    }
}
