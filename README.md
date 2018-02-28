github：https://github.com/shawntime/shawn-native-rpc

#### RPC过程详解
![image.png](http://upload-images.jianshu.io/upload_images/2511194-657aa186a0b9c6d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](http://upload-images.jianshu.io/upload_images/2511194-e8b12419c0f63b87.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 实现内容
* json序列化
* zookeeper实现服务端服务注册和客户端的服务发现和负载均衡
* 自动扫描服务端所提供的接口，并注册到zookeeper中
* 自动生成客户端动态代理对象
* socket实现通讯

#### 模块说明
* native-core：公共包：注解、异常定义、通讯协议、序列化、zookeeper客户端封装以及包扫描，配置加载等工具类
* native-customer：调用方需引入的模块，实现自动生成客户端动态代理对象，zk的服务发现和负载均衡，以及通信实现
* native-provider：服务提供方需引入的模块，实现了接口客户端请求并处理，zk的服务注册
* native-interface：对外提供服务的接口定义
* shawn-test-provider：提供方demo
* shawn-test-customer：调用方demo

#### 配置
##### provider配置

> 配置naive-rpc.properties文件
```
scanPackage=com.shawntime.rpc.api
servicePort=11001
```
* scanPackage: 包扫描路径，自动创建包路径下接口动态代理类
* servicePort: 服务端监听端口号

> 配置spring

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:cache="http://www.springframework.org/schema/cache"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.1.xsd
       http://www.springframework.org/schema/cache
       http://www.springframework.org/schema/cache/spring-cache-4.1.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="com.shawntime"/>
    <context:annotation-config/>
    <aop:aspectj-autoproxy proxy-target-class="true"/>

    <!-- 配置消息格式化方式 -->
    <bean id="jsonFormatter" class="com.shawntime.rpc.core.serialize.json.JsonFormatter" />
    <bean id="jsonParse" class="com.shawntime.rpc.core.serialize.json.JsonParse" />

    <!-- 配置服务类，该处使用socket处理请求 -->
    <bean id="rpcProvider" class="com.shawntime.rpc.provider.service.socket.SocketRpcProvider">
        <property name="formatter" ref="jsonFormatter" />
        <property name="parse" ref="jsonParse" />
    </bean>

    <!-- 配置zk服务器集群信息，作服务注册 -->
    <bean id="zookeeperClient" class="com.shawntime.rpc.core.zookeeper.ZookeeperClient">
        <constructor-arg value="127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183" />
    </bean>
</beans>
```

##### customer配置

> 配置naive-rpc.properties文件
```
scanPackage=com.shawntime.rpc.api
```
* scanPackage: 包扫描路径，自动创建包路径下接口动态代理类

> 配置spring

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:cache="http://www.springframework.org/schema/cache"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.1.xsd
       http://www.springframework.org/schema/cache
       http://www.springframework.org/schema/cache/spring-cache-4.1.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="com.shawntime"/>
    <context:annotation-config/>
    <aop:aspectj-autoproxy proxy-target-class="true"/>

    <!-- 配置消息格式化方式 -->
    <bean id="jsonFormatter" class="com.shawntime.rpc.core.serialize.json.JsonFormatter" />
    <bean id="jsonParse" class="com.shawntime.rpc.core.serialize.json.JsonParse" />
    
    <!-- 客户端处理配置 -->
    <bean id="socketInvoke" class="com.shawntime.rpc.customer.invoke.socket.SocketInvoke" />
    
    <bean id="beanNameGenerator" class="org.springframework.context.annotation.AnnotationBeanNameGenerator" />

    <!-- 客户端动态代理注册类 -->
    <bean id="customBeanDefinitionRegistryPostProcessor"
          class="com.shawntime.rpc.customer.config.CustomBeanDefinitionRegistryPostProcessor">
        <property name="invoke" ref="socketInvoke" />
        <property name="formatter" ref="jsonFormatter" />
        <property name="parse" ref="jsonParse" />
        <property name="beanNameGenerator" ref="beanNameGenerator" />
        <property name="serviceFindManager" ref="serviceFindManager" />
    </bean>

    <!-- 服务发现和负载均衡 -->
    <bean id="serviceFindManager" class="com.shawntime.rpc.customer.ServiceFindManager">
        <property name="zookeeperClient" ref="zookeeperClient" />
    </bean>

    <!-- 配置zk服务器集群信息，作服务注册 -->
    <bean id="zookeeperClient" class="com.shawntime.rpc.core.zookeeper.ZookeeperClient">
        <constructor-arg value="127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183" />
    </bean>
</beans>
```

### Demo
* 接口定义，用RpcService注解
```java
@RpcService
public interface IUserService {

    UserOut getUserInfo(UserIn userIn, Integer level);

    void addUserProvince(ProvinceIn in);
}
```

* 提供方

```java
package com.shawntime.test.provider;

import com.shawntime.rpc.api.IUserService;
import com.shawntime.rpc.api.contract.ProvinceIn;
import com.shawntime.rpc.api.contract.UserIn;
import com.shawntime.rpc.api.contract.UserOut;
import org.springframework.stereotype.Component;

@Component
public class UserService implements IUserService {

    public UserOut getUserInfo(UserIn userIn, Integer level) {
        UserOut userOut = new UserOut();
        if (level > 0) {
            userOut.setUserId(1);
            userOut.setAddress("安徽芜湖");
            userOut.setAge(18);
            userOut.setUserName(userIn.getUserName());
        } else {
            userOut.setUserId(2);
            userOut.setAddress("河北保定");
            userOut.setAge(19);
            userOut.setUserName(userIn.getUserName());
        }
        return userOut;
    }

    public void addUserProvince(ProvinceIn provinceIn) {
        System.out.println(provinceIn);
    }
}

```

* 调用方

```java
@Component
public class LoginService implements ILoginService {

    @Resource
    private IUserService userService;

    public boolean login(String userName, String password, Integer level) {
        UserIn userIn = new UserIn();
        userIn.setUserName(userName);
        userIn.setPassword(password);
        UserOut userInfo = userService.getUserInfo(userIn, level);
        System.out.println("userId:" + userInfo.getUserId()
                + ", age:" + userInfo.getAge()
                + ", address:" + userInfo.getAddress());
        return userInfo != null;
    }

    public int addUserProvince(ProvinceIn in) {
        userService.addUserProvince(in);
        return 1;
    }
}
```



