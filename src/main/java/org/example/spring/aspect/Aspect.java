package org.example.spring.aspect;


public interface Aspect {

    Class<?> pointCut();

    Object around(JdkProceedingJoinPoint pjp) throws Exception;

}
