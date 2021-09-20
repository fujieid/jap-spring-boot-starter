# jap-spring-boot-starter

这是为[JustAuth Plus](https://justauth.plus/)开发的Spring Boot Starter依赖。

访问https://github.com/Vector6662/jap-spring-boot-starter-demo，为本starter的demo。

## 快速开始

### Step 1：引入依赖

首先需要引入基本的maven依赖：

```xml
<dependency>
    <groupId>com.fujieid.jap.spring.boot</groupId>
    <artifactId>jap-spring-boot-starter</artifactId>
    <version>${VERSION}</version>
</dependency>
```

为JustAuth Plus的[四种授权策略](https://justauth.plus/quickstart/)都提供了相应的starter：

`jap-simple-spring-boot-starter`、`jap-social-spring-boot-starter`、`jap-oauth2-spring-boot-starter`、`jap-oidc-spring-boot-starter`

根据你的web后台需要支持的授权策略，引入相应的starter。如你的项目需要simple和oauth2授权方式，那么只需要在`pom.xml`中引入：

```xml
<dependency>
    <groupId>com.fujieid.jap.spring.boot</groupId>
    <artifactId>jap-simple-spring-boot-starter</artifactId>
    <version>${VERSION}</version>
</dependency>
<dependency>
    <groupId>com.fujieid.jap.spring.boot</groupId>
    <artifactId>jap-oauth2-spring-boot-starter</artifactId>
    <version>${VERSION}</version>
</dependency>
```

其他两种授权策略的maven坐标如下：

```xml
<dependency>
    <groupId>com.fujieid.jap.spring.boot</groupId>
    <artifactId>jap-social-spring-boot-starter</artifactId>
    <version>${VERSION}</version>
</dependency>
<dependency>
    <groupId>com.fujieid.jap.spring.boot</groupId>
    <artifactId>jap-oidc-spring-boot-starter</artifactId>
    <version>${VERSION}</version>
</dependency>
```

### Step 2：`application.properties`中的基础配置

引入maven依赖后，你需要对jap-spring–boot-starter进行一些基础配置，多数情况下采用默认即可。下面是一些简单的例子。

```properties
# basic 基本配置
# 是否为sso
jap.basic.sso=false
jap.basic.cache-expire-time=12
jap.basic.token-expire-time=12
# 如果启启用了sso，则可能需要对sso进行一些配置
# sso
jap.sso.cookie-domain=xxx
jap.sso.cookie-max-age=xxx
jap.sso.cookie-name=xxx
```



准备工作已完成，接下来进行编码，以oauth2为例，有以下三个步骤：

### Step 3：实现`JapUserServiceType`

为oauth2策略创建一个service，具体可以参考[jap-oauth2：实现 `JapUserService` 接口](https://justauth.plus/quickstart/jap-oauth2.html#%E5%AE%9E%E7%8E%B0-japuserservice-%E6%8E%A5%E5%8F%A3)。❗❗❗特别注意的是，**你需要在`@Service`注解中添加参数`JapUserServiceType.OAUTH2`**，表明这是oauth2的`JapUserService`，像这样：

```java
@Service(JapUserServiceType.OAUTH2)
public class Oauth2UserServiceImpl implements JapUserService {
	......
}
```

当然，也可以在`application.properties`中配置oauth2的`JapUserService`，即指定该service类的包全名：

```properties
jap.oauth2-user-service=my.dong6662.japspringbootstarterdemo.service.Oauth2UserServiceImpl
```

### Step 4：`application.properties`中配置oauth2

oauth2提供了五种授权方式：*授权码（authorization-code）*、*隐式（implicit）*、*密码（password）*、*client-credentials*、*refresh-token*，选择一种或多种作为你的web应用支持的oauth2授权方式。这里提供了*授权码* 方式下的demo：

```properties
# gitee平台
# 授权码方式

# 指定平台
jap.oauth2[0].platform=gitee
# 授权码方式
jap.oauth2[0].grant-type=authorization_code
# 授权码方式下response-type必须为code
jap.oauth2[0].response-type=code
jap.oauth2[0].client-id=e9b4f19402d2ccb3375f5be19b9c76738fffe071d6b450a65dc4baa70a7ab752
jap.oauth2[0].client-secret=83bd48fc1ec9807f769c6328304e6222f2290b57d60f346a24976b48a752b794
# 你的应用服务器提供的接口，会接受code参数
jap.oauth2[0].callback-url=http://localhost:8080/oauth/gitee/authorization-code
# 获取token的地址
jap.oauth2[0].token-url=https://gitee.com/oauth/token
# 所有的api在：https://gitee.com/api/v5/swagger#/getV5User
jap.oauth2[0].userinfo-url=https://gitee.com/api/v5/user
# 获取user info的方法，GET、POST等。每个platform的不一样，需要查看具体平台的API
jap.oauth2[0].user-info-endpoint-method-type=get
# 获取授权码code的地址
jap.oauth2[0].authorization-url=https://gitee.com/oauth/authorize
jap.oauth2[0].verify-state=false
```

### Step 5：编写Controller

使用`JapTemplate`，仅需在Controller中完成一行代码即可。调用`authenticateByAuthorizationCode`方法，表明使用的是授权码方式，并指定platform即可。

```java
@RestController
@RequestMapping("/oauth")
public class Oauth2Controller {
    @Autowired
    JapTemplate japTemplate;
    
    @RequestMapping("/gitee/authorization-code")
    public JapResponse authorizationCode(){
        // 访问该路径需要有code参数
        return japTemplate.opsForOauth2().authenticateByAuthorizationCode("gitee");
    }
}
```

可以发现，调用`authenticateByAuthorizationCode`方法的url也是上边配置信息中`jap.oauth2[0].callback-url`填写的参数。

若你还想采用oauth2别的授权方式，比如*密码*，则可以调用方法：

```java
public JapResponse authenticateByPassword(String platform, String username, String password)
```

但是别忘了在配置文件`application.properties`中添加密码方式相应的配置信息。

## 引入redis缓存

需要引入Spring Boot的redis starter：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>${VERSION}</version>
</dependency>
```

并在`application.properties`中完成redis的**基本配置**：

```properties
# redis配置
spring.redis.port=6379
spring.redis.host=127.0.0.1
spring.redis.timeout=3m
```

目前有两类信息需要缓存。

首先是token：

```properties
# token缓存
jap.cache.token.type=redis
jap.cache.token.expire-time=3m
```

其次，social策略有单独的缓存：

```properties
# social 缓存类型
jap.social.cache.type=redis
```

## 较为完整的配置

```properties
# basic 基本配置
jap.basic.sso=false
jap.basic.cache-expire-time=12
jap.basic.token-expire-time=12
# sso
jap.sso.cookie-domain=123
jap.sso.cookie-max-age=312321
jap.sso.cookie-name=3123124

# simple

# social

# social 缓存
jap.social.cache.type=default

jap.social.gitee.platform=gitee
jap.social.gitee.state=3242vregv
jap.social.gitee.just-auth-config.client-id=228d103043840b9706f04ad165726a0079c7e0263bf7c11f1205b4054ff094a9
jap.social.gitee.just-auth-config.client-secret=a06ccbdef86d193f25dc240d3e0a9038801ff3cf4c40937f2b58904c8f32a298
jap.social.gitee.just-auth-config.redirect-uri=http://localhost:8080/socail/gitee



# oauth2

# gitee平台
# 授权码方式
jap.oauth2[0].platform=gitee
jap.oauth2[0].response-type=code
jap.oauth2[0].client-id=e9b4f19402d2ccb3375f5be19b9c76738fffe071d6b450a65dc4baa70a7ab752
jap.oauth2[0].client-secret=83bd48fc1ec9807f769c6328304e6222f2290b57d60f346a24976b48a752b794
jap.oauth2[0].grant-type=authorization_code
# The URL in your application where users will be sent after authorization. 来自：https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps#parameters
# 用户授权后，也就是第一阶段gitee给我的服务器返回code的地址。这个参数我感觉我的服务器并没有啥用呀，感觉只有gitee端用得着。可能这是要作检查啥的吧，搞不懂。
# gitee服务器是不关心这个地址的有效性的，因为gitee只对这个地址进行重定向而不是转发，只不过让这个地址带上了参数code，也就是最终请求这个localhost地址的是用户的浏览器！
jap.oauth2[0].callback-url=http://localhost:8080/oauth/gitee/authorization-code
# 获取token的地址
jap.oauth2[0].token-url=https://gitee.com/oauth/token
# 所有的api在：https://gitee.com/api/v5/swagger#/getV5User
jap.oauth2[0].userinfo-url=https://gitee.com/api/v5/user
# 获取user info的方法，GET、POST等。每个platform的不一样，需要查看具体平台的API
jap.oauth2[0].user-info-endpoint-method-type=get
# 获取授权码code的地址
jap.oauth2[0].authorization-url=https://gitee.com/oauth/authorize
jap.oauth2[0].verify-state=false
# password方式
jap.oauth2[1].platform=gitee
jap.oauth2[1].client-id=e9b4f19402d2ccb3375f5be19b9c76738fffe071d6b450a65dc4baa70a7ab752
jap.oauth2[1].client-secret=83bd48fc1ec9807f769c6328304e6222f2290b57d60f346a24976b48a752b794
jap.oauth2[1].grant-type=password
jap.oauth2[1].token-url=https://gitee.com/oauth/token
jap.oauth2[1].callback-url=http://localhost:8080/oauth/gitee/redirect
jap.oauth2[1].userinfo-url=https://gitee.com/api/v5/user
jap.oauth2[1].user-info-endpoint-method-type=get


# GitHub平台
jap.oauth2[2].platform=github
jap.oauth2[2].response-type=code
jap.oauth2[2].client-id=772a23a61ae5ef9df25e
jap.oauth2[2].client-secret=d01a6a44bcf838d4e6d7b572279af59425e35a7a
jap.oauth2[2].grant-type=authorization_code
jap.oauth2[2].callback-url=http://localhost:8080/oauth/github/authorization-code
jap.oauth2[2].authorization-url=https://github.com/login/oauth/authorize
jap.oauth2[2].token-url=https://github.com/login/oauth/access_token
jap.oauth2[2].userinfo-url=https://api.github.com/user
jap.oauth2[2].verify-state=false

#微博
#参考api：https://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI#OAuth2
jap.oauth2[3].platform=weibo
jap.oauth2[3].authorization-url=https://api.weibo.com/oauth2/authorize
# 这个地址在两个地方有用，第一个是访问/oauth2/authorize接口时作为重定向地址，第二个是访问/oauth2/access_token要带上，但是此时感觉没啥卵用，
# 但是微博就要求带上：回调地址，需需与注册应用里的回调地址一致。
jap.oauth2[3].callback-url=http://localhost:8080/oauth/weibo/authenticate-code
jap.oauth2[3].token-url=https://api.weibo.com/oauth2/access_token
jap.oauth2[3].access-token-endpoint-method-type=post
jap.oauth2[3].grant-type=authorization_code
jap.oauth2[3].response-type=code
jap.oauth2[3].client-id=xxx
jap.oauth2[3].client-secret=xxx
jap.oauth2[3].userinfo-url=https://api.weibo.com/2/users/show.json
jap.oauth2[3].user-info-endpoint-method-type=get
jap.oauth2[3].verify-state=true
jap.oauth2[3].state=245rfegfsaf
jap.oauth2[3].revoke-token-url=https://api.weibo.com/oauth2/revokeoauth2



# JapUserService
jap.simple-user-service=my.dong6662.japspringbootstarterdemo.service.SimpleUserServiceImpl
jap.social-user-service=my.dong6662.japspringbootstarterdemo.service.SocialUserServiceImpl
jap.oauth2-user-service=my.dong6662.japspringbootstarterdemo.service.Oauth2UserServiceImpl

# token缓存
jap.cache.token.type=redis
jap.cache.token.expire-time=3m

# redis配置
spring.redis.port=6379
spring.redis.host=127.0.0.1
spring.redis.timeout=3m
```





