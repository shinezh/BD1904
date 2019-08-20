# hbase

## 产生背景

### GOOGLE搜索

- 海量网页数据存储 ------  GFS
- 海量网页数据的计算 ----  MAPREDUCE
- 快速随机查询  -----------  bigtable



## hbase是什么

> 海量数据 快速随机查询，分布式数据库

### 面向列

- mysql|Oracle 面向行的存储，一行一个单元

- hbase 面向‘列’，单列or多列

### nosql的分布式数据库

- no sql 不支持标准sql

  hbase中单独提供一套自己的操作语法

- not only sql

  通过第三方工具，可以提供标准sql编程

  hbase最终数据存储在hdfs上，



## 安装

### 安装准备

- jdk
- hadoop
- zookeeper

### 安装节点

- 分布式
- 角色：主节点 2Hmaster   从节点 Hregionserver



### 安装

- 上传，解压

- 修改环境变量

  ```shell
  export HBASE_HOME=/opt/hbase
  export PATH=$PATH:$HBASE_HOME/bin
  ```

  