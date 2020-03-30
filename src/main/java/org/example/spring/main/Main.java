package org.example.spring.main;

import org.example.spring.bean.BeanFactory;
import org.example.spring.bean.ComponentScan;
import org.example.spring.service.User;

@ComponentScan("org.example.spring.service")
public class Main {

    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new BeanFactory(Main.class);
        User user = (User) beanFactory.getBean("userImpl");
        System.out.println(user);
    }
}
