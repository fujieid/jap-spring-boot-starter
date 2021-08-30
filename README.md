# 项目名称

**开发 springboot starter 依赖包**

**社区：JustAuth**

地址：[项目六-开发-springboot-starter-依赖包](https://justauth.wiki/resource/summer2021.html#%E9%A1%B9%E7%9B%AE%E5%85%AD-%E5%BC%80%E5%8F%91-springboot-starter-%E4%BE%9D%E8%B5%96%E5%8C%85)

## 任务：

- 开发依赖包：`jap-simple-springboot-starter`、`jap-oauth2-springboot-starter`、`jap-social-springboot-starter`、`jap-oidc-springboot-starter`、`jap-springboot-starter`
- 编写相关的使用文档
- 编写测试用例

## 开发日志

##### 2021/7/13

三种调用方式：

1. 传入不同的策略，`Strategy.SIMPLE`，得到相应的response，这样做是为了屏蔽掉编程创建`JapConfig`和`AuthenticateConfig`。

   ```java
   JapResponse simple = japStrategyFactory.authenticate(Strategy.SIMPLE, userService, request, response);
   ```

2. 传入目标strategy对应的class对象，得到相应实例，就可以调用该策略的各种方法。这种方式比较适用于`SocialStrategy`，因为其不仅有`authenticate()`，还有`refreshToken()`等，而其他策略基本只有`authenticate()`方法可以调用。

   ```java
   SocialStrategy socialStrategy = japStrategyFactory.create(SocialStrategy.class, userService);
   return socialStrategy.authenticate(japProperties.getSocial(), request, response);
   ```



##### 2021/7/17

用一种授权方式登录后会在session上记录request，所以集成多种登录策略需要进一步思考。

分析一下SimpleStrategy#authenticate()源码：

```java

 @Override
    public JapResponse authenticate(AuthenticateConfig config, HttpServletRequest request, HttpServletResponse response) {
        // Convert AuthenticateConfig to SimpleConfig
        try {
            //确保AuthenticateConfig实例的类型为SimpleConfig
            this.checkAuthenticateConfig(config, SimpleConfig.class);
        } catch (JapException e) {
            return JapResponse.error(e.getErrorCode(), e.getErrorMessage());
        }
        SimpleConfig simpleConfig = (SimpleConfig) config;

        JapUser sessionUser = null;
        try {
            //里面会调用父类AbstractJapStrategy的checkSession()，看看这个用户是否已经登录了
            //用session和cookie保存登录状态
            sessionUser = this.checkSessionAndCookie(simpleConfig, request, response);
        } catch (JapException e) {
            return JapResponse.error(e.getErrorCode(), e.getErrorMessage());
        }
        if (null != sessionUser) {
            return JapResponse.success(sessionUser);
        }
		//走到这里表示没有登陆过
        UsernamePasswordCredential credential = this.doResolveCredential(request, simpleConfig);
        if (null == credential) {
            return JapResponse.error(JapErrorCode.MISS_CREDENTIALS);
        }
        JapUser user = japUserService.getByName(credential.getUsername());
        if (null == user) {
            return JapResponse.error(JapErrorCode.NOT_EXIST_USER);
        }

        boolean valid = japUserService.validPassword(credential.getPassword(), user);
        if (!valid) {
            return JapResponse.error(JapErrorCode.INVALID_PASSWORD);
        }

        return this.loginSuccess(simpleConfig, credential, user, request, response);
    }
```

`AbstractJapStrategy`中有这么一个构造器值得注意：

```java
public AbstractJapStrategy(JapUserService japUserService, JapConfig japConfig, JapUserStore japUserStore, JapCache japCache) {
        this.japUserService = japUserService;
        if (japConfig.isSso()) {
            // init Kisso config
            JapSsoHelper.initKissoConfig(japConfig.getSsoConfig());
        }
        this.japContext = new JapContext(japUserStore, japCache, japConfig);

        JapAuthentication.setContext(this.japContext);//这一个地方非常关键

        // Update the cache validity period
        JapCacheConfig.timeout = japConfig.getCacheExpireTime();
    }
```

`JapAuthentication.setContext(this.japContext);`值得深入考虑。首先这是一个static对象，也就是一个项目中只有一个`JapAuthentication`对象，其中只有一个参数`JapContext`，但是每次声明一个strategy的时候都会调用上面这个构造器，这样`JapAuthentication`里面的`JapContext`就有很大问题，为最后一个创建的stragegy产生的。

所以现在要解决的问题是，采用同一个`JapContext`，这样里边的`japUserStore`,`japCache`,`japConfig`才不会乱来。

`JapAuthentication`很重要，除了从context属性外，其中的checkUser等用来检查当前用户是否登录，用Session记录登录状态。



##### 2021/7/22

[如何正确控制springboot中bean的加载顺序总结](https://blog.csdn.net/qianshangding0708/article/details/107373538)

[条件注解 @ConditionalOnBean 的正确使用姿势](https://blog.csdn.net/forezp/article/details/84313907)，这种方式针对的是bean不在同一个@Configuration注解的类下



[SpringMVC之RequestContextHolder分析](https://www.cnblogs.com/shuilangyizu/p/8621669.html)，讨论了request和response怎么和当前请求挂钩

##### 2021/7/23

实现了三种注入方式，以`socialStrategy`为例：

```java
@Autowired
JapStrategyFactory japStrategyFactory;
@Autowired
JapProperties japProperties;
@Autowired
SocialStrategy socialStrategy;

//方式一，这种方式采用了ServletRequestAttributes获取当前线程绑定的request和response。不是很确定，但应该没有线程安全问题。
japStrategyFactory.authenticate(Strategy.SOCIAL);
//方式二
japStrategyFactory.authenticate(Strategy.SOCIAL, japProperties.getSocial(), request, response);
//方式三
socialStrategy.authenticate(japProperties.getSocial(), request, response);
```



##### 2021/7/28

- 完成了`Oauth2Strategy`和`OidcStrategy`的注入
- 避免了创建4种strategy的时候没有指定JapUserService。如果strategy没有指定的JapUserService，则传入`DefaultJapUserService`为JapUserService的空实现。可以过后调用`JapStrategyFactory`的`authenticate(Strategy strategy,JapUserService japUserService)`方法传入japUserService。
- 考虑公共的authenticate方法都不显式传递request和response。

todo：

考虑JapCache和JapStore继承redis，采用Jedis或Redission。



##### 2021/8/10

实现了`JapCache`接口的redis实现，但是还没有测试oauth2策略。目前找到关于这种策略的授权过程的文章：[OAuth 2.0 的四种方式](https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html)。



##### 2021/8/13

重新封装了`JapTemplate`，将调用方式更加简化：

```java
@Autowired
JapProperties japProperties;
@Autowired
SimpleStrategy simpleStrategy;
@Autowired
JapTemplate japTemplate;
```

方式一：传入平台名称即可

```java
JapResponse japResponse = japTemplate.social("gitee");
```

方式二：

```java
JapResponse japResponse = 
    socialStrategy.authenticate(japProperties.getSocial().get("gitee"), request, response);
```

properties配置文件的用例如下：

```properties
# basic 基本配置
jap.basic.sso=true
jap.basic.cache-expire-time=13
jap.basic.token-expire-time=12

# sso
jap.sso.cookie-domain=123
jap.sso.cookie-max-age=312321
jap.sso.cookie-name=3123124

# social
# gitee
jap.social.gitee.platform=gitee
jap.social.gitee.state=3242vregv
jap.social.gitee.just-auth-config.client-id=fda07d40917d6f040822d3fa01c8c75588c67d63132c3ddc5c66990342115ba9
jap.social.gitee.just-auth-config.client-secret=016f88fbff2d178263c4060c46168f4937153120a310adc21980e7838b76e833
jap.social.gitee.just-auth-config.redirect-uri=https://sso.jap.com:8443/social/login/gitee
# github
jap.social.github.platform=github
jap.social.github.state=xxxx

# oauth
# gitee
jap.oauth.gitee.platform=gitee
jap.oauth.gitee.client-id=e9b4f19402d2cwcb3375f5bfffe071d6b4nwa65dc4baa70a7ab752
jap.oauth.gitee.client-secret=83brd48fc1we4e6222f229nub57d60f346a24976b48a752b794
jap.oauth.gitee.callback-url=https://gitee.com/login
jap.oauth.gitee.token-url=http://127.0.0.1:8080/oauth/token
jap.oauth.gitee.userinfo-url=http://127.0.0.1:8080/oauth/userInfo
jap.oauth.gitee.authorization-url=https://gitee.com/oauth/authorize
jap.oauth.gitee.grant-type=authorization_code
jap.oauth.gitee.response-type=code
```

##### 2021/8/29

解决了多模块互相依赖的问题，用`mvn install` 来将自己写得maven项目发布到本地maven仓库，参考了这篇文章：[Maven本地子模块互相依赖](https://juejin.cn/post/6844904038589267981)。但是后面发生了报错，解决方案为：[Maven报错：The packaging for this project did not assign a file to the build artifact](https://blog.csdn.net/gao_zhennan/article/details/89713407)，里面也提到了lifecycle和plugin的区别，执行一个lifecycle，比如`mvn install`，会把它之前的21个阶段都给执行了，而插件`mvn install:install`只会执行lifecycle中对应的install这一个phase。

之后有遇到了一个报错：*repackage failed: Unable to find main class*，找到的解决方案是这样的：[idea中maven打包工具类 repackage failed: Unable to find main class](https://blog.csdn.net/ybb_ymm/article/details/109283783)



## TODO LIST

1. 是否需要提供显式传入service的方法，比如这样：

   ```java
   japtemplate.simple(simpleservice);
   ```

2. 将redis用作缓存是否需要考虑并发控制

3. 关于如何获得每一种策略的`JapUserService`实现类，在`JapAutoConfiguration#getUserService(...)`的注释上写了第三种，考虑一下是否有必要实现。

   > 3.（考虑是否实现）以SimpleStrategy为例，将service类的名称命名为{@code SimpleUserService}或{@code SimpleUserServiceImpl}

4. 三个缓存接口**`JapUserStore`**、**`JapCache`**、**`AuthStateCache`**，如果引入了redis，那么它们全部都采用redis作为缓存，还是通过配置文件单独确定各自的缓存类型？

5. 还是关于缓存接口，用redisTemplate，但是每一种缓存的key虽然都是String类型，但是value是不一样的，有String,JapUser,Serialze，这个怎么搞？每一个都自定义一个redisTemplate？还是都通用一个`RedisTemplate<String,Object>`

6. >  最后 starter 的封装比较考验对 Spring Boot 自动装配的理解，建议后面浩东同学可以再深入的研究下。目前开发的 5 个 starter 通过**配置化**的形式进行选择，但是后期希望可以进行**模块化**的形式进行引入。



#### 三个需要用redis实现的接口

**接口`JapUserStore`**

两个实现类：`SessionJapUserStore`、`SsoJapUserStore`（严格说这个类是继承了SessionJapUserStore的）

主要在`AbstractJapStrategy`。以`SessionJapUserStore`为例，存储的是japUser实例，用session来存放登录信息，

然而四个策略类并没有提供有这个接口参数的构造器，大概是框架不欢迎自定义实现。

**接口`JapCache`**

默认实现类：`JapLocalCache`。里边用到了AQS作为锁的实现，有点意思，但是不难。采用的数据结构是map。

用处：

1. `JapTokenHelper`，里面的方法们只需要两个参数，userId和token，也就是在这里japcache的用处是将userId作为key，token作为value。
2. 四种策略类的构造器都有用到



**接口`AuthStateCache`**

实现类`AuthDefaultStateCache`。只有socialstrategy的构造器上有用到。

```java
public SocialStrategy(JapUserService japUserService, JapConfig japConfig, JapCache japCache, AuthStateCache authStateCache) {
        this(japUserService, japConfig, japCache);
        this.authStateCache = authStateCache;
    }
```

