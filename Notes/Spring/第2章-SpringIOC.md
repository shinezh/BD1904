# Spring IOC容器

## IOC是什么





## 实现准备

### xml解析（dom4j）



### Xpath 表达式



### 反射



### 内省





## 实现步骤

### 导入spring jar

core/beans/context/expression/commons-logging

### 配置文件

- \<bean>**标签**
  - 代表创建和管理的一个对象；
  - 默认调用无参构造；

- **依赖注入**

  - 设值注入：java 接口+setter方法

    ```xml
    <bean><property> </property></bean>
    ```

  - **构造注入**：接口+有参构造

    ```xml
    <bean><constructor-arg> </constructor-arg></bean>
    ```

    

### 加载配置文件

获取ioc容器（对象管理），获取对象；



### 根据配置文件初始化容器

### 根据配置文件创建bean并放入容器中；  



## 操作步骤

1. 导入dom4j、jaxen包，用于xml解析
2. 

