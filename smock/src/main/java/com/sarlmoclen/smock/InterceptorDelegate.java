package com.sarlmoclen.smock;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;

public class InterceptorDelegate {

  @RuntimeType
  public static Object intercept(@This Object mock,
                                 @Origin Method invokedMethod,
                                 @FieldValue("interceptor") SMockInterceptor interceptor,
                                 @AllArguments Object[] args) {
    return interceptor.invoke(mock, invokedMethod, args);
  }

}
