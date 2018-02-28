package com.shawntime.rpc.provider.config;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.shawntime.rpc.core.RpcServiceScanner;
import com.shawntime.rpc.core.zookeeper.ZookeeperServerConfig;
import com.shawntime.rpc.provider.ServiceConfigManager;
import com.shawntime.rpc.provider.ServiceRegisterManager;
import org.springframework.context.annotation.Configuration;

/**
 * 单独的listener，需要在web.xml进行配置，服务注册
 */
@Configuration
public class ServiceRegisterConfig {

    @Resource
    private ServiceRegisterManager serviceRegisterManager;

    @PostConstruct
    public void onApplicationEvent() {
        List<Class<?>> clazzList = RpcServiceScanner.scan();
        ZookeeperServerConfig zookeeperServerConfig = new ZookeeperServerConfig();
        zookeeperServerConfig.setIp(ServiceConfigManager.getIp());
        zookeeperServerConfig.setPort(ServiceConfigManager.getPort());
        clazzList.forEach(clazz -> {
            serviceRegisterManager.register(clazz, zookeeperServerConfig);
        });

    }
}