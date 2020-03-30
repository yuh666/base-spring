package org.example.spring.aspect;

import java.lang.reflect.Method;

public class JdkProceedingJoinPoint implements ProceedingJoinPoint {

    private Method method;
    private Object[] args;
    private Object target;

    public JdkProceedingJoinPoint(Method method, Object[] args, Object target) {
        this.method = method;
        this.args = args;
        this.target = target;
    }

    @Override
    public Object proceed() throws Exception {
        return method.invoke(target, args);
    }
}
