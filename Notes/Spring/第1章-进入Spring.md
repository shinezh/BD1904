# 第1天 Spring

## 什么是Spring

产生于2003年，是一个分层的javase/ee一站式轻量级开源框架；



## 优点

1. 简化开发，方便解耦；（高内聚，低耦合）

   spring本身是一个工厂（容器），创建管理对象以及维护对象之间关系；

2. 支持AOP编程， 面向切面编程；拦截添加增强；

3. 支持优秀的框架集成

4. 简化javaee的api，是对javaee的良好补充；

5. 支持声明式事务；

6. 支持junit集成；



## Spring Framework核心

### IOC - 控制反转   DI - 依赖注入

将手动创建对象、管理对象和维护对象关系的权力反转给spring的IOC容器（配置）；

### AOP



![SpringFramework](../pics/arch1.png)

## 核心容器

核心容器由核心，Bean，上下文和表达式语言模块组成，它们的细节如下：

- **核心**模块提供了框架的基本组成部分，包括 IoC 和依赖注入功能。
- **Bean** 模块提供 BeanFactory，它是一个工厂模式的复杂实现。
- **上下文**模块建立在由核心和 Bean 模块提供的坚实基础上，它是访问定义和配置的任何对象的媒介。ApplicationContext 接口是上下文模块的重点。
- **表达式语言**模块在运行时提供了查询和操作一个对象图的强大的表达式语言。

## 数据访问/集成

数据访问/集成层包括 JDBC，ORM，OXM，JMS 和事务处理模块，它们的细节如下：

- **JDBC** 模块提供了删除冗余的 JDBC 相关编码的 JDBC 抽象层。
- **ORM** 模块为流行的对象关系映射 API，包括 JPA，JDO，Hibernate 和 iBatis，提供了集成层。
- **OXM** 模块提供了抽象层，它支持对 JAXB，Castor，XMLBeans，JiBX 和 XStream 的对象/XML 映射实现。
- Java 消息服务 **JMS** 模块包含生产和消费的信息的功能。
- **事务**模块为实现特殊接口的类及所有的 POJO 支持编程式和声明式事务管理。

## Web

Web 层由 Web，Web-MVC，Web-Socket 和 Web-Portlet 组成，它们的细节如下：

- **Web** 模块提供了基本的面向 web 的集成功能，例如多个文件上传的功能和使用 servlet 监听器和面向 web 应用程序的上下文来初始化 IoC 容器。
- **Web-MVC** 模块包含 Spring 的模型-视图-控制器（MVC），实现了 web 应用程序。
- **Web-Socket** 模块为 WebSocket-based 提供了支持，而且在 web 应用程序中提供了客户端和服务器端之间通信的两种方式。
- **Web-Portlet** 模块提供了在 portlet 环境中实现 MVC，并且反映了 Web-Servlet 模块的功能。

## 其他

还有其他一些重要的模块，像 AOP，Aspects，Instrumentation，Web 和测试模块，它们的细节如下：

- **AOP** 模块提供了面向方面的编程实现，允许你定义方法拦截器和切入点对代码进行干净地解耦，它实现了应该分离的功能。
- **Aspects** 模块提供了与 **AspectJ** 的集成，这是一个功能强大且成熟的面向切面编程（AOP）框架。
- **Instrumentation** 模块在一定的应用服务器中提供了类 instrumentation 的支持和类加载器的实现。
- **Messaging** 模块为 STOMP 提供了支持作为在应用程序中 WebSocket 子协议的使用。它也支持一个注解编程模型，它是为了选路和处理来自 WebSocket 客户端的 STOMP 信息。
- **测试**模块支持对具有 JUnit 或 TestNG 框架的 Spring 组件的测试。





