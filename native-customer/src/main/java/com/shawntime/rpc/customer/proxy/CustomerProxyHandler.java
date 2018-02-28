package com.shawntime.rpc.customer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.annotation.Resource;

import com.shawntime.rpc.core.exception.RpcException;
import com.shawntime.rpc.core.protocol.ResponseProtocol;
import com.shawntime.rpc.core.serialize.Formatter;
import com.shawntime.rpc.core.serialize.Parse;
import com.shawntime.rpc.core.util.ClassUtil;
import com.shawntime.rpc.core.util.JsonHelper;
import com.shawntime.rpc.core.zookeeper.ZookeeperClient;
import com.shawntime.rpc.core.zookeeper.ZookeeperServerConfig;
import com.shawntime.rpc.customer.ServiceFindManager;
import com.shawntime.rpc.customer.invoke.Invoke;

/**
 * Created by shma on 2018/2/12.
 */
public class CustomerProxyHandler implements InvocationHandler {

    private Invoke invoke;

    private Formatter formatter;

    private Parse parse;

    private Class clazz;

    private ServiceFindManager serviceFindManager;

    public CustomerProxyHandler() {
        super();
    }

    public CustomerProxyHandler(Invoke invoke, Formatter formatter, Parse parse, Class clazz, ServiceFindManager
            serviceFindManager) {
        this.invoke = invoke;
        this.formatter = formatter;
        this.parse = parse;
        this.clazz = clazz;
        this.serviceFindManager = serviceFindManager;
    }

    public Object create() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if ("toString".equals(methodName)) {
            return proxy.toString();
        }
        if ("hashCode".equals(methodName)) {
            return null;
        }
        ZookeeperServerConfig serverConfig = serviceFindManager.getZookeeperServerConfig(clazz);
        if (serverConfig == null) {
            throw new RpcException("服务发现异常");
        }
        Class clazz = proxy.getClass().getInterfaces()[0];
        String requestBody = formatter.requestFormatter(clazz, args, method);
        if (method.getReturnType() == Void.class) {
            this.invoke.invoke(requestBody, serverConfig);
            return null;
        } else {
            String responseBody = this.invoke.invoke(requestBody, serverConfig);
            if ("error".equals(responseBody)) {
                return null;
            }
            ResponseProtocol responseProtocol = parse.responseParse(responseBody);
            if (responseProtocol.getClazz().equalsIgnoreCase("void")) {
                return null;
            }
            Class<?> aClass = ClassUtil.forNameWithPrimitive(responseProtocol.getClazz());
            return JsonHelper.deSerialize(responseProtocol.getValue().toString(), aClass);
        }
    }

    public Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Invoke invoke) {
        this.invoke = invoke;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public Parse getParse() {
        return parse;
    }

    public void setParse(Parse parse) {
        this.parse = parse;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public ServiceFindManager getServiceFindManager() {
        return serviceFindManager;
    }

    public void setServiceFindManager(ServiceFindManager serviceFindManager) {
        this.serviceFindManager = serviceFindManager;
    }
}
