package com.shawntime.rpc.test.customer;

import javax.annotation.Resource;

import com.shawntime.rpc.api.IUserService;
import com.shawntime.rpc.api.contract.ProvinceIn;
import com.shawntime.rpc.api.contract.UserIn;
import com.shawntime.rpc.api.contract.UserOut;
import org.springframework.stereotype.Component;

/**
 * Created by shma on 2018/1/19.
 */
@Component
public class LoginService implements ILoginService {

    @Resource
    private IUserService userService;

    public boolean login(String userName, String password, Integer level) {
        UserIn userIn = new UserIn();
        userIn.setUserName(userName);
        userIn.setPassword(password);
        UserOut userInfo = userService.getUserInfo(userIn, level);
        System.out.println("userId:" + userInfo.getUserId()
                + ", age:" + userInfo.getAge()
                + ", address:" + userInfo.getAddress());
        return userInfo != null;
    }

    public int addUserProvince(ProvinceIn in) {
        userService.addUserProvince(in);
        return 1;
    }
}
