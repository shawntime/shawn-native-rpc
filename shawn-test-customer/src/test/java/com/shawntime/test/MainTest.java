package com.shawntime.test;

import com.shawntime.rpc.api.contract.CityIn;
import com.shawntime.rpc.api.contract.ProvinceIn;
import com.shawntime.rpc.api.contract.UserIn;
import com.shawntime.rpc.test.customer.LoginService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shma on 2018/2/13.
 */
public class MainTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-application.xml");
        LoginService loginService = context.getBean(LoginService.class);
        System.out.println(loginService.login("张三", "123456", 1));

        UserIn userIn1 = new UserIn();
        userIn1.setUserName("nihao");
        userIn1.setPassword("123456");

        ProvinceIn in = new ProvinceIn();
        in.setUserIn(userIn1);

        CityIn cityIn1 = new CityIn();
        cityIn1.setCityId(110100);
        cityIn1.setCityName("北京");

        CityIn cityIn2 = new CityIn();
        cityIn2.setCityId(210100);
        cityIn2.setCityName("上海");

        CityIn[] cityIns = new CityIn[2];
        cityIns[0] = cityIn1;
        cityIns[1] = cityIn2;

        in.setCityIns(cityIns);

        System.out.println(loginService.addUserProvince(in));
    }
}
