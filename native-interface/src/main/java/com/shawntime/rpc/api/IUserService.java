package com.shawntime.rpc.api;

import com.shawntime.rpc.api.contract.ProvinceIn;
import com.shawntime.rpc.api.contract.UserIn;
import com.shawntime.rpc.api.contract.UserOut;
import com.shawntime.rpc.core.annotation.RpcService;

/**
 * Created by shma on 2018/1/18.
 */
@RpcService
public interface IUserService {

    UserOut getUserInfo(UserIn userIn, Integer level);

    void addUserProvince(ProvinceIn in);
}
