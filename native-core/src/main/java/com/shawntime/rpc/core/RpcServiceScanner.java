package com.shawntime.rpc.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.shawntime.rpc.core.annotation.RpcService;
import com.shawntime.rpc.core.util.PkgUtil;
import com.shawntime.rpc.core.util.PropertyUtil;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Created by yihui on 2017/8/19.
 */
public final class RpcServiceScanner {

    private RpcServiceScanner() {
        // ----
    }

    public static final List<Class<?>> scan() {
        List<Class<?>> clazzList = new ArrayList<>();
        String packages = PropertyUtil.getProperty(Const.SCANNER_PACKAGE_NAME);
        if (StringUtils.isEmpty(packages)) {
            return clazzList;
        }
        String[] pkgArray = StringUtils.tokenizeToStringArray(packages,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        for (String pkg : pkgArray) {
            Set<Class<?>> clazzSet = PkgUtil.getClazzFormPackage(pkg);
            clazzSet.forEach(clazz -> {
                if (clazz != null && clazz.getAnnotation(RpcService.class) != null) {
                    clazzList.add(clazz);
                }
            });
        }
        return clazzList;
    }
}