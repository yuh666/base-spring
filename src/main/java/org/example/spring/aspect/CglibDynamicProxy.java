package org.example.spring.aspect;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

public class CglibDynamicProxy implements DynamicProxy, MethodInterceptor {

    private Object target;
    private Map<Class<?>, Aspect> aspectMap;


    public CglibDynamicProxy(Object target,
            Map<Class<?>, Aspect> aspectMap) {
        this.target = target;
        this.aspectMap = aspectMap;
    }

    @Override
    public Object createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args,
            MethodProxy methodProxy) throws Throwable {
        Aspect aspect = null;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            aspect = aspectMap.get(annotation.annotationType());
            break;
        }
        if (aspect == null) {
            return methodProxy.invoke(target, args);
        }
        CglibProceedingJoinPoint pjp = new CglibProceedingJoinPoint(
                methodProxy, args, target);
        return aspect.around(pjp);
    }
}
