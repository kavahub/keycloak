package cn.dubhe.keycloak.mqtt;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

/**
 * spid定义
 *  
 * @author PinWei Wan
 * @since 17.0.1
 */
public class MqttPublisherSpi implements Spi {

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "mqttPublisher";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return PublisherService.class;
    }

    @Override
    public Class<? extends ProviderFactory<?>> getProviderFactoryClass() {
        return PublisherServiceProviderFactory.class;
    }
    
}
