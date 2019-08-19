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

