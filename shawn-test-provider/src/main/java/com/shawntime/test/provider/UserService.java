package com.shawntime.test.provider;

import com.shawntime.rpc.api.IUserService;
import com.shawntime.rpc.api.contract.ProvinceIn;
import com.shawntime.rpc.api.contract.UserIn;
import com.shawntime.rpc.api.contract.UserOut;
import org.springframework.stereotype.Component;

/**
 * Created by shma on 2018/2/13.
 */
@Component
public class UserService implements IUserService {

    public UserOut getUserInfo(UserIn userIn, Integer level) {
        UserOut userOut = new UserOut();
        if (level > 0) {
            userOut.setUserId(1);
            userOut.setAddress("安徽芜湖");
            userOut.setAge(18);
            userOut.setUserName(userIn.getUserName());
        } else {
            userOut.setUserId(2);
            userOut.setAddress("河北保定");
            userOut.setAge(19);
            userOut.setUserName(userIn.getUserName());
        }
        return userOut;
    }

    public void addUserProvince(ProvinceIn provinceIn) {
        System.out.println(provinceIn);
    }
}
