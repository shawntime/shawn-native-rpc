package com.shawntime.rpc.customer;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.google.common.collect.Maps;
import com.shawntime.rpc.core.util.JsonHelper;
import com.shawntime.rpc.core.zookeeper.ZookeeperClient;
import com.shawntime.rpc.core.zookeeper.ZookeeperServerConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Created by shma on 2018/2/12.
 */
public class ServiceFindManager {

    private ZookeeperClient zookeeperClient;

    private static final Map<Class, List<ZookeeperServerConfig>> configMap = Maps.newHashMap();

    public ZookeeperServerConfig getZookeeperServerConfig(Class clazz) throws Exception {
        List<ZookeeperServerConfig> zookeeperServerConfigs = configMap.get(clazz);
        if (CollectionUtils.isEmpty(zookeeperServerConfigs)) {
            zookeeperServerConfigs = updateServerConfig(clazz);
        }
        if (CollectionUtils.isEmpty(zookeeperServerConfigs)) {
            return null;
        }
        int index = (int)(Math.random() * zookeeperServerConfigs.size());
        return zookeeperServerConfigs.get(index);
    }

    public void listener(Class clazz) throws Exception {
        zookeeperClient.listener(clazz, this::updateServerConfig);
    }

    private List<ZookeeperServerConfig> updateServerConfig(Class clazz) {
        String path = "/" + clazz.getName().replaceAll("\\.", "/");
        List<String> childrenList;
        try {
            childrenList = zookeeperClient.findChildrenList(path);
            if (CollectionUtils.isEmpty(childrenList)) {
                return null;
            }
            List<ZookeeperServerConfig> configList = childrenList.stream()
                    .map(childrenPath -> {
                        try {
                            byte[] data = zookeeperClient.getData(path + "/" + childrenPath);
                            return JsonHelper.deSerialize(new String(data), ZookeeperServerConfig.class);
                        } catch (Exception e) {
                            return null;
                        }
                    }).filter(t -> t != null).collect(Collectors.toList());
            configMap.put(clazz, configList);
            return configList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ZookeeperClient getZookeeperClient() {
        return zookeeperClient;
    }

    public void setZookeeperClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }
}
