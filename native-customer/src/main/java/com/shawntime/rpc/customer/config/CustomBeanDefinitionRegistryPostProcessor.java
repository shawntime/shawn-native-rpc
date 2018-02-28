package com.shawntime.rpc.customer.config;

import java.util.List;
import javax.annotation.Resource;

import com.shawntime.rpc.core.RpcServiceScanner;
import com.shawntime.rpc.core.serialize.Formatter;
import com.shawntime.rpc.core.serialize.Parse;
import com.shawntime.rpc.customer.ServiceFactoryBean;
import com.shawntime.rpc.customer.ServiceFindManager;
import com.shawntime.rpc.customer.invoke.Invoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Configuration;

/**
 * Created by shma on 2018/2/13.
 */
@Configuration
public class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CustomBeanDefinitionRegistryPostProcessor.class);

    private Invoke invoke;

    private Formatter formatter;

    private Parse parse;

    private BeanNameGenerator beanNameGenerator;

    private ServiceFindManager serviceFindManager;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        logger.info("Invoke Metho postProcessBeanDefinitionRegistry");
        List<Class<?>> clazzList = RpcServiceScanner.scan();
        for (Class<?> clazz : clazzList) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClass(ServiceFactoryBean.class);
            beanDefinition.setLazyInit(true);
            beanDefinition.getPropertyValues().addPropertyValue("invoke", invoke);
            beanDefinition.getPropertyValues().addPropertyValue("formatter", formatter);
            beanDefinition.getPropertyValues().addPropertyValue("parse", parse);
            beanDefinition.getPropertyValues().addPropertyValue("clazz", clazz);
            beanDefinition.getPropertyValues().addPropertyValue("serviceFindManager", serviceFindManager);
            String beanName = this.beanNameGenerator.generateBeanName(beanDefinition, beanDefinitionRegistry);
            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // ----
    }

    public Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Invoke invoke) {
        this.invoke = invoke;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public Parse getParse() {
        return parse;
    }

    public void setParse(Parse parse) {
        this.parse = parse;
    }

    public BeanNameGenerator getBeanNameGenerator() {
        return beanNameGenerator;
    }

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    public ServiceFindManager getServiceFindManager() {
        return serviceFindManager;
    }

    public void setServiceFindManager(ServiceFindManager serviceFindManager) {
        this.serviceFindManager = serviceFindManager;
    }
}
