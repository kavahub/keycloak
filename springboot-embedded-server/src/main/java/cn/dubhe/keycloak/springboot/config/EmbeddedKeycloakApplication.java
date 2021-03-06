package cn.dubhe.keycloak.springboot.config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.keycloak.Config;
import org.keycloak.exportimport.ExportImportConfig;
import org.keycloak.exportimport.ExportImportManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakTransactionManager;
import org.keycloak.services.ServicesLogger;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.resources.KeycloakApplication;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import cn.dubhe.keycloak.springboot.config.KeycloakCustomProperties.AdminUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 继承{@link KeycloakApplication}，实现创建用户和导入realm功能
 *  
 * @author PinWei Wan
 * @since 17.0.1
 */
@Slf4j
public class EmbeddedKeycloakApplication  extends KeycloakApplication {

    static KeycloakCustomProperties customProperties;
    
    @Override
    protected ExportImportManager bootstrap() {
        ExportImportManager exportImportManager = super.bootstrap();

        tryCreateMasterRealmAdminUser();
        tryImportRealm();
        return exportImportManager;
    }

    protected void loadConfig() {
        Config.init(SpringBootConfigProvider.getInstance());
    }

    protected void tryCreateMasterRealmAdminUser() {

        if (!customProperties.getAdminUser().isCreateAdminUserEnabled()) {
            log.info("Skipping creation of keycloak master adminUser.");
            return;
        }

        AdminUser adminUser = customProperties.getAdminUser();

        String username = adminUser.getUsername();
        if (!(StringUtils.hasLength(username) || StringUtils.hasText(username))) {
            return;
        }

        KeycloakSession session = getSessionFactory().create();
        KeycloakTransactionManager transaction = session.getTransactionManager();
        try {
            transaction.begin();

            boolean randomPassword = false;
            String password = adminUser.getPassword();
            if (!StringUtils.hasText(adminUser.getPassword())) {
                password = UUID.randomUUID().toString();
                randomPassword = true;
            }
            new ApplianceBootstrap(session).createMasterRealmUser(username, password);
            if (randomPassword) {
                log.info("Generated admin password: {}", password);
            }
            ServicesLogger.LOGGER.addUserSuccess(username, Config.getAdminRealm());

            transaction.commit();
        } catch (IllegalStateException e) {
            transaction.rollback();
            ServicesLogger.LOGGER.addUserFailedUserExists(username, Config.getAdminRealm());
        } catch (Throwable t) {
            transaction.rollback();
            ServicesLogger.LOGGER.addUserFailed(t, username, Config.getAdminRealm());
        } finally {
            session.close();
        }
    }

    protected void tryImportRealm() {

        KeycloakCustomProperties.Migration imex = customProperties.getMigration();
        if (!imex.isImportEnabled()) {
            return;
        }

        Resource importLocation = imex.getImportLocation();
        if (!importLocation.exists()) {
            log.error("Could not find keycloak import file {}", importLocation);
            return;
        }

        File file;
        try {
            file = importLocation.getFile();
        } catch (IOException e) {
            log.error("Could not read keycloak import file {}", importLocation, e);
            return;
        }

        log.info("Starting Keycloak realm configuration import from location: {}", importLocation);

        KeycloakSession session = getSessionFactory().create();

        ExportImportConfig.setAction("import");
        ExportImportConfig.setProvider(imex.getImportProvider());
        ExportImportConfig.setFile(file.getAbsolutePath());

        ExportImportManager manager = new ExportImportManager(session);
        manager.runImport();

        session.close();

        log.info("Keycloak realm configuration import finished.");
    }
}
