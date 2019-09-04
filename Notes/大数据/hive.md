# hive

## 原理

> 完全分布式缺陷：单点故障

- namenode的**高可用**设计

  Hadoop2.0中，一个集群中可以设计2个namenode，这两个namenode在同一时间只有一个对外提供服务，将这个nd成为active的，另一个处于热备份状态，称之为standby namenode；一旦active宕机，standby就会立即无缝切换

  secondarynamenode时刻处于冷备份状态

- 要想实现高可用，必须保证
  - active和standby的元数据是实时保持一致；
  - 保证standby和active的状态信息（集群中的namenode状态信息）一致；standby要实时感知active的存活状态；借助于zk active可以将状态信息吸入zk中，standby监听zk对应的状态信息的节点，一旦发现状态信息改变了，立即将自己状态改为active，同时修改zk中的状态信息
  - 转换过程中，避免脑裂发生，standby切换为active之前，



## 搭建准备

### 依赖环境

- JDK
- 时间同步
- 用户、IP、SSH、sudoers设置

### 集群规划

| 内容      | hdp01                  | hdp02                  | hdp03                  |
| --------- | ---------------------- | ---------------------- | ---------------------- |
| HDFS      | namenode   zkfc        | namenode    zkfc       |                        |
|           | datanode \| jounalnode | datanode \| jounalnode | datanode \| jounalnode |
| yarn      | resourcemanager        |                        | resourcemanager        |
|           | nodemanager            | nodemanager            | nodemanager            |
| zookeeper | QuorumPeerMain         | QuorumPeerMain         | QuorumPeerMain         |
|           | 7                      | 6                      | 5                      |

### 开始安装

- 关闭集群
- 删除Hadoop每一个节点的所有数据文件



### 版本

- **1.2.1：**现阶段，企业环境中最常用的版本；
- **2.3.x：**支持底层的计算引擎不只是MapReduce，支持spark，tez



## hive背景

> mapreduce 分布式计算的时候，绝大多数的场景针对于结构化数据的针对结构化数据，做数据统计分析sql语句最擅长



- hive 是啥
- **hive是hadoop的另一种形式的客户端**
- 本质上，是一个翻译器，提供sql(hql)编程，最终底层将sql语句转换为mr任务的，hive表中的数据是存储在hdfs的数据仓库
- facebook开源的
- 只要是能看作二位表格的结构化数据，都可以使用类似sql语法的操作去执行计算



### hive的数据存储

> hive存储数据的库、表之分

- 表中的数据，这个数据底层存储hdfs的；
- **元数据：**描述表数据的数据，默认derby







## hive搭建

### 远程连接

- 启动hive安装节点的 hiveserver2

  `hiveserver2`

- 修改Hadoop配置，==所有节点==

  - hdfs-site.xml

    ```xml
    <property>
    	<name>dfs.webhdfs.enabled</name>
    	<value>true</value>
    </property>
    ```

  - core-site.xml

    ```xml
    <property>
        <name>hadoop.proxyuser.shineu.hosts</name>
        <value>*</value>
    </property>
    
    <property>
        <name>hadoop.proxyuser.shineu.groups</name>
        <value>*</value>
    </property>
    
    hadoop.proxyuser.hadoop.hosts 配置成*的意义，表示任意节点使用 hadoop 集群的代理用户
    hadoop 都能访问 hdfs 集群，hadoop.proxyuser.hadoop.groups 表示代理用户的组所属
    ```

  - 启动hiveserver2服务
  
    `nohup hiveserver2 >/dev/null 2>&1 &`
  
    `nohup hiveserver2 1>/home/hadoop/hiveserver.log 2>/home/hadoop/hiveserver.err &`
  
  - 启动beelin客户端
  
    `beeline -u jdbc:hive2://hdp01:10000 -n shineu`





## hive特点

- Hive由Facebook 实现并开源
- 是基于Hadoop 的一个**数据仓库工具**
  - **数据仓库**（data warehouse）
    - 数据仓库针对于海量数据，数据库针对小批量的数据；
    - 应用场景：
      - **OLAT：**On-Line Analysis Processing 在线分析处理 **数仓 擅长查询，不擅长增删改**
      - **OLTP：**On-Line Transaction Processing 在线事务处理 **数据库擅长 增删改**
  - 对于hive来说，不支持delete update ；支持insert，但不建议使用，效率低下；hive的数据加载 ==load== 
  - **事务支持：**
    - 数仓 不支持 事务；hive3可用
    - 数据库 支持事务；
- 可以将结构化的数据映射为一张数据库表
  - hive只能做结构化数据，海量结构化；
  - hive不能完全替代mr，mr不仅仅可以针对结构化数据，还可以对半结构化数据xml；

- 并提供 HQL(Hive SQL)查询功能，底层数据是存储在**HDFS**上。

- Hive的本质是将 SQL 语句转换为 MapReduce 任务运行

  sql中的关键字转换

  - join
  - group by
  - order by

- 使不熟悉 MapReduce 的用户很方便地利用 HQL 处理和计算 HDFS 上的结构化的数据，适用于**离线**的批量数据计算。



### hive优点

- **可扩展性**, 横向扩展，Hive 可以自由的扩展集群的规模，一般情况下不需要重启服务
  横向扩展：通过分担压力的方式扩展集群的规模
  纵向扩展： 一台服务器cpu i7-6700k 4核心8线程，8核心16线程，内存64G => 128G
- **延展性**，Hive 支持自定义函数，用户可以根据自己的需求来实现自己的函数
- **良好的容错性**，可以保障即使有节点出现问题，SQL 语句仍可完成执行



### hive缺点

1. Hive **不支持记录级别的增删改操作**，但是用户可以通过查询生成新表或者将查询结
   果导入到文件中（当前选择的 hive-2.3.2 的版本支持记录级别的插入操作）
2. Hive 的查询**延时很严重**，因为 MapReduce Job 的启动过程消耗很长时间，所以不能
   用在交互查询系统中。
3. Hive**不支持事务**（因为不没有增删改，所以主要用来做 OLAP（联机分析处理），而
   不是 OLTP（联机事务处理），这就是数据处理的两大级别）。



## hive基本架构

4层架构

#### 用户接口层

- cli        命令行
- jdbc/odbc
- webui  可视化界面操作（hue，cdh）

#### 跨语言服务平台

> thift server|hiveserver2

- 保证其他语言(java pthon) 等可以操作hive

#### 核心驱动层

- 将hql语句转化为mr任务，并进行提交
  - **解释器：**hql —— 抽象语法树
  - **编译器（compiler）：**抽象语法树 —— 逻辑执行计划
  - **优化器（optimizer）：**优化逻辑执行计划
  - **执行器（executor）：**执行最终优化结果

#### 元数据层

​	hive数据存储

- 表数据|原始数据：

  表中真正存储的数据；存在hdfs

  键表   插入数据

  将表中的数据存为文件的形式；

- 元数据

  描述表数据的数据   MYSQL  -> bd1904

  - 描述数据库相关信息的数据







## hive的数据组织形式

- **库：**database

  - 便于数据精细化管理，将不同模块的数据，存储在不同的数据中

- **表：**table

  - 按照权限分，表数据的管理权限

    - **内部表|管理表** managed_table

      默认创建的都是内部表

      表数据的管理权限，hive自己所有的

      存储表数据的hdfs目录，hive具备绝对的权限（数据的删除）

      内部表在进行删除的时候，元数据和原始数据一定被删除；

    - **外部表** external_table

      `create external table stu`

  - 内外表区别：

    - 建表语句：external

    - 删除时候：本质

      内部表：元数据和原始数据一并删除

      外部表：只删除元数据

    - 应用场景：

      外部表：公共数据，好多部门同用的数据，原始采集数据；

      内部表：内部部门业务数据；

  - 按照功能分：

    - 分区表：这里的分区完全不同于mr中分区

      - 纯手动，往分区插入数据，不做校验，“读模式”

      hive中每一个表中存储的数据海量的数据；

      我们在进行查询时 `select * from stu where age=19`

      执行的全表扫描，数据量大，全表扫描严重影响查询效率；

      为了提高查询效率，将原来的表划分为不同的区域，查询的时候降低扫描范围；每一个区域就叫做分区；

      - 如何划分区域？ 根据查询需求进行分，eg：age

      分区本质上相当于把原来的表划分为多个小表；

      - 分区表的表现形式：一个分区表对应一个目录

    - **分桶表**：程序帮助我们实现，规则由我们指定；

      类似于mr中的分区

      作用：
    
      - 提升抽样性能；
        - 取某一个或几个桶中的数据；
      - 提升join性能；
      - `select * from a join b on a.id=b.id;`
        - 如果a、b都非常大，先将其分桶，id%5；

      将原属数据，按照一定的规则，分成不同的文件；

      /user/hive/warehouse/haha77.db/stu/0000

      分桶：不同粪桶，不同的文件

      /user/hive/warehouse/haha77.db/stu/0000

      /user/hive/warehouse/haha77.db/stu/0001

      /user/hive/warehouse/haha77.db/stu/0002
    
      **每一个分桶如何切分:**分桶字段.hash&Integer_max % 

- 视图

  类型：VIRTUAL_VIEM

  类似于mysql中

  hive中只存在逻辑视图，不存在物化视图；

  hive中的视图不会真正的执行；仅仅将视图中的sql语句保存

  `create view view_nameas select ...`

  视图类似于sql查询语句中的快捷方式；

  **作用：**提高sql代码的可读性；



## hive操作

### 建库

`create database if not exists dbname;`

### 切换库

`use dbname;`

### 查看正在使用的库

`select current_database();`

### 查看库列表

`show databases;`

`show databases like "test*"`

### 查看库的详细描述信息

`desc database dbname;`       描述

### 删除数据库；

`drop database if not exists;`

==不能删除非空数据库==

`drop database if exists dbname cascade;`  联级删除；

`drop database if exists dbname restricd;`  联级删除；



### hive表操作

- **建表**

  `CREATE [EXTERNAL] TABLE [IF NOT EXISTS] table_name`
  - **EXTERNAL：**外部表关键字，不加默认内部表；
  - **IF NOT EXISTS：**建表防止报错；

  - 字段类型 字段名

  - **PARTITIONED BY：**指定分区，分区标识符

  - **CLUSTERED BY：**指定分桶的；分桶字段.hash % 个数；

    `INTO 桶个数 BUCKETS;`

  - **ROW FORMAT：**指定格式化，通常情况下用于指定列之间的分隔符；

    hive加载数据；

    load	将一个本地数据或hdfs数据（文件）加载到hive中

    hive	列——文件字段；

  - **ROW FORMAT：**指定的分隔符；

  - **STORED AS：**指定hive 的表数据，在hdfs的存储格式

    textfile	默认	文本

    SEQUENCEFILE

    RCFILE



### DDL操作

#### 创建表

- 创建一个内部表

  ```sql
  CREATE TABLE IF NOT EXISTS stu_managed(id int,name string,sex string,age int,dept string)
  ROW FORMAT delimited fields terminated by ','
  stored as textfile;
  ```

- 创建一个外部表

  ```sql
  CREATE EXTERNAL TABLE IF NOT EXISTS stu_external(id int,name string,sex string,age int,dept string)
  ROW FORMAT delimited fields terminated by ','
  stored as textfile;
  ```

- 创建一个分区表

  分区字段使用查询业务中经常顾虑的字段，生产上经常采用日期；

  分区字段：dept

  ```sql
  CREATE TABLE IF NOT EXISTS stu_ptn(id int,name string,sex string,age int)
  partitioned by (dept string) 
  ROW FORMAT delimited fields terminated by ',';
  ```

  **分区字段不能与表中其他字段重复，否则报错** 
  **`FAILED: SemanticException [Error 10035]: Column repeated in partitioning columns`** 

  

- 创建一个分桶表；

  分桶字段：age  个数 3

  ```sql
  CREATE TABLE IF NOT EXISTS stu_buk(id int,name string,sex string,age int,dept string)
  CLUSTERED BY (age) SORTED BY (age DESC) INTO 3 BUCKETS
  ROW FORMAT delimited fields terminated by ',';
  ```

- 表复制

  ```sql
  create table tbname like stu_managed;
  create external table tbname like stu_managed;
  ```

  > 表复制只复制表结构，不会复制表属性；

- ctas语句建表

  ```sql
  create table tbname as select ...
  ```

#### 查看表列表

`show tables;`

#### 查看表的详细信息

`desc tb_name;`	查看字段信息；

`desc extended tb_name;` 查看字段的详细信息；

`desc formatted tb_name;` 查看表的详细信息并进行格式化显示；

#### 修改表

- 修改表的列信息

  - 修改表列名

    `ALTER TABLE tbname CHANGE cold cnew type`

    修改类型 只能 小--> 大

  - 添加列

    `ALTER TABLE tbname ADD COLUMNS (col type);`

- 修改表的分区信息

  - 添加分区

    ...../stu_ptn/dept="ss";

    `ALTER TABLE tbname ADD PARTITION(分区字段="分区值");`

    `ALTER TABLE tbname ADD PARTITION(分区字段="分区值") PARTITION(分区字段="分区值2");`

  - 修改分区

    修改分区的存储路径

    添加分区的时候直接修改

    `ALTER TABLE tbname ADD PARTITION(分区字段="分区值") LOCATION "hdfs-path";`

    修改已经存在的分区的存储路径

    `ALTER TABLE tbname PARTITION(分区字段="分区值") SET LOCATION "hdfs-path";`

  - 查询分区

    `SHOW PARTITIONS tbname;`

    分区字段只指定一个，交一级分区；指定多个，叫多级分区；

    `SHOW PARTITIONS stu_ptn PARTITIONS (高级分区);`   查看某个高级分区下的所有子分区；

  - 删除分区

    `ALTER TABLE tbname DROP PARTITION(分区字段="分区值");`

- 显示创建语句

  `SHOW CREATE TABLE tbname;`

  ```sql
  hive> show partitions stu_ptn;
  OK
  dept=pt1
  Time taken: 0.174 seconds, Fetched: 1 row(s)
  hive> show create table stu_buk;
  OK
  CREATE TABLE `stu_buk`(
    `id` int,
    `name` string,
    `sex` string,
    `age` int,
    `dept` string,
    `address` string)
  CLUSTERED BY (
    age)
  SORTED BY (
    age DESC)
  INTO 3 BUCKETS
  ROW FORMAT SERDE
    'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
  WITH SERDEPROPERTIES (
    'field.delim'=',',
    'serialization.format'=',')
  STORED AS INPUTFORMAT
    'org.apache.hadoop.mapred.TextInputFormat'
  OUTPUTFORMAT
    'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
  LOCATION
    'hdfs://bd1904/user/hive/warehouse/bd1904.db/stu_buk'
  TBLPROPERTIES (
    'last_modified_by'='shineu',
    'last_modified_time'='1565595581',
    'transient_lastDdlTime'='1565595581')
  ```

#### 清空表

​	`TRUNCATE TABLE tbname;`

​	清空表中的数据；此操作只针对于**内部表**，清空表的操作是将表对应的hdfs的目录下的文件删除；

#### 删除表

​	`DROP TABLE IF EXISTS tbname;`



### DML操作 data manage language

#### 向表中添加数据

- 将一个已经存在的文件（本地|hdfs）加载到hive中，按照hive表中指定的分割方式进行解析这个数据；

  `LOAD DATA [local] INPAT 'path' INTO TABLE tbname;`

  - **local：**代表数据来源

  - 将数据从本地加载到hive的表，就是将数据从指定的路径下**复制**到了hive表所在的路径下；
    - 测试，手动将数据上传到hive表hdfs对应的路径下，在hive中可以进行查询；

  - 将数据从hdfs加载到hive中；==移动过程==

  > load的本质将数据复制|移动到hive在hdfs中的路径下，只要数据在hive表所在的目录下，hive表就会自动解析；

​	

- insert

  - 单条数据插入，一次插入一条；

    `INSERT INTO TABLE tbname values();`

  - 单重数据插入

    一次性插入一个slq的查询结果

    将一个slq的查询结果（多条）插入到表中；

    `INSERT INTO TABLE tbname select * from stu WHERE age=19;`

  - 多重数据插入

    一次扫描表，但是最终将多个查询结果，插入到多张表中，或者一个表的多个分区中；

    ```sql
    #插入到多个表中
    FROM tbname
    INSERT INTO TABLE tb1 select...
    INSERT INTO TABLE tb2 select...
    
    FROM stu_managed
    INSERT INTO TABLE tb1 SELECT * WHERE age=18
    INSERT INTO TABLE tb2 SELECT * WHERE age>19;
    ```

  - 插入到多个分区中
    分区表中数据是分成两块存储的，
    一个是分区字段   /user/hive/warehouse/bd1904.db/stu_ptn/dept=pt1
    一个是普通字段   表数据-文件
    分区表操作的时候一定要指定分区名 partition()

    ```sql
    FROM stu_managed
    INSERT INTO stu_ptn PARTITION(dept="CS") 
    SELECT id,name,sex,age WHERE dept="CS"
    INSERT INTO stu_ptn PARTITION(dept="IS")
    SELECT id,name,sex,age WHERE dept="IS"
    ```

  ==验证：分区数据的插入方式及分桶表的数据插入方式==
  
  - 插入数据到分桶表
    
  - 推荐使用insert ... select...
    
  - **CTAS：**create table ... as select ...
  
    `CREATE TABLE stu_gt20 AS SELECT id,name,age FROM student WHERE AGE>20;`



### hive不支持语法

- delete update
- 默认情况下，不支持笛卡尔积；



#### 几种排序

- order by 全局排序

  - 实现原理：最终将这个sql翻译成的MR程序只是用一个reducetask

- sort by   局部排序（每个reducetask的结果中排序）

- distribute by  分桶 

  - 有多个文件

- cluster by       分桶

- `distribute by (id) sort by (id) = cluster by id;`

  