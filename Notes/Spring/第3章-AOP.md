# 第3章 AOP

## 嘛是AOP

采用横向抽取方式，在运行期间将增强代码添加到目标对象的一种思想，底层采用动态代理；



## 应用场景

- 事务管理（声明式事务）

- 日志系统
- 性能检测
- .....



## 相关框架

### Spring AOP

### aspect

### jboss



## 专业术语

### target 目标对象

- 需要添加增强代码的对象；

### joinpoint 连接点

- 可以添加增强代码的目标方法

### pointcut 切入点

- 已经添加了增强代码方法；是指我们要对哪些连接点进行拦截的定义；

### advice 通知

- 实现特定接口的增强代码类
- 前置通知、后置通知、环绕通知、异常通知、最终通知

### aspect 切面

- 增强代码和切入点之间形成的逻辑面

### weaver 织入

- 增强代码，添加到切入点过程



## 入门案例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd ">

    <!--创建目标类对象-->
    <bean id="userService" class="service.impl.UserServiceImpl">
        <!-- collaborators and configuration for this bean go here -->
    </bean>
    <!--创建增强代码类对象-->
    <bean id="myAdvice" class="advice.MyAspect">
        <!-- collaborators and configuration for this bean go here -->
    </bean>
    <!--3. 织入增强代码-->
    <aop:config>
        <!--切入点：寻找添加增强代码的方法
            id 唯一标识
            expression 设定作为切入点的方法
            execution(返回值 包名.类名.方法名（参数列表）） 通配符也可以
            execution(* service.impl.UserServiceImpl.*(..))
            -->
        <aop:pointcut id="pointCut" expression="execution(* service..*(..))"/>
        <!--切面：切入点+增强代码-->
        <aop:aspect ref="myAdvice">
            <!--前置增强-->
            <aop:before method="before" pointcut-ref="pointCut"></aop:before>
            <!--后置增强-->
            <aop:after method="after" pointcut-ref="pointCut"></aop:after>
        </aop:aspect>
    </aop:config>
</beans>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd">
    <!--配置service-->
    <bean id="customerService" class="cn.shine.service.impl.ICustomerServiceImpl">
    </bean>
    <!--基于xml的aop配置-->
    <!--1、把通知类交给spring管理-->
    <bean id="logger" class="cn.shine.service.utils.Logger">
    </bean>

    <!--2、导入aop名称空间，并且使用aop：config开始aop的配置-->
    <aop:config>
        <!--3、使用aop：aspect配置切面，
			id属性用于给切面提供一个唯一标识，
		ref属性 用于应用通知bean的id-->
        <aop:aspect id="logAdvice" ref="logger">
            <!--4、配置通知的类型，指定增强的方法何时执行
                method属性：用于指定的增强方法名称
                pointcut属性：用于指定切入点表达式
                    关键字：execution表达式
                    写法：访问修饰符 返回值 包名.包名...类名.方法名（参数列表）
                -->
            <aop:before method="printLog" pointcut="execution(public void cn.shine.service.impl.ICustomerServiceImpl.saveCustomer())"/>
        </aop:aspect>
    </aop:config>
```

- 前置通知

- 后置通知

- 异常通知

- 最终通知

- 环绕通知：直接配置环绕通知，切入点方法没有执行，而环绕通知里面的方法执行了；

  - 由动态代理可知，环绕通知指的是invoke方法，并且有明确的切入点方法调用

  - spring为我们提供了一个接口，ProceedingJionPoint.该接口可以作为环绕通知的方法参数来使用

    该接口中有一个方法 proceed()  相当于method.invoke(),，就是明确调调用业务层的核心方法（切入点方法）

  - **环绕通知**是spring框架为我们提供的一种可以在代码中手动控制通知方法什么时候执行的方式；

  ```java
   /**
   * 环绕通知
   * @return null
   */
  public Object aroundPrintLog(ProceedingJoinPoint pjp){
      Object proceed = null;
      try {
          System.out.println("前置");
          proceed = pjp.proceed();
          System.out.println("后置");
      } catch (Throwable throwable) {
          System.out.println("异常");
          throwable.printStackTrace();
      }finally {
          System.out.println("最终");
      }
      return proceed;
  }
  ```

  


## 注解AOP









## 代理

访问目标之前或者之后实现预处理及后续处理；



## 代理模式

### 静态代理

- 接口编写代理类；



### 动态代理

- 运行期间，对目标方法进行拦截，添加增强；

[动态代理](url="C:\Users\shine\Desktop\BD1904\Notes\Spring\第4章-动态代理.md")

#### jdk动态代理（标准动态代理）

- 接口+委托类

- 代理类实现接口（知道委托类中的所有方法，拦截、增强)

#### cglib动态代理

-  只需要委托类；

- 代理类对象直接继承委托类（知晓委托类方法）

  ```java
  public void testCglib(){
      //1.创建委托类对象
      UserServiceImpl2 userService = new UserServiceImpl2();
      //2.给委托类创建代理对象（代理类对象继承委托类）
      Enhancer enhancer = new Enhancer();
      //认爹
      enhancer.setSuperclass(UserServiceImpl2.class);
      //方法拦截处理
      enhancer.setCallback(new MethodInterceptor() {
          @Override
          public Object intercept(Object proxy, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
              //前置增强
              Object reValue = method.invoke(userService, objects);
              //后置增强
              return reValue;
          }
      });
      //3.访问代理对象的方法
      UserServiceImpl2 proxy2 = (UserServiceImpl2) enhancer.create();
      proxy2.addUser();
      proxy2.deleteUser();
  }
  ```






## 注解实现装配

### 常见注解（基本装配）

```xml
<bean class=" "></bean>     @Component

@Controller
表示层
@Service
业务逻辑层
@Repository
数据持久层
```



### 注入

- **xml**

  - 手工注入（设值注入，构造注入）

    

- 自动注入

  ```xml
  
  ```

  - **ByType**
    - 按照类型注入，从整个ioc容器中寻找属性，同类型的值实现注入；
    - 如果找不到，则不注入；
    - 如果找到一个，则注入成功；
    - 如果找到多个，则抛出异常；
  - **ByName**
    - 按照名字注入，从整个ioc容器中寻找属性（参数），同名的值实现注入
    - 



- 注解注入
  - @**Autowired：**按照类型注入（属性->参数）；

