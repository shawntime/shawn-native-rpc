package com.shawntime.rpc.test.customer;

import com.shawntime.rpc.api.contract.ProvinceIn;

/**
 * Created by shma on 2018/1/19.
 */
public interface ILoginService {

    boolean login(String userName, String password, Integer level);

    int addUserProvince(ProvinceIn in);
}
