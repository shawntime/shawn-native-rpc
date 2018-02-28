package com.shawntime.rpc.customer.invoke;

import java.io.IOException;

import com.shawntime.rpc.core.protocol.ResponseProtocol;
import com.shawntime.rpc.core.zookeeper.ZookeeperServerConfig;

/**
 * Created by shma on 2018/2/12.
 */
public interface Invoke {

    String invoke(String requestBody, ZookeeperServerConfig serverConfig) throws IOException;
}
