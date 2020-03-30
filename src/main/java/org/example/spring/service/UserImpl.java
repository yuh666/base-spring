package org.example.spring.service;

import org.example.spring.bean.Autowired;
import org.example.spring.bean.Component;

@Component
public class UserImpl implements User {

    @Autowired
    private User2 user2Impl;



    @DemoPointCut
    @Override
    public void demo() {
        System.out.println("xxxx");
    }
}
