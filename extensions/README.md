# extensions

`keycloak`扩展插件集合

## 1. Keycloak SPI 插件： oidc-lucky-number-mapper  

`OIDC`协议的令牌映射器

可以配置这个映射器，设置上限和下限数值，映射器会随机生成之间的一个数值，并保存到令牌中

### Keycloak设置
1. 启动项目`springboot-embedded-server`
2. 管理员登录Keycloak界面，在 `Clients > 选择 public-client > Mappers > 点击 create` ，在界面中填如下值(其他项保持默认值)：`name` -> Lucky Number Mapper, `Mapper Type` -> Lucky Number, `Token Claim Name` -> myLuckyNumber
3. 点击Save

### 帮助

设置完成后，首先登录获取令牌：

```
curl --location --request POST 'http://localhost:9000/auth/realms/dubhe/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=zhangs' \
--data-urlencode 'password=123' \
--data-urlencode 'client_id=public-client' \
--data-urlencode 'grant_type=password'

返回结果：

{
	"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoMURyc1BJYUNjMEoyMVJhM29NekQ1SS1leTViZEhvYXFrOUY4TzhiTzNZIn0.eyJleHAiOjE2NTMwNDA5NDksImlhdCI6MTY1MzAzOTgwOSwianRpIjoiYzk0YTc3NTktM2I2MC00ZmRiLTk3MDctZWNiYThmZWU5MGJkIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwL2F1dGgvcmVhbG1zL3NwcmluZ3NlZWRzIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjhiZTYzYjdlLTU0ZDItNDEzZS05ZTFjLTI3ZWU4NzI1ZDE3NSIsInR5cCI6IkJlYXJlciIsImF6cCI6InB1YmxpYy1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiZjc2MDVjZDctODE4MS00MGFkLWJhMWMtOTUyNGI3NjIwNDk2IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1zcHJpbmdzZWVkcyIsIm9zcy1yZWFkIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZjc2MDVjZDctODE4MS00MGFkLWJhMWMtOTUyNGI3NjIwNDk2IiwiYmlydGhkYXRlIjoiMTk3Ny0wMi0wMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoi5bygIOS4ieS4iSIsInByZWZlcnJlZF91c2VybmFtZSI6InpoYW5ncyIsImdpdmVuX25hbWUiOiLlvKAiLCJmYW1pbHlfbmFtZSI6IuS4ieS4iSIsIm15THVja3lOdW1iZXIiOjY1LCJlbWFpbCI6InpoYW5nc0BxcS5jb20ifQ.QhjhuimFF_-KSXiWVBsY52zSja7iZEK_a6iD__YT-9XaXTJHfUYX4AJpPLrrDhfErCuk0Z21t77sUSfZJNFMPyA3jodxcu2Mt7AbRuEoqITt1561hgCTALc3bHlGl2aW6UKxHfGZBE6i8FAEOUZlOSYk_Ydekd_vLFie2i5NrHGyzBfJy-f3zW-DncuaR7e0rxDKTu8xANwkepQaBRwGJOjzh0M-0AXeyK8ccH_EbmlstXCpUkpCdypr9tdmMdyynxHk_kyxQju8iF4lmanIRcQ4VfOZDYxty7DVeTCMSacYrA4GhokXMH0XoyzkiGd0fP_untnZsxnGiDYKeWndOg",
	"expires_in": 1140,
	"refresh_expires_in": 1800,
	"refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5OGQwMTczNi0wZWM0LTQ1N2YtYWE2NS03OGJkMjFjNTQ5NjQifQ.eyJleHAiOjE2NTMwNDE2MDksImlhdCI6MTY1MzAzOTgwOSwianRpIjoiMWIwMWE3NTUtMmUwMC00OWQ1LWI2MTctMmUwZGVjYjk0OTgyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwL2F1dGgvcmVhbG1zL3NwcmluZ3NlZWRzIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwL2F1dGgvcmVhbG1zL3NwcmluZ3NlZWRzIiwic3ViIjoiOGJlNjNiN2UtNTRkMi00MTNlLTllMWMtMjdlZTg3MjVkMTc1IiwidHlwIjoiUmVmcmVzaCIsImF6cCI6InB1YmxpYy1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiZjc2MDVjZDctODE4MS00MGFkLWJhMWMtOTUyNGI3NjIwNDk2Iiwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZjc2MDVjZDctODE4MS00MGFkLWJhMWMtOTUyNGI3NjIwNDk2In0.S_mTH7byJiekM2Y4beRjMVSc8YkVsLGhsfydJBlzwuw",
	"token_type": "Bearer",
	"not-before-policy": 1651718967,
	"session_state": "f7605cd7-8181-40ad-ba1c-9524b7620496",
	"scope": "profile email"
}
```

然后访问用户接口：

```
curl --location --request GET 'http://localhost:9000/auth/realms/dubhe/protocol/openid-connect/userinfo' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoMURyc1BJYUNjMEoyMVJhM29NekQ1SS1leTViZEhvYXFrOUY4TzhiTzNZIn0.eyJleHAiOjE2NTMwNDA5NDksImlhdCI6MTY1MzAzOTgwOSwianRpIjoiYzk0YTc3NTktM2I2MC00ZmRiLTk3MDctZWNiYThmZWU5MGJkIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwL2F1dGgvcmVhbG1zL3NwcmluZ3NlZWRzIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjhiZTYzYjdlLTU0ZDItNDEzZS05ZTFjLTI3ZWU4NzI1ZDE3NSIsInR5cCI6IkJlYXJlciIsImF6cCI6InB1YmxpYy1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiZjc2MDVjZDctODE4MS00MGFkLWJhMWMtOTUyNGI3NjIwNDk2IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1zcHJpbmdzZWVkcyIsIm9zcy1yZWFkIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZjc2MDVjZDctODE4MS00MGFkLWJhMWMtOTUyNGI3NjIwNDk2IiwiYmlydGhkYXRlIjoiMTk3Ny0wMi0wMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoi5bygIOS4ieS4iSIsInByZWZlcnJlZF91c2VybmFtZSI6InpoYW5ncyIsImdpdmVuX25hbWUiOiLlvKAiLCJmYW1pbHlfbmFtZSI6IuS4ieS4iSIsIm15THVja3lOdW1iZXIiOjY1LCJlbWFpbCI6InpoYW5nc0BxcS5jb20ifQ.QhjhuimFF_-KSXiWVBsY52zSja7iZEK_a6iD__YT-9XaXTJHfUYX4AJpPLrrDhfErCuk0Z21t77sUSfZJNFMPyA3jodxcu2Mt7AbRuEoqITt1561hgCTALc3bHlGl2aW6UKxHfGZBE6i8FAEOUZlOSYk_Ydekd_vLFie2i5NrHGyzBfJy-f3zW-DncuaR7e0rxDKTu8xANwkepQaBRwGJOjzh0M-0AXeyK8ccH_EbmlstXCpUkpCdypr9tdmMdyynxHk_kyxQju8iF4lmanIRcQ4VfOZDYxty7DVeTCMSacYrA4GhokXMH0XoyzkiGd0fP_untnZsxnGiDYKeWndOg'

返回结果：

{
	"sub": "8be63b7e-54d2-413e-9e1c-27ee8725d175",
	"resource_access": {
		"account": {
			"roles": ["manage-account", "manage-account-links", "view-profile"]
		}
	},
	"birthdate": "1977-02-01",
	"email_verified": true,
	"name": "张 三三",
	"preferred_username": "zhangs",
	"given_name": "张",
	"family_name": "三三",
	"myLuckyNumber": 10,
	"email": "zhangs@qq.com"
}

```

返回结果中包含`myLuckyNumber`信息，而且每次请求的值不一样的

## 2. Keycloak SPI 插件： bcrypt, noop

自定义密码加密算法。`Keycloak`提供默认的加密算法`pbkdf2-sha256`，已经满足使用了；有些特殊情况，如：用户的密码来自外部系统，这种情况下，就必须与外部系统的加密方式一致。

此插件使用`BCrypt`算法，代码引用`org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder`类， 框架`spring-security-crypto`中提供有许多的加密算法。

### Keycloak设置
1. 启动项目`springboot-embedded-server`
2. 管理员登录Keycloak界面，在 `Authertication > Password > 点击 Add policy... > 选择 Hashing Algorithm` ，在 `Policy Value` 中填入 `bcrpt`
3. 点击Save

每个realm，每个用户可以定义不同的加密方式，在Keycloak中新定义的算法不影响已存在的用户登录。

查看用户密码加密信息
1. 管理员登录Keycloak界面，在 `Users > 点击 View all users > 任意选择记录 > Credentials > Manage Credentials `
2. 找到 `Type` 是 `password` 的记录，点击 `Show data...`

**noop** 是明文密码，请谨慎使用





