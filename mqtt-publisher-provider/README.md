# mqtt-publisher-provider

#### Keycloak SPI： 发送消息到MQTT服务器(mqttPublisher)

定义的SPI插件：
名称 | 用途
---|---
dubhe-server | 将消息发送到MQTT服务器，这需要配置MQTT服务器信息
dubhe-logger | 将消息发送到日志，一般情况下，这种方式适用于测试或调试

## 配置

在项目`springboot-embeedded-server`下，找到`application.yml`文件，添加如下信息

```
keycloak:
...
  # server 插件配置
  mqttPublisher:
    provider: mqtt
    serverUri: tcp://localhost:1883   # 必须的，MQTT服务地址
    username: xxx                     # 可选的，登录用户名
    password: xxxx                    # 可选的，登录密码
    clientId: xxxxx                   # 必须的，客户端ID， 默认：keycloak
    automaticReconnect: true          # 可选的，自动连接，默认：true
    cleanSession: true                # 可选的，保存会话状态，默认：true
    connectionTimeout: 10             # 可选的，连接超时时间，单位秒，默认：10
    keepAliveInterval: 60             # 可选的，保持连接时间，单位秒，默认：60

  # logger 插件配置
  mqttPublisher:
    provider: logger
...  
```

**配置其中一个插件即可**

#### 参考

- [MQTT Topics, Wildcards, & Best Practices - MQTT Essentials: Part 5](https://www.hivemq.com/blog/mqtt-essentials-part-5-mqtt-topics-best-practices/)
- [MQTT 入门](https://www.emqx.com/zh/mqtt)