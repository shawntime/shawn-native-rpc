package com.shawntime.rpc.core.serialize;

import com.shawntime.rpc.core.protocol.RequestProtocol;
import com.shawntime.rpc.core.protocol.ResponseProtocol;

/**
 * Created by shma on 2018/1/31.
 */
public interface Parse {

    /**
     * 服务端接收到的请求内容反序列化为RequestProtocol对象
     */
    RequestProtocol requestParse(String requestContent);

    /**
     * 客户端接收到响应内容反序列号为Object对象
     */
    ResponseProtocol responseParse(String responseContent);
}
