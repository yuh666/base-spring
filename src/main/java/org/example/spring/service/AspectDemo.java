package org.example.spring.service;

import org.example.spring.aspect.Aspect;
import org.example.spring.aspect.JdkProceedingJoinPoint;
import org.example.spring.aspect.ProceedingJoinPoint;
import org.example.spring.bean.Component;

@Component
public class AspectDemo implements Aspect {
    @Override
    public Class<?> pointCut() {
        return DemoPointCut.class;
    }

    @Override
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("pre exe");
        Object proceed = pjp.proceed();
        System.out.println("post exe");
        return pjp;
    }
}
