# Bean的相关

```
 ClassPathXmlApplicationContext:只能获取类路径下的配置文件* 
 FileSystemXmlApplicationContext:可以获取磁盘任意路径下的配置文件** 
 
```

## bean创建的两种规则

- BeanFactory：延迟加载思想，bean对象什么时候用什么时候创建
- ApplicationContext：立即加载思想，只要一解析完配置文件就立即创建对象
- 



## bean的三种创建方式

- 调用默认无参构造函数，若没有默认无参构造器，则报错   此种方式使用最多*  
- 使用工厂中的方法创建bean,需要使用bean标签的factory-method属性，从静态工厂中创建对象*  
- 使用实例工厂创建



## Bean的作用范围

- 可以通过配置文件更改作用范围
- 配置的属性    **scope属性**
- 属性的取值：
  - singleton：默认，单例
  - prototype：多例
  - request：作用范围是一次请求，和当前请求的转发
  - session：作用范围是一次会话
  -  globalsession：全局会话



## Bean的生命周期

### 涉及标签

- scope；  //singleton protype
  - init-method
  - destroy-method

## 单例

- 出生：容器创建，对象就出生
- 运行：只要容器在，对象就一直在
- 死亡：容器销毁，则对象销毁；



### 多例

- 出生：每次使用时创建
- 运行：只要对象在使用中，就一直活着
- 死亡：当对象长时间未使用，并且也没有被别的对象引用，由Java的垃圾回收器回收；。





## Spring的依赖注入

### 注入方式

- 使用构造函数注入

  - 涉及标签   constructor-args
  - 标签属性：
    - type：指定参数类型
    - index：制定参数的索引位置，从0开始
    - name：指定参数的名称（一般用这个）
    - ------上面三个属性是指定给哪个属性赋值，下面两个指定赋什么值------
    - ref：指定其他bean类型数据
    - value：指定基本数据类型或String类型
  - 标签位置：bean标签内

  ```xml
  <bean id="customerService" class="cn.aura.imp.ICustomerServiceImpl">
      <constructor-arg name="driver" value="111"/>
      <constructor-arg index="1" value="222"/>
  </bean>
  ```

  

- 使用set方法注入

  - 涉及标签： property
  - 涉及属性：
    - name：指定参数的名称（一般用这个）
    - ------上面属性是指定给哪个属性赋值，下面两个指定赋什么值------
    - ref：指定其他bean类型数据
    - value：指定基本数据类型或String类型

- 使用注解注入



### 注入类型

- 基本类型和String类型
- 其他bean类型（必须在spring配置文件中出现过的bean）
- 复杂类型（集合等）



