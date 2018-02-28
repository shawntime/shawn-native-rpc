package com.shawntime.rpc.provider;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.shawntime.rpc.core.Const;
import com.shawntime.rpc.core.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shma on 2018/2/12.
 */
public final class ServiceConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ServiceConfigManager.class);

    private ServiceConfigManager() {
        // --
    }

    public static final String getIp() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("获取本地ip地址异常:[msg:{}]", e.getMessage(), e);
        }
        return "127.0.0.1";
    }

    public static final int getPort() {
        return Integer.parseInt(PropertyUtil.getProperty(Const.SERVICE_PORT));
    }
}
