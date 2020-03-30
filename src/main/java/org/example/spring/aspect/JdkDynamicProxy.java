package org.example.spring.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;


public class JdkDynamicProxy implements DynamicProxy, InvocationHandler {

    private Object target;
    private Map<Class<?>, Aspect> aspectMap;

    public JdkDynamicProxy(Object target,
            Map<Class<?>, Aspect> aspectMap) {
        this.target = target;
        this.aspectMap = aspectMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Aspect aspect = null;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            aspect = aspectMap.get(annotation.annotationType());
            break;
        }
        if (aspect == null) {
            return method.invoke(target, args);
        }
        JdkProceedingJoinPoint pjp = new JdkProceedingJoinPoint(method, args,
                target);
        return aspect.around(pjp);
    }

    @Override
    public Object createProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }
}
