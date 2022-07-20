# springboot-embedder-server

基于springboot的，内嵌式的Keycloak服务

#### 修改配置文件
修改数据库配置， 编辑`src/resources/application-dev.yml`文件，修改配置：

```
spring:
...
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/keycloak-embed?characterEncoding=UTF-8&useSSL=true
    username: root
    password: root

...
keycloak:
  custom:
    adminUser:
      username: admin
      password: admin
      create-admin-user-enabled: true

    migration:
      import-enabled: true
      import-location: classpath:dubhe-realm.json
...
```    
#### 打包

``` 
mvn clean install
```

命令运行成功后，在项目目录`target`下有`cn.dubhe.keycloak.springboot-embedded-server-17.0.1.jar`文件。

#### 本地运行项目  

可以在IDE中启动项目，也可以在命令行使用maven命令启动：

``` 
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

***说明***：在项目启动过程中，会自动创建表结构，初始化超级用户，导入dubhe realm等等

打开游览器，访问地址：http://localhost:9000/auth ，输入超级用户名及密码： admin/admin

#### Docker中运行项目

Step-1: 在生成镜像的过程中，需要docker环境。如果是windows系统，启动`Docker Desktop` 

Step-2: 在`Docker Desktop`中安装mysql，如下命令：
```
docker pull mysql:8.0.29
docker run -p 13306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.29
```

启动mysql服务，使用客户端连接服务（端口号是：13306），创建数据库和用户，脚本如下：
```
CREATE DATABASE IF NOT EXISTS keycloak_embed DEFAULT CHARSET utf8;
create user 'kkuser'@'%' identified by 'kkuser';
grant create,alter,drop,select,insert,update,delete on keycloak.* to kkuser@'%';
flush privileges; 
```


Step-3: 打包，生成镜像文件，运行如下命令：

```
mvn clean spring-boot:build-image
```

Step-4: 运行镜像
```
docker run -p 9000:9000 --link mysql:mysql --name springboot-embedded-server -e "SPRING_PROFILES_ACTIVE=pro" -d docker.io/library/springboot-embedded-server:17.0.1
```

成功后，如下操作：
1. 打开游览器访问：http://localhost:9000/auth 使用admin/admin登录
2. 选择`Add realm` -> `select file`, 找到`springboot-embedded-server\src\main\resources\dubhe-realm.json`文件，导入即可
3. 打开游览器，输入地址：http://localhost:9000/auth/realms/dubhe/account/ 可以使用用户`zhangs/123`登录成功

#### 阿里云镜像服务

阿里云提供个人的，免费的镜像服务。开通服务，在本机上运行
```
docker login --username=<阿里云账号> registry.cn-hangzhou.aliyuncs.com
```

然后运行命令
```
mvn compile jib:build -Pali-docker 
```

命令成功运行后，在阿里云镜像服务中可以查询到

本地拉取并运行镜像
```
docker run -p 9000:9000 --link mysql:mysql --name springboot-embedded-server -d registry.cn-hangzhou.aliyuncs.com/beidou/cn.dubhe.keycloak.springboot-embedded-server
```


