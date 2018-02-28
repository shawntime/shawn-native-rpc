package com.shawntime.rpc.provider;

import javax.annotation.Resource;

import com.shawntime.rpc.core.util.JsonHelper;
import com.shawntime.rpc.core.zookeeper.ZookeeperClient;
import com.shawntime.rpc.core.zookeeper.ZookeeperServerConfig;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by shma on 2018/2/12.
 */
@Component
public class ServiceRegisterManager {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegisterManager.class);

    @Resource
    private ZookeeperClient zookeeperClient;

    public void register(Class<?> clazz, ZookeeperServerConfig zookeeperServerConfig) {
        String path = "/" + clazz.getName().replaceAll("\\.", "/");
        String dataPath = path + "/node" + zookeeperServerConfig;
        String data = JsonHelper.serialize(zookeeperServerConfig);
        try {
            zookeeperClient.create(path, CreateMode.PERSISTENT);
            zookeeperClient.create(dataPath, CreateMode.EPHEMERAL, data.getBytes());
        } catch (Exception e) {
            logger.error("zookeeper服务注册失败:[path:{}]", path, e);
        }

    }
}
