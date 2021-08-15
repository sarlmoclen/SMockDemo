package com.sarlmoclen.smock;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import org.objenesis.ObjenesisStd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;

public class ByteBuddySMockCreator implements SMockCreator {

  @Override
  public <T> T createMock(Class<T> classToMock, List<OngoingStubbing> ongoingStubbingList) {
    DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
            //指定父类
            .subclass(classToMock)
            //指定所有方法都需要拦截
            .method(ElementMatchers.named("add"))
            //委托InterceptorDelegate.class处理拦截的方法
            .intercept(MethodDelegation.to(InterceptorDelegate.class))
            //定义字段“interceptor”，类型是SMockInterceptor.class，private
            .defineField("interceptor", SMockInterceptor.class, Modifier.PRIVATE)
            //继承接口
            .implement(SMockIntercepable.class)
            .intercept(FieldAccessor.ofBeanProperty())
            //产生字节码
            .make();
    outputClazz(dynamicType.getBytes());
    Class<? extends T> classWithInterceptor = (Class<? extends T>) dynamicType
              //加载类
              .load(getClass().getClassLoader(), Default.WRAPPER)
              //获得class对象
              .getLoaded();
    T mockTargetInstance = (T) new ObjenesisStd().newInstance(classWithInterceptor);
    ((SMockIntercepable) mockTargetInstance).setInterceptor(new SMockInterceptor(ongoingStubbingList));
    return mockTargetInstance;
  }

  private static void outputClazz(byte[] bytes) {
    FileOutputStream out = null;
    try {
      String pathName = "/D:/test.class";
      out = new FileOutputStream(new File(pathName));
      out.write(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (null != out) try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
