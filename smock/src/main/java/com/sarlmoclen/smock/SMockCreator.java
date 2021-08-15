package com.sarlmoclen.smock;

import java.util.List;

public interface SMockCreator {

  <T> T createMock(Class<T> classToMock, List<OngoingStubbing> ongoingStubbingList);

}
