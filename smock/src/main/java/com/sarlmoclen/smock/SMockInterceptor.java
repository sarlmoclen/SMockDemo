package com.sarlmoclen.smock;

import android.annotation.SuppressLint;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class SMockInterceptor {

  private static final String BYTE = "byte";
  private static final String SHORT = "short";
  private static final String INT = "int";
  private static final String LONG = "long";
  private static final String FLOAT = "float";
  private static final String DOUBLE = "double";
  private static final String BOOLEAN = "boolean";
  private static final String CHAR = "char";
  private List<OngoingStubbing> ongoingStubbingList;

  public SMockInterceptor(List<OngoingStubbing> ongoingStubbingList) {
    this.ongoingStubbingList = ongoingStubbingList;
  }

  @SuppressLint("NewApi")
  public Object invoke(Object mock, Method invokedMethod, Object[] args) {
    String methodName = invokedMethod.getName();
    String className = mock.getClass().getName();
    OngoingStubbing ongoingStubbing = new OngoingStubbing(className, methodName, args);
    if (!ongoingStubbingList.contains(ongoingStubbing)) {
      ongoingStubbingList.add(ongoingStubbing);
      Type type = invokedMethod.getReturnType();
      if(type.getTypeName().equals(BYTE)
              ||type.getTypeName().equals(SHORT)
              ||type.getTypeName().equals(INT)
              ||type.getTypeName().equals(LONG)){
        return 0;
      }
      if(type.getTypeName().equals(FLOAT)
              ||type.getTypeName().equals(DOUBLE)){
        return 0f;
      }
      if(type.getTypeName().equals(BOOLEAN)){
        return false;
      }
      if(type.getTypeName().equals(CHAR)){
        return '0';
      }
      return null;
    } else {
      int index = ongoingStubbingList.indexOf(ongoingStubbing);
      OngoingStubbing recordedOngoingStubbing = ongoingStubbingList.get(index);
      return recordedOngoingStubbing.getResult();
    }
  }

}
