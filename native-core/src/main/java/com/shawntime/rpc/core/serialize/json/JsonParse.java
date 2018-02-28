package com.shawntime.rpc.core.serialize.json;

import com.alibaba.fastjson.TypeReference;
import com.shawntime.rpc.core.protocol.RequestProtocol;
import com.shawntime.rpc.core.protocol.ResponseProtocol;
import com.shawntime.rpc.core.serialize.Parse;
import com.shawntime.rpc.core.util.JsonHelper;

/**
 * Created by shma on 2018/1/31.
 */
public class JsonParse implements Parse {

    @Override
    public RequestProtocol requestParse(String requestContent) {
        return JsonHelper.deSerialize(requestContent, new TypeReference<RequestProtocol>() {}.getType());
    }

    @Override
    public ResponseProtocol responseParse(String responseContent) {
        return JsonHelper.deSerialize(responseContent, new TypeReference<ResponseProtocol>() {}.getType());
    }
}
