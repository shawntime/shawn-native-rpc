package com.shawntime.rpc.core.zookeeper;

/**
 * Created by shma on 2018/1/24.
 */
public class ZookeeperServerConfig {

    private String ip;

    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
