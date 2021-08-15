package com.sarlmoclen.smock;

import android.annotation.SuppressLint;

import java.util.Arrays;
import java.util.Objects;

public class OngoingStubbing<T> {

  private String className;
  private String methodName;
  private Object[] args;
  private T result;

  public OngoingStubbing(String className, String methodName, Object[] args) {
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

  @SuppressLint("NewApi")
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OngoingStubbing<?> ongoingStubbing = (OngoingStubbing<?>) o;
    return Objects.equals(className, ongoingStubbing.className) &&
        Objects.equals(methodName, ongoingStubbing.methodName) &&
        Arrays.equals(args, ongoingStubbing.args);
  }

  @SuppressLint("NewApi")
  @Override
  public int hashCode() {
    int result = Objects.hash(className, methodName);
    result = 31 * result + Arrays.hashCode(args);
    return result;
  }

}
