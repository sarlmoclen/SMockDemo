package com.sarlmoclen.smock;

import java.util.Arrays;

public class InvocationDetail<T> {

    private String className;

    private String methodName;

    private Object[] args;

    private T result;

    public InvocationDetail(String className, String methodName, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public void thenReturn(T t) {
        this.result = t;
    }

    public T getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvocationDetail invocationDetail = (InvocationDetail) o;
        return equals(className, invocationDetail.className) &&
                equals(methodName, invocationDetail.methodName) &&
                Arrays.equals(args, invocationDetail.args);
    }

    private boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{className.hashCode(), methodName.hashCode(), args.hashCode()});
    }

}
