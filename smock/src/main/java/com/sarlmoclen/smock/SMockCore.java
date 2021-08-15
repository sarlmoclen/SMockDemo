package com.sarlmoclen.smock;

import java.util.ArrayList;
import java.util.List;

public class SMockCore {

  private final List<OngoingStubbing> ongoingStubbingList = new ArrayList<>(100);
  private final SMockCreator mockCreator = new ByteBuddySMockCreator();

  public <T> T mock(Class<T> classToMock) {
    T result = mockCreator.createMock(classToMock, ongoingStubbingList);
    return result;
  }

  @SuppressWarnings("unchecked")
  public <T> OngoingStubbing<T> when(T methodCall) {
    int currentSize = ongoingStubbingList.size();
    return (OngoingStubbing<T>) ongoingStubbingList.get(currentSize - 1);
  }

}
