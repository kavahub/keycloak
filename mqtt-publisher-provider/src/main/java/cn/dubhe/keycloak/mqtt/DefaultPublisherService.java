package cn.dubhe.keycloak.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的服务
 * 
 * @author PinWei Wan
 * @since 17.0.1
 */
@Slf4j
public class DefaultPublisherService implements PublisherService {
    private final ConfigProperties properties;

    public DefaultPublisherService(ConfigProperties properties) {
        this.properties = properties;
    }

    @Override
    public void close() {
    }

    @Override
    public void publish(String topic, String message) {
        if (log.isDebugEnabled()) {
            log.debug("Send message to MQTT server, topic:{}, message:{}", topic, message);
        }

        if (Objects.isNull(topic) || Objects.isNull(message)) {
            throw new IllegalArgumentException("Parameters 'topci' and 'message' are required");
        }

        try {
            MqttClient client = properties.mqttClient();
            MqttConnectOptions options = properties.mqttConnectOptions();
            client.connect(options);

            MqttMessage payload = toPayload(message);
            payload.setQos(0);
            payload.setRetained(true);
            client.publish(topic, payload);
            client.disconnect();
        } catch (Exception e) {
            throw new MqttPublishException(e);
        }
    }

    private MqttMessage toPayload(String s) {
        byte[] payload = s.getBytes(StandardCharsets.UTF_8);
        return new MqttMessage(payload);
    }
}
