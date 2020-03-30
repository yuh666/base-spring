package org.example.spring.bean;


public class BeanDefinition {

    private String name;
    private Class<?> clazz;
    private boolean isAspect;

    public BeanDefinition(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public Object newInstance() throws Exception {
        return clazz.newInstance();
    }

    public void setAspect(boolean aspect) {
        isAspect = aspect;
    }

    public boolean isAspect() {
        return isAspect;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }
}
