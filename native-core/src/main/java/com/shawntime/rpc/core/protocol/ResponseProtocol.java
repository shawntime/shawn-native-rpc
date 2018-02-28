package com.shawntime.rpc.core.protocol;

/**
 * Created by shma on 2018/1/31.
 */
public class ResponseProtocol {

    private String clazz;

    private Object value;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
