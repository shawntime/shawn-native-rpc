package com.shawntime.rpc.api.contract;

/**
 * Created by shma on 2018/1/18.
 */
public class UserIn {

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
