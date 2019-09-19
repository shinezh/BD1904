# Mybatis

## Mybatis

MyBatis 是一款优秀的**持久层框架**，它支持**定制化 SQL**、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生类型、接口和 Java 的 POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。



## SSm





## Mybatis框架

- 是Apache旗下的开源项目，原名 ==ibatis== 
- 是一款优秀的持久层框架，它支持定制化SQL



## 入门案例

### 步骤

1. 导包，mybaits.jar 数据库的jar
2. 配置 mybatis 核心配置文件；一次
3. 配置 mapper 的xml映射文件；
   1. 定制化sql
   2. 输入参数
   3. 输出结果



```xml
<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
<!--    <properties resource="/config/mybatis.properties">-->
<!--        <property name="username" value="root"/>-->
<!--        <property name="password" value="mysql"/>-->
<!--    </properties>-->

    <environments default="development">
        <!--运行环境-->
        <environment id="development">
            <!--事务管理-->
            <transactionManager type="JDBC"/>

            <!--数据来源-连接池-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql:///db1904?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=UTC"/>
                <property name="username" value="root"/>
                <property name="password" value="mysql"/>
            </dataSource>
        </environment>
    </environments>

    <!-- 使用相对于类路径的资源引用 -->
    <mappers>
        <mapper resource="mapper/StudentMapper.xml"/>
    </mappers>
</configuration>
```

- **查询：**

  -  #{}和\${}区别:
    	  1.#{}代表占位符  ${}标识连接符
    	  2.\${}存在sql注入的危险,敏感数据不可使用,排序类似场景使用比较方便
    	  3.简单数值类型: \#{} :任意,​\${} :必须是value
    	    对象类型: 都是属性名
    		map:都是key
  - 增删改:
        主键返回:
           1.use...               (主键自增)
           2.selectKeys	  (通用) 
  - 技巧:
         mybatis-3.4.5\org\apache\ibatis\builder\xml\

  ```xml
  <mapper namespace="dfsjgfkjk">
  
    <!-- 根据bid查询出一个book封装到book对象中-->
    <select id="selectBook" resultType="book">
        select * from book where bid = #{bid}
    </select>
    
    <!--1.查询所有  resultType:单个对象的类型  -->
    <select id="selectAll" resultType="Book">
       select * from book
    </select>
    
    <!--2.模糊查询   查询书名中包含"三"书  -->
    <select id="selectLikeName" resultType="book">
       select * from book where bname like '%${value}%'
    </select>	 
     
    <!--3.插入  主键返回   keyProperty:存放的属性名称 -->
    <!-- <insert id="insert" useGeneratedKeys="true" keyProperty="bid">
       insert into book(bname,author,price) values(#{bname},#{author},#{price})
    </insert> -->
   
    <insert id="insert">
       <selectKey keyProperty="bid" resultType="int" order="AFTER">
       	select LAST_INSERT_ID()
       	<!-- select SEQ.nextVal from dual -->
       </selectKey>
       insert into book(bname,author,price) values(#{bname},#{author},#{price})
    </insert>
  
  </mapper>  
  ```

  

## mybatis运行原理

### 全局配置

- 事务管理器
- 连接池

### 映射

- mapper接口+映射文件
- 定制化sql，输入参数，输出结果

### 测试





## mapper代理方式

mapper接口+mapper映射文件

#### 完全限定名称-namespace

#### 方法名 -> id

#### 参数类型 -> parameterType

#### 返回值类型  -> ResultType、ResultMap



## 高级映射

### 封装到集合

\<colletion property="" column="" ofType="">



### 封装到对象

\<association property=" " colum=" " javaType=" ">

