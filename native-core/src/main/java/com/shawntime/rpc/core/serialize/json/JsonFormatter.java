package com.shawntime.rpc.core.serialize.json;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.shawntime.rpc.core.protocol.ParameterEntity;
import com.shawntime.rpc.core.protocol.RequestProtocol;
import com.shawntime.rpc.core.serialize.Formatter;
import com.shawntime.rpc.core.util.JsonHelper;

/**
 * Created by shma on 2018/1/31.
 */
public class JsonFormatter implements Formatter {

    @Override
    public String requestFormatter(Class clazz, Object[] args, Method method) {
        RequestProtocol protocol = new RequestProtocol();
        protocol.setClazz(clazz.getName());
        protocol.setMethodName(method.getName());
        protocol.setReturnClazz(method.getReturnType().getName());
        ParameterEntity[] parameterEntitys = null;
        if (args != null) {
            parameterEntitys = new ParameterEntity[args.length];
            for (int i = 0; i < args.length; ++i) {
                Object arg = args[i];
                ParameterEntity parameterEntity = new ParameterEntity();
                parameterEntity.setClazz(arg.getClass().getName());
                parameterEntity.setValue(arg);
                parameterEntitys[i] = parameterEntity;
            }
        }
        protocol.setArgs(parameterEntitys);
        return JsonHelper.serialize(protocol);
    }

    @Override
    public String responseBodyFormatter(Object object) {
        return JsonHelper.serialize(object);
    }
}
