# 第5章 SSM

## Spring MVC参数封装

- 简单参数类型封装

  请求参数名称和方法参数名称一致，则可以直接封装；

- 对象类型数据封装

  请求参数名称和对象的顺序保持一致，则可封装；

- 数组类型封装

  请求参数名称和数组名称一致，则可封装；

- list或者map集合

  需要作为对象的属性存在，只要和请求参数名称一致，则可以封装； 



## 数据回显

借助model对象，使用和request一致；





## 转发和重定向

forward：请求 转发；

redirect：请求 重定向；



## SSM整合

### 整合思路

- **mybatis：**负责数据持久层（数据库连接）

- **spring mvc**：负责表示层（接受请求和返回响应）

- **spring framework：**负责业务逻辑层
  - 整个项目的衔接问题（jar，配置）；
  - mapper，sqlSession，ssf对象的创建和管理交给spring
  - mybatis 配置文件和spring配置文件合并；
  - 事务管理->spring

### jar包整合

- mybatis
  - mybatis.jar
  - mybatis-spring  简化spring管理mybatis中的对象

- SQL
  - MySQL-connector
  - log4j
  - commons-logging

- spring-framework
  - spring-core
  - spring-context
  - spring-beans
  - spring-expression
  - spring-aop
  - spring-aspects
  - aopalliance
  - aspectjweaver
  - spring-jdbc  简化jdbc开发（配置连接池）
  - spring-orm  spring整合orm框架
  - spring-tx     事务管理

- springmvc
  - spring-webmvc
  - spring-web



### 配置文件整合



