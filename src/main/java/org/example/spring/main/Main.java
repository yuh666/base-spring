package org.example.spring.main;

import org.example.spring.bean.BeanFactory;
import org.example.spring.bean.ComponentScan;
import org.example.spring.service.User;
import sun.jvm.hotspot.debugger.arm.ARMThreadContext;

@ComponentScan("org.example.spring.service")
public class Main {

    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new BeanFactory(Main.class);
        User user = (User) beanFactory.getBean("userImpl");
        user.demo();
    }


}
