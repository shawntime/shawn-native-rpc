package com.shawntime.rpc.api.contract;

/**
 * Created by shma on 2018/1/22.
 */
public class ProvinceIn {

    private CityIn[] cityIns;

    private com.shawntime.rpc.api.contract.UserIn UserIn;

    public CityIn[] getCityIns() {
        return cityIns;
    }

    public void setCityIns(CityIn[] cityIns) {
        this.cityIns = cityIns;
    }

    public com.shawntime.rpc.api.contract.UserIn getUserIn() {
        return UserIn;
    }

    public void setUserIn(com.shawntime.rpc.api.contract.UserIn userIn) {
        UserIn = userIn;
    }
}
