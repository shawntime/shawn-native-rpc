package com.shawntime.rpc.core.protocol;

/**
 * Created by shma on 2018/1/31.
 */
public class RequestProtocol {
    
    private String clazz;

    private String methodName;

    private ParameterEntity[] args;

    private String returnClazz;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ParameterEntity[] getArgs() {
        return args;
    }

    public void setArgs(ParameterEntity[] args) {
        this.args = args;
    }

    public String getReturnClazz() {
        return returnClazz;
    }

    public void setReturnClazz(String returnClazz) {
        this.returnClazz = returnClazz;
    }
}
