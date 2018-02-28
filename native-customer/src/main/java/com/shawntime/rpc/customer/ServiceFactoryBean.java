package com.shawntime.rpc.customer;

import com.shawntime.rpc.core.serialize.Formatter;
import com.shawntime.rpc.core.serialize.Parse;
import com.shawntime.rpc.customer.invoke.Invoke;
import com.shawntime.rpc.customer.proxy.CustomerProxyHandler;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by shma on 2018/2/13.
 */
public class ServiceFactoryBean implements FactoryBean {

    private Invoke invoke;

    private Formatter formatter;

    private Parse parse;

    private Class clazz;

    private ServiceFindManager serviceFindManager;

    public ServiceFactoryBean() {
        super();
    }

    public ServiceFactoryBean(Invoke invoke, Formatter formatter,
                              Parse parse, Class clazz, ServiceFindManager serviceFindManager) {
        this.invoke = invoke;
        this.formatter = formatter;
        this.parse = parse;
        this.clazz = clazz;
        this.serviceFindManager = serviceFindManager;
    }

    @Override
    public Object getObject() throws Exception {
        CustomerProxyHandler customerProxyHandler = new CustomerProxyHandler();
        customerProxyHandler.setClazz(clazz);
        customerProxyHandler.setParse(parse);
        customerProxyHandler.setFormatter(formatter);
        customerProxyHandler.setInvoke(invoke);
        customerProxyHandler.setServiceFindManager(serviceFindManager);
        return customerProxyHandler.create();
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
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
