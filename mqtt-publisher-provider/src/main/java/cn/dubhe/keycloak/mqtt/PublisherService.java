package cn.dubhe.keycloak.mqtt;

import org.keycloak.provider.Provider;

/**
 * 发送消息服务
 *  
 * @author PinWei Wan
 * @since 17.0.1
 */
public interface PublisherService extends Provider {
    
    /**
     * 发布消息
     * @param topic 主题
     * @param message 消息
     */
    public void publish(final String topic, final String message);
}
