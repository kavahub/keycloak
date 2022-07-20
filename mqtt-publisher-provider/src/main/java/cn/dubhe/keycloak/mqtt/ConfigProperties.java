package cn.dubhe.keycloak.mqtt;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.keycloak.Config.Scope;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置属性
 * 
 * @author PinWei Wan
 * @since 17.0.1
 */
@Getter
@Slf4j
public class ConfigProperties {
    /** 服务地址 */
    private String serverUri;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 客户端ID */
    private String clientId;

    /** 自动重新连接 */
    private boolean automaticReconnect;
    /** 清楚会话 */
    private boolean cleanSession;
    /** 连接超时，单位秒 */
    private int connectionTimeout;
    /** 保持连接, 单位秒 */
    private int keepAliveInterval;

    private ConfigProperties(String serverUri, String username, String password, String clientId,
            boolean automaticReconnect, boolean cleanSession, int connectionTimeout, int keepAliveInterval) {
        this.serverUri = serverUri;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.automaticReconnect = automaticReconnect;
        this.cleanSession = cleanSession;
        this.connectionTimeout = connectionTimeout;
        this.keepAliveInterval = keepAliveInterval;
    }

    public static ConfigProperties create(Scope config) {
        final ConfigProperties rslt = Properties.configProperties(config);
        log.info(rslt.toString());
        return rslt;
    }

    public MqttClient mqttClient() throws MqttException {
        return new MqttClient(serverUri, clientId);
    }
    
    public MqttConnectOptions mqttConnectOptions() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(automaticReconnect);
        options.setCleanSession(cleanSession);
        options.setConnectionTimeout(connectionTimeout);
        options.setKeepAliveInterval(keepAliveInterval);

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password) ) {
            options.setUserName(username);
            options.setPassword(password.toCharArray());
        }   

        return options;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "password");
    }

    public Map<String, String> toMap() {
        final Map<String, String> rslt = new HashMap<>();
        rslt.put(Properties.SERVER_URI.getCode(), this.serverUri);
        rslt.put(Properties.USERNAME.getCode(), this.username);
        rslt.put(Properties.CLIENT_ID.getCode(), this.clientId);
        rslt.put(Properties.AUTOMATIC_RECONNECT.getCode(), String.valueOf(this.automaticReconnect));
        rslt.put(Properties.CLEAN_SESSION.getCode(), String.valueOf(this.cleanSession));
        rslt.put(Properties.CONNECTION_TIMEOUT.getCode(), String.valueOf(this.connectionTimeout));
        rslt.put(Properties.KEEP_ALIVE_INTERVAL.getCode(), String.valueOf(this.keepAliveInterval));
        return null;
    }

    @Getter
    public enum Properties {
        SERVER_URI("serverUri", "MQTT Broker 服务的 TCP/IP 地址", "tcp://localhost:1883"),
        USERNAME("username", "通过发送用户名和密码来进行相关的认证和授权", null),
        PASSWORD("password", "通过发送用户名和密码来进行相关的认证和授权", null),
        CLIENT_ID("clientId", "服务端使用 ClientId 识别客户端。连接服务端的每个客户端都有唯一的 ClientId", "keycloak"),
        AUTOMATIC_RECONNECT("automaticReconnect", "自动重新连接", true),
        CLEAN_SESSION("cleanSession", "客户端和服务端可以保存会话状态，以支持跨网络连接的可靠消息传输，这个标志告诉服务器这次连接是不是一个全新的连接", true),
        CONNECTION_TIMEOUT("connectionTimeout", "以秒为单位，连接超时时间", 10),
        KEEP_ALIVE_INTERVAL("keepAliveInterval", "以秒为单位的时间间隔，它是指在客户端传输完成一个控制报文的时刻到发送下一个报文的时刻，两者之间允许空闲的最大时间间隔", 60),
        ;

        private final String code;
        private final String helpText;
        private final Object defaultValue;

        private Properties(String code, String helpText, Object defaultValue) {
            this.code = code;
            this.helpText = helpText;
            this.defaultValue = defaultValue;
        }

        public String resolveConfigVar(final Scope config) {
            String value = this.defaultValue == null ? null : this.defaultValue.toString();
            if (config != null && config.get(this.code) != null) {
                value = config.get(this.code);
            } else {
                // 尝试从环境变量中读取配置信息，如: S8D_MQTT_SERVERURI:
                String envVariableName = "MQTT_" + this.code.toUpperCase(Locale.ENGLISH);
                String env = System.getenv(envVariableName);
                if (env != null) {
                    value = env;
                }
            }
            return value;
        }

        /**
         * 读取参数信息
         * 
         * @param config
         * @return
         */
        public static ConfigProperties configProperties(final Scope config) {
            final String serverUri = SERVER_URI.resolveConfigVar(config);
            final String username = USERNAME.resolveConfigVar(config);
            final String password = PASSWORD.resolveConfigVar(config);
            final boolean automaticReconnect = Boolean
                    .parseBoolean(AUTOMATIC_RECONNECT.resolveConfigVar(config));
            final boolean cleanSession = Boolean.parseBoolean(CLEAN_SESSION.resolveConfigVar(config));
            final int connectionTimeout = Integer.parseInt(CONNECTION_TIMEOUT.resolveConfigVar(config));
            final int keepAliveInterval = Integer.parseInt(KEEP_ALIVE_INTERVAL.resolveConfigVar(config));
            final String clientId = CLIENT_ID.resolveConfigVar(config);
    
            return new ConfigProperties(serverUri, username, password, clientId,
                    automaticReconnect, cleanSession, connectionTimeout, keepAliveInterval);
        }
    }
}
