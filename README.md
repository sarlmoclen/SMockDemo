# SMockDemo
模仿Mockito的mock库
# 背景知识
## Unit Test
在java或者kotlin中，指的就是针对类进行测试。也就是说这里设定类为最小的可测试单元。
## 为什么要Unit Test
### 提高代码正确性
比如执行结果符合预期，流程按步骤执行符合预期等等
### 启动速度快
在本地运行，比编译安装速度快很多
### 好的单测可作为指导文档，方便使用者使用及阅图
### 提升代码可读性
容易写的单测一定是好理解的，可读性高，符合KISS原则
### 发现设计问题
比如代码可测性比较差，方法封装不合理
### 顺便微重构
如果发现了设计不合理，这里就可以去重构
## 如何做Unit Test
Unit Test也叫Local Unit Test，这里的Local指的就是本地的虚拟机运行环境JVM，JVM中可以直接运行我们编写的class文件，所以，我们就在这里编写一些验证逻辑，达到测试的目的。

Android本身提供了Junit库，但是如果遇到依赖关闭比较深的类，当我们为这个类创建实例对象时候将会非常麻烦，这里便要用到Mockito，它可以mock当前类所需的所有依赖项，并且对这些依赖项进行stub（打桩）和verfity（验证），从而实现对当前类的测试行为。
# Mockito 是什么
上面我们提到了Mockito的两个功能，这里注重介绍下。
## stub（打桩）
打桩的意思就是，我们可以为某个行为指定输入输出，我们有这样一个类进行测试：
```java
public class Utils {

    public int add(int arg0, int arg1){
        int result = arg0 + arg1;
        return  result;
    }

}
```
然后我们针对建立测试类：
```java
@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Test
    public void testAdd_mockito(){
        Utils utils = Mockito.mock(Utils.class);
        Mockito.when(utils.add(1,2)).thenReturn(6);
        Assert.assertEquals(6,utils.add(1,2));
    }

}
```
Mockito.when(utils.add(1,2)).thenReturn(6)，这句的意思是当执行utils.add(1,2)时候，返回6，我们指定了输入和输出。
## verify（验证）
同样是针对上面的类进行测试：
```java
@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Test
    public void testAdd(){
        Utils utils = Mockito.mock(Utils.class);
        utils.add(1,2);
        utils.add(1,2);
        Mockito.verify(utils, new Times(2)).add(1,2);
    }

}
```
Mockito.verify(utils,new Times(2)).add(1,2)，这句意思是验证utils.add(1,2)有没有被执行两次。
# 分析Mockito原理
我们可以先做一个测试，将单元测试改成如下写法：
```java
@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Test
    public void testAdd_mockito(){
        Utils utils = Mockito.mock(Utils.class);
        utils.add(1,2)；
        Mockito.when(1).thenReturn(6);
        Assert.assertEquals(6,utils.add(1,2));
    }

}
```
发现测试是通过的，对比下之前的写法：
```java
@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Test
    public void testAdd_mockito(){
        Utils utils = Mockito.mock(Utils.class);
        Mockito.when(utils.add(1,2)).thenReturn(6);
        Assert.assertEquals(6,utils.add(1,2));
    }

}
```
发现不同在when函数的参数部分，我们看下when的实现：
```java
public <T> OngoingStubbing<T> when(T methodCall) {
    MockingProgress mockingProgress = mockingProgress();
    mockingProgress.stubbingStarted();
    @SuppressWarnings("unchecked")
    OngoingStubbing<T> stubbing = (OngoingStubbing<T>) mockingProgress.pullOngoingStubbing();
    if (stubbing == null) {
        mockingProgress.reset();
        throw missingMethodInvocation();
    }
    return stubbing;
}
```
这里其实methodCall是没有被用到的，所以我们可以怀疑，重点是执行utils.add方法时触发了某些行为，根据功能我们猜测，当执行utils.add时候，Mockito会把函数的相关信息（签名信息，类名等）存储下来，当执行thenReturn时候，把最近一条函数信息跟6做绑定存储，最后，当执行utils.add时候，根据这些信息，查找当时设置的值并返回。我们改下测试代码：
```java
@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Test
    public void testAdd_mockito(){
        Utils utils = Mockito.mock(Utils.class);
        utils.add(1,2)；
        utils.add(1,3)
        Mockito.when(1).thenReturn(6);
        Assert.assertEquals(6,utils.add(1,2));
    }

}
```
这个时候返回：
```java
java.lang.AssertionError:
Expert:6
Actual:0
```
跟我们猜想的行为相符。

我们知道Mockito是基于动态代理设计实现的，根据上面的实例以及猜想，这里可以理解为利用动态代理，我们得到当前类的代理类，通过代理类，我们可以控制修改调用行为以及返回行为，实现打桩，当然也可以验证一些调用行为，实现验证功能。
# 自己写一个Mockito
Mockito是通过Byte Buddy （不利用本地编译器，自己生成class文件）和Objenesis （实例化class文件，绕过构造器）生成和初始化 mock 对象的，我们也用这两个库。
