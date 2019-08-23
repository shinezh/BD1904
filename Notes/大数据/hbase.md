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




## 设计思想

- 随机（近）实时查询
- hbase 分布式nosql数据库，数据量很大的
- 调表结构，创建多层索引，布隆过滤器
- hbase 最多二层索引，无论多大都不会进行切分；



## hbase特点

- 对zookeeper的依赖性极强
    - 选主
    - 寻址机制，最上层索引表的存储hbase节点位置
- 大：bigtable，一个表可以有上十亿行，上百万列
- 面向列：严格意义上说，是面向“列簇”

- 稀疏：hbase中null不进行存储的
- **无模式**：无严格模式--表结构不严谨
    - **读模式**：数据读取的时候，进行数据校验
        - hive | load
    - **写模式：**数据写入的时候，进行数据校验
        - mysql

- **数据库特点：**
    - 不能支持join
    - 所有的数据，在底层存储都是byte[]



## hbase表结构

- **行键**：rowkey
    - 一行的标识，不同的行键不同
    - 相同行键的数据属于同一行
    - hbase中，索引的时候按照行键进行索引
    - hbase中的行键默认会按照字典顺序进行生序排序，便于创建索引
- 列族|列簇：column family
    - hbase中一个列簇存储一个物理文件，面向列簇存储
    - 列簇属于hbase的表结构的一部分
    - 列簇的设计依据：
        - 通常情况下将具有相同io属性（读|写）的列放在同一个列簇下
        - 列簇不建议过多，最多不超过3个

- **列**：column
    - 表数据的一部分
    - 每一行的列名和对应的值，插入数据是指定的
    - 每一个列，都属于一个列簇
    - 定位一个列，指定列簇
- **数据版本|时间戳：**version
    - 存储每一个列数据的时候，

- **单元格：**cell
    - hbase表中，每一个列的每一个值存储在一个单元格中
    - 定位一个单元格：
        - 指定行键 --> 列簇 ==》 列 ==》 时间戳 ==》 单元格



## hbase使用

### shell

- 启动hbase客户端：`hbase shell`

    - help：查看帮助文档

    ```shell
    COMMAND GROUPS:
    Group name: general
    Commands: status, table_help, version, whoami
    status	#查看集群的状态信息
    version	#hbase的版本信息
    whoami	#查看当前用户
    help "commend"	#查看命令帮助
    
    Group name: namespace	#类似于传统关系型数据库的database
    Commands: alter_namespace, create_namespace, describe_namespace, drop_namespace, list_namespace, list_namespace_tables
    #hbase的namespace相关操作，弱化了database的概念；默认情况下有一个namespace供客户端操作 default
    
    #创建namespace
    create_namespace 'ns1'
    #查看namespace
    list_namespace
    #查看指定namespace描述信息
    describe_namespace 'ns1'
    #删除指定namespace
    drop_namespace 'ns1'
    #查看指定namespace下的表
    list_namespace_tables
    
    Group name: ddl
    Commands: alter, alter_async, alter_status, create, describe, disable, disable_all, drop, drop_all, enable, enable_all, exists, get_table, is_disabled, is_enabled, list, locate_region, show_filters
    
    #建表
    hbase> create 'test_shell', 'info1', 'info2', 'info3'
    #简写，所有属性默认值，建表于default namespace中
    create "bd1904:test_shell","info1","info2"	#在bd1904中创建表
    
    hbase> create 'test_shell02', {NAME => 'info01', VERSIONS => 1, TTL => 2592000, BLOCKCACHE => true},{NAME => 'info02', VERSIONS => 2, TTL => 2592000, BLOCKCACHE => true}
    #指定属性
    #name:列簇名
    #version =》指定数据版本，默认1
    #TTL 过期时间，时间戳，默认forever
    
    hbase> create 'test_shell03', {NAME => 'info2', CONFIGURATION => {'hbase.hstore.blockingStoreFiles' => '10'}}
    
    #查看表列表
    list
    #查看所有的表列表
    list_namespace_table
#查看表信息
    describe "bd1904:test_shell"     
    {NAME => 'info1', BLOOMFILTER => 'ROW', VERSIONS => '1', IN_MEMORY => 'false', KEEP_DELETED_CELLS => 'FALSE', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FOREV
    ER', COMPRESSION => 'NONE', MIN_VERSIONS => '0', BLOCKCACHE => 'true', BLOCKSIZE => '65536', REPLICATION_SCOPE => '0'}                                    
    {NAME => 'info2', BLOOMFILTER => 'ROW', VERSIONS => '1', IN_MEMORY => 'false', KEEP_DELETED_CELLS => 'FALSE', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FOREV
    ER', COMPRESSION => 'NONE', MIN_VERSIONS => '0', BLOCKCACHE => 'true', BLOCKSIZE => '65536', REPLICATION_SCOPE => '0'}                                    
    #修改表
    	#修改表属性
    	alter "bd1904:test_shell",{NAME=>"info1", VERSIONS=>2}
    	#删除列簇
    	alter 'ns1:t1', NAME => 'f1', METHOD => 'delete'
    	alter 'ns1:t1', 'delete' => 'f1'
    	alter 'bd1904:test_shell',NAME => 'info1',METHOD => 'delete'
    
    #表的启用和禁用
    #hbase中表的状态有两种：
    	#启用：ENABLED		
    	#禁用：DISABLED
    	disable		#禁用表
    		disable 'bd1904:test_shell'
    	disable_all	#禁用所有
    		disable_all 'namespace'
    	is_disabled	#查看表是否被禁用，返回布尔值
    		is_disabled 'bd1904:test_shell'
    	
    	enable
    	enable_all
    	is_enabled
    	
    #删除表
    	#删除表之前先禁用表
    	drop 'bd1904:test_shell'
    	drop_all 'bd1904:.*'
    	
    Group name: dml
    Commands: append, count, delete, deleteall, get, get_counter, get_splits, incr, put, scan, truncate, truncate_preserve
    #数据插入
    	put 'ns1:t1', 'r1', 'c1', 'value'
    	put 'bd1904:test_shell','rk001','info1:name','ls'
    	put 'bd1904:test_shell','rk001','info1:age','18'
    	put 'bd1904:test_shell','rk001','info2:address','shenzhen'
    
    #查询单条数据 get
    	get '表名','行键'
    	get 'user_info','rk001'
    		#指定版本信息查询，定位一个单元格
    		get '表名', '行健', {COLUMN => '列族：列', TIMESTAMP => 时间戳}
    		get "user_info","rk0001",{COLUMN => "base_info:name",TIMESTAMP=> 1565468018012}
    		
    #数据扫描   scan
    	scan 'test_shell'	全表扫描
    	#指定需要扫描的列
    	scan 'test_shell',{COLUMS => ['info1:age','info2:name']}
    	#指定需要扫描的起始行键，从起始键开始扫描
    	scan "user_info",{COLUMNS => "base_info:name",STARTROW => "rk0001"}
    	#指定需要扫描的结束行健   
    	scan 't1', {COLUMNS => ['c1', 'c2'], STARTROW =>"", ENDROW => 'xyz'}
    eg:
    	scan "user_info",{COLUMNS => "base_info:age",STARTROW => "rk0001",ENDROW => "zhangsan_20150701_0005"}
    	
    	#指定从起始行健开始  需要返回的数据条数    LIMIT=>5 
    	eg:
    	scan "user_info",{COLUMNS => ["base_info:name","base_info:age"],LIMIT => 5}
    	#默认从第一条数据开始扫描的
    	scan "user_info",{COLUMNS => ["base_info:name","base_info:age"],STARTROW=> "rk0001",LIMIT => 5}
    
    	#指定时间戳范围的数据   效率不高  所有时间戳范围内的数据
    	scan 't1', {COLUMNS => 'c1', TIMERANGE => [1303668804, 1303668904]}
    	scan "user_info",{COLUMNS => "base_info:age",TIMERANGE => [1565468004512,1565468005371]}
    	
    	#hbase中数据查询的方式：
        1）全表扫描
        2）指定rowkey范围进行扫描
        3）单条数据查询
    ```
    
    

