package com.shawntime.rpc.core.protocol;

/**
 * 方法请求参数封装
 */
public class ParameterEntity {

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
