# hive高级应用

## hive shell操作

### 进入hive之后的

```shell
Command Description
quit	#退出客户端

set key=value #在hive的客户都安设置配置
set This will print a list of configuration variables that are overridden by user or hive.
set -v 	#打印所有hive参数

add FILE [file] [file]* Adds a file to the list of resources
add jar jarname

list FILE list all the files added to the distributed cache
list FILE [file]* Check if given resources are already added to distributed cache

! [cmd] Executes a shell command from the hive shell

dfs [dfs cmd] Executes a dfs command from the hive shell

[query] Executes a hive query and prints results to standard out

source FILE 在hive客户端加载脚本；
```

### 设置参数

```shell
-i 从文件初始化 HQL
-e 从命令行执行指定的 HQL
-f 执行 HQL 脚本
-v 输出执行的 HQL 语句到控制台
-p <port> connect to Hive Server on port number
-hiveconf x=y（Use this to set hive/hadoop configuration variables）
-S：表示以不打印日志的形式执行命名操作
```





## hive转换流程

- hive本质，将hql语句转换为一个个的hql的操作符（operator），操作符根据语句的关键词提取的，select,group by,聚合函数,order by ,limit,having

- operator操作符
  - mapreduce操作 group by 
  - hdfs的读写操作 fetch Operator hdfs

### hive典型语句转MR

#### group by + 聚合函数

- map端：
  - key：group by 的字段
  - value：需要聚合的字段
- reduce端：
  - 相同的一组的数据分到一起
  - 求聚合结果  sum|avg|count|max|min...

- 默认 group by + 聚合函数一起使用是有优化，在map端执行combiner



#### order by 全局排序  |  sort by 局部排序





#### join
默认情况下，join过程执行的 mapjoin | reduce join
在hive中，默认有一个表是小表的时候，执行mapjoin
小表限制：`set hive.`


##### mapjoin
- 将一个小表加载到缓存中，读取到内存中map
- map端map函数读取另一个大表，进行关联
- 大 * 小


## hive数据倾斜
> 数据倾斜：

### 不容易产生数据倾斜的情况
- map join
- sum|max|min + group by

#### 哪些操作不执行MR
- map join
- select 原始字段
- FILTER where, LIMIT

#### 容易产生数据倾斜的情况
- reduce join
- group by 不和聚合函数一起
- count(distinct) 数据量大的时候容易数据倾斜

### 典型场景分析

#### join过程

- 大量的关联键为NULL的时候

  users	用户表	UserId

  logs	  日志表	所有用户浏览网站的信息	UserId null

  `select \* from users a join logs b on a.UserId=b.UserId;`

  ```sql
  map
  	key:
  		UserId
  	value: 标识+其他
  shuffle:
  	分区
  	排序
  	分组
  reduce：
  	一个分区 -- reducetask
  	null 其他-- 1个reducetask
  解决方案：
  1、将null值进行分散到多个reducetask中
  	null+随机数
  	select * from users a join logs b on a.userid=nvl(b.userid,b.userid+rand())
  2、null值不参与关联，最后将null值拼接到最后
  	select * from users a join
  	(select * from logs where userid is not null) b
  	on a.userid=b.userid
  	union 
  	select null,null,null,* from logs where userid is null;
  
  ```

- 关联键 类型不统一

  users	userId int

  logs	  userId string

  `select * from users a join logs b on a.userid=b.userid` 

  解决方案：修改数据类型，将数据类型统一

- 大数据 * 大数据

  reduce join

  key值分布不均匀，分区数据不均匀，数据倾斜；

  - hive中的所有类型join方法

    - 小\*小  map join

    - 大\*小  map join（小文件《23.8M）

    - 大\*中  内存能够承受，默认执行reduce join，为了效率，也为了解决数据倾斜，强制执行mapjoin

      `/*+mapjoin(需要强制加载到内存中的小表)*/`

      ```sql
      select 
      /*+mapjoin(a)*/*
      from users a join logs b on a.userid=b.userid;
      ```

    - 大\*大  （两个表都非常大，无法放在内存）

      ```sql
      users	500G	用户表，每当有一个用户注册就有一条数据
      logs	1T		日志表，每一个点击行为，一条数据，一天分析一次
      
      解决方案：
      	对其中一个表进行瘦身----users
      	只需要过滤出logs能够关联的userid
      	1、求logs表中有哪些userid
      	select distinct userid from logs;
      	2、对users表进行瘦身
      	create table user_final as
      	select /*+mapjoin(a)*/b.*
      	from
      	(select distinct userid from logs) a
      	join users b on a.userid=b.userid;
      	3、进行真正的关联
      	select 
      	/*+mapjoin(a)*/*
      	from user_final a join logs b on a.userid=b.userid; 
      	
      ```

      