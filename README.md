# keycloak

集成keycloak，各个项目说明如下：

| 名称                         | 说明                                                                                 |
| ---------------------------- | ------------------------------------------------------------------------------------ |
| event-listener-mqtt-provider | Keycloak SPI eventsListener 插件：将系统事件发布到MQTT服务                           |
| extensions                   | Keycloak SPI 插件：许多的，简单的插件集合                                            |
| mail-sender-mqtt-provider    | Keycloak SPI emailSender 插件：将邮件发送至MQTT服务                                  |
| mqtt-publisher-provider      | 自定义SPI：集成MQTT client，发布消息到MQTT服务，其他插件可以调用此服务               |
| otp-mail-provider            | Keycloak SPI authenticator 插件：动态口令（OTP）使用邮件实现，将验证码发送至用户邮箱 |
| otp-sms-provider             | Keycloak SPI authenticator 插件：动态口令（OTP）使用短信实现，将验证码发送至用户手机 |
| theme-provider               | Keycloak SPI themeResource 插件：自定义主题                                          |
| springboot-embedded-server   | 基于spring boot的，内嵌式的Keycalok服务                                              |
| keycloak-server              | Keycloak官方服务配置，工具                                                           |
| angular-ui                   | 前端Angular中使用keycloak                                                            |

#### 使用说明

下载项目，使用maven编译打包：

```
mvn clean install
```

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 参考
- [更详细的说明请参考我的博客](https://blog.csdn.net/i_mouse)
- [Understanding token usage in Keycloak](https://www.janua.fr/understanding-token-usage-in-keycloak/#:~:text=Offline%20tokens%20can%20have%20very%20long%20living%20period,the%20offline%20token%20for%20a%20refresh%20token%20action.)
- [Customizing Themes for Keycloak](https://www.baeldung.com/spring-keycloak-custom-themes)
- [Customizing the Login Page for Keycloak](https://www.baeldung.com/keycloak-custom-login-page)
- [Custom User Attributes with Keycloak](https://www.baeldung.com/keycloak-custom-user-attributes)
