package com.shawntime.rpc.provider;

import com.shawntime.rpc.provider.service.AbstractRpcProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shma on 2018/2/13.
 */
public class MainTest {

    public static void main(String[] args) {
//        PropertyConfigurator.configure("log4j.properties");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-application.xml");
        AbstractRpcProvider provider = context.getBean(AbstractRpcProvider.class);
        provider.start();
    }
}
