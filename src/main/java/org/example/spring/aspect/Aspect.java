package org.example.spring.aspect;


public interface Aspect {

    Class<?> pointCut();

    Object around(ProceedingJoinPoint pjp) throws Throwable;

}
