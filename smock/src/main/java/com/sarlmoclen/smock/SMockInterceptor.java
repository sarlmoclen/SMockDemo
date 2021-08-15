package com.sarlmoclen.smock;

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
  private List<OngoingStubbing> recordedInvocationDetails;

  public SMockInterceptor(List<OngoingStubbing> recordedInvocationDetails) {
    this.recordedInvocationDetails = recordedInvocationDetails;
  }

  public Object invoke(Object mock, Method invokedMethod, Object[] arguments) {
    String methodName = invokedMethod.getName();
    String attachedClassName = mock.getClass().getName();
    OngoingStubbing invocationDetail = new OngoingStubbing(attachedClassName, methodName, arguments);
    if (!recordedInvocationDetails.contains(invocationDetail)) {
      recordedInvocationDetails.add(invocationDetail);
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
      int index = recordedInvocationDetails.indexOf(invocationDetail);
      OngoingStubbing recordedBehaviourDetail = recordedInvocationDetails.get(index);
      return recordedBehaviourDetail.getResult();
    }
  }

}
