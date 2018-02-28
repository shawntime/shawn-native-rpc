package com.shawntime.rpc.core.serialize;

import java.lang.reflect.Method;

public interface Formatter {

    /**
     * 客户端请求消息体序列化
     */
    String requestFormatter(Class clazz, Object[] args, Method method);

    /**
     * 服务端处理响应结果序列化
     */
    String responseBodyFormatter(Object object);
}
