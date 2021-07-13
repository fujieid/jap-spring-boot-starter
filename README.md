# 项目名称

**开发 springboot starter 依赖包**

**社区：JustAuth**

地址：[项目六-开发-springboot-starter-依赖包](https://justauth.wiki/resource/summer2021.html#%E9%A1%B9%E7%9B%AE%E5%85%AD-%E5%BC%80%E5%8F%91-springboot-starter-%E4%BE%9D%E8%B5%96%E5%8C%85)

## 任务：

- 开发依赖包：`jap-simple-springboot-starter`、`jap-oauth2-springboot-starter`、`jap-social-springboot-starter`、`jap-oidc-springboot-starter`、`jap-springboot-starter`
- 编写相关的使用文档
- 编写测试用例

## 开发日志

**2021/7/13**

两种调用方式：

1. 传入不同的策略，`Strategy.SIMPLE`，得到相应的response，这样做是为了屏蔽掉编程创建`JapConfig`和`AuthenticateConfig`。

   ```java
   JapResponse simple = japStrategyFactory.authenticate(Strategy.SIMPLE, userService, request, response);
   ```

2. 传入目标strategy对应的class对象，得到相应实例，就可以调用该策略的各种方法。这种方式比较适用于`SocialStrategy`，因为其不仅有`authenticate()`，还有`refreshToken()`等，而其他策略基本只有`authenticate()`方法可以调用。

   ```java
   SocialStrategy socialStrategy = japStrategyFactory.create(SocialStrategy.class, userService);
   return socialStrategy.authenticate(japProperties.getSocial(), request, response);
   ```

   

