# sqoop

## 产生背景

- 最早，数据存储基于传统型数据库
- 无法满足需求
- 换成hdfs分布式存储





## 是啥

- 数据迁移
- import
- export



## 数据导入方向

- 以大数据平台为中心
- 数据迁入：import mysql|oracle ==》 hadoop平台
- 数据迁出：export hadoop平台数据 ==》 mysql|oracle



- hadoop平台(广义)
    - hdfs
    - hive
    - hbase
- hadoop 平台
    - 开源：apache社区，免费
    - 商业版
        - CDH   cloudera
        - HDP   Hortonworks
        - 星环科技  TDH



## 本质

> 命令工具
>
> 将sqoop命令进行转换为MR任务

### sqoop数据迁入

- 从mysql读取数据，将数据输出到hdfs
- map端：
    - `FileInputFormat` == 只针对文本
        - `LongWritable`    `Text`
    - 重新定义输入：`DBInputFormat`
        - 将数据库的数据，一行一行的读取过来
        - 在map端直接输出

### sqoop数据迁出

- 从hdfs读取数据，将数据迁出到mysql中
- map端
    - 输入不变
    - `FileInputFormat`  一行一行读取过来
    - 重新定义 `OutputFormat` ==> `DBOutputFormat` 





## sqoop安装

### 安装准备

- hadoop
- jdk
- zookeeper
- mysql
- hive
- hbase

### 安装节点

- 命令行工具，将命令行提交hadoop集群运行
- 只需要安装一个节点
- 这个节点：必须访问到hadoop集群



### 数据导入

#### mysql =》 hdfs

```shell
sqoop 
```





#### 增量数据导入

> 每次导入新增数据

#### 全量数据导入



### 数据导出

#### hdfs ==> mysql

```shell
sqoop export \
--connect jdbc:mysql://hdp01:3306/sqoopdb  \
--username root \
--password mysql \
--table sqoopfur \
--export-dir /user/hadoop/myimport_add \
--fields-terminated-by ','


--export-dir   指定hdfs需要导出的数据的文件夹
fields-terminated-by '\t'   指定的是hdfs上文件的分隔符

#mysql中的数据库和表都需要自己先创建
create database sqooptest;
create table 
```

