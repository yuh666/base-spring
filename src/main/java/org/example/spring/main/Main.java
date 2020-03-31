package org.example.spring.main;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.example.spring.bean.BeanFactory;
import org.example.spring.bean.ComponentScan;
import org.example.spring.service.User;
import sun.jvm.hotspot.debugger.arm.ARMThreadContext;

import java.lang.reflect.Method;

@ComponentScan("org.example.spring.service")
public class Main {

    public static void main(String[] args) throws Exception {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        BeanFactory beanFactory = new BeanFactory(Main.class);
        User user = (User) beanFactory.getBean("userImpl");
        user.demo();
    }





}
