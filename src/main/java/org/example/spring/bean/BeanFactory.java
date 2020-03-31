package org.example.spring.bean;

import org.example.spring.aspect.Aspect;
import org.example.spring.aspect.CglibDynamicProxy;
import org.example.spring.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.*;

public class BeanFactory {

    private Class<?> bootstrapClass;
    private String scanPath;
    private Map<String, Object> idBeanMap = new HashMap<>();
    private Map<Class<?>, Aspect> aspectMap = new HashMap<>();
    private Map<String, BeanDefinition> idDefinitionMap = new HashMap<>();

    public BeanFactory(Class<?> bootstrapClass) throws Exception {
        this.bootstrapClass = bootstrapClass;
        resolveBootstrapClass();
        loadBeanDefinitions();
        initSingletons();
    }


    private void initSingletons() throws Exception {
        for (String id : idDefinitionMap.keySet()) {
            doGetBean(id);
        }
    }

    private Object doGetBean(String id) throws Exception {
        if (idBeanMap.containsKey(id)) {
            return idBeanMap.get(id);
        }
        BeanDefinition definition = idDefinitionMap.get(id);
        Object o = definition.newInstance();
        Field[] declaredFields = definition.getClazz().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                String name = declaredField.getName();
                Object o1 = doGetBean(name);
                if (o1 == null) {
                    throw new RuntimeException("Cannot autowire bean named: " + name);
                }
                declaredField.setAccessible(true);
                declaredField.set(o, o1);
            }
        }
        if (definition.isAspect()) {
            Aspect a = (Aspect) o;
            aspectMap.put(a.pointCut(), (Aspect) o);
            return a;
        } else {
            Object proxy = new CglibDynamicProxy(o, aspectMap).createProxy();
            idBeanMap.put(id, proxy);
            return proxy;
        }
    }

    private void resolveBootstrapClass() {
        ComponentScan scan = bootstrapClass.getAnnotation(ComponentScan.class);
        if (scan == null || ".".equals(scan.value())) {
            throw new IllegalArgumentException("scan path must be indicated!");
        }
        scanPath = scan.value();
    }

    private void loadBeanDefinitions() {
        Set<Class<?>> classes = ClassUtil.getClasses(scanPath, true);
        for (Class<?> clazz : classes) {
            if (clazz.isInterface()) {
                continue;
            }
            Component component = clazz.getAnnotation(Component.class);
            if (component == null) {
                continue;
            }
            String name = component.name();
            if ("".equals(name)) {
                String simpleName = clazz.getSimpleName();
                name = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
            }
            if (idDefinitionMap.containsKey(name)) {
                throw new RuntimeException("find two beans named: " + name);
            }
            BeanDefinition definition = new BeanDefinition(name, clazz);
            //处理切面
            if (Aspect.class.isAssignableFrom(clazz)) {
                definition.setAspect(true);
            }
            idDefinitionMap.put(name, definition);
        }
    }

    public Object getBean(String id) {
        return idBeanMap.get(id);
    }

}
