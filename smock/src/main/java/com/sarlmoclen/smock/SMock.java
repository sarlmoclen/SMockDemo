package com.sarlmoclen.smock;

public class SMock {

  private static final SMockCore SMOCK_CORE = new SMockCore();

  public static <T> T mock(Class<T> clazz) {
    return SMOCK_CORE.mock(clazz);
  }

  public static <T> OngoingStubbing when(T methodCall) {
    return SMOCK_CORE.when(methodCall);
  }

}
