package com.shawntime.rpc.core.zookeeper;

import java.util.List;
import java.util.function.Function;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

/**
 * Created by shma on 2018/1/24.
 */
public class ZookeeperClient {

    private CuratorFramework curatorFramework;

    public ZookeeperClient(String connectUrl) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectString(connectUrl)
                .namespace("rpc")
                .connectionTimeoutMs(3000)
                .build();
        curatorFramework.start();
    }

    public boolean isExist(String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        return stat != null;
    }

    public void create(String path, CreateMode createMode) throws Exception {
        if (isExist(path)) {
            return;
        }
        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(createMode)
                .forPath(path);
    }

    public void create(String path, CreateMode createMode, byte[] data) throws Exception {
        if (isExist(path)) {
            return;
        }
        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(createMode)
                .forPath(path, data);
    }

    public List<String> findChildrenList(String path) throws Exception {
        return curatorFramework.getChildren().forPath(path);
    }

    public byte[] getData(String path) throws Exception {
        return curatorFramework.getData().forPath(path);
    }

    public void listener(Class clazz, Function<Class, List<ZookeeperServerConfig>> updateServerConfig) throws Exception {
        String path = "/" + clazz.getName().replaceAll("\\.", "/");
        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, path, true);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event)
                    throws Exception {
                updateServerConfig.apply(clazz);
            }
        });
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    }
}
