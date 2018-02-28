package com.shawntime.rpc.customer.config;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.shawntime.rpc.core.RpcServiceScanner;
import com.shawntime.rpc.core.zookeeper.ZookeeperServerConfig;
import com.shawntime.rpc.customer.ServiceFindManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 单独的listener，需要在web.xml进行配置，服务注册
 */
@Configuration
public class CustomerRegisterConfig {

    @Resource
    private ServiceFindManager serviceFindManager;

    @PostConstruct
    public void onApplicationEvent() {
        List<Class<?>> clazzList = RpcServiceScanner.scan();
        clazzList.forEach(clazz -> {
            try {
                serviceFindManager.listener(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}