package org.example.spring.aspect;

import net.sf.cglib.proxy.MethodProxy;


public class CglibProceedingJoinPoint implements ProceedingJoinPoint {

    private MethodProxy proxy;
    private Object[] args;
    private Object target;

    public CglibProceedingJoinPoint(MethodProxy proxy, Object[] args, Object target) {
        this.proxy = proxy;
        this.args = args;
        this.target = target;
    }

    @Override
    public Object proceed() throws Throwable {
        return proxy.invoke(target, args);
    }
}
