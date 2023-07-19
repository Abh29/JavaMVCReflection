package com.company.tools;

public class Param<T> {
    public T value;
    public Class type;
    public boolean nullable = false;
    public Param(T value, Class type, boolean nullable){
        this.value = value;
        this.type = type;
        this.nullable = nullable;
    }
    public Param(T value , Class type){
        this.value = value;
        this.type = type;
    }
    public Param(Class type){
        this.type = type;
        this.value = (T) new Object();
    }
    public Param(Class type, boolean nullable){
        this.value = (T) new Object();
        this.type = type;
        this.nullable = nullable;
    }

}
