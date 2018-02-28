package com.shawntime.rpc.core.exception;

/**
 * Created by shma on 2018/1/31.
 */
public class RpcException extends RuntimeException {

    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
