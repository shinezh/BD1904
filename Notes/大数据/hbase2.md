## hbase原理

- hbase架构
    - 朱从架构

### 核心概念

#### region

- 每一个表的数据，都需要进行划分多个region
- rebion对habse表的行的方向上的划分
- 一个region代表的是一个表中多行数据，一定行键范围的数据
- region是hbase进行分布式存储的最小单位
- 负载均衡的最小单位
- 一个region不可再进行分割的，在进行分布式存储的时候
- 一个region最终只能存储在一个hbase节点上（hregionserver）



- hbase在创建表的时候默认只有一个region
- 刚开始进行数据插入的时候，操作只有这一个region
- 随着数据插入，数据来嗯不断增加，当数据量达到一定大小的时候，必然进行region的split
- 一个region切分为2个region，一旦切分完成，就会面临新的region的重新分配
- hmaster决定每一个region存储在哪一个hregionserver节点上
- 原来老region会进行下线
- 每一个region需要多大split：10737418240字节 -- 10GB
- 每一个region中的一个列族的物理文件达到10GB才开始切分



- 如何切分
- 按照rowkey的中间值进行切分



- 刚开始进行hbase中表数据插入的时候，操作只有一个region
- 在这个region分裂之前，操作的都是这一个region
- 操作的只有一个regionserver
- 这种现象在另一个层面上也会叫做**数据热点**

- **如何解决**：
    - 在建表的时候，进行表预分区（region）
    - 指定多个分区



#### hbase中的数据热点

- 客户端进行数据操作（I/O）的时候，频繁操作某一个个别region，造成这个region所在的regionserver热点
- 在hbase的操作过程中，尽量避免数据热点
- 在hbase的操作过程中，尽量避免数据热点
- 根源：经常访问的数据集中分配到了某一个regionserver上



#### store

-  UPDATE mysql.user SET user='root' where host='localhost';





### hbase的寻址机制

```
hbase一个表最终被拆分为一个个region
每一个region可能会存储在不同的regionserver
无论读|写，首先快速定位到写|读在哪一个regionserver
```

- 寻址过程（0.96之前）

    1. 客户端先去访问zk

        获取-root-表的存储regionserver位置以及region编号

    2. 访问-root-表的region

        获取.meta表对应的regionserver的region编号

    3. 访问.meta表 需要查询的rk所在的region

        获取原始数据的regionserver的region编号

    4. 开始真正的访问对应的regionserver的region标数据

- 存储机制（版本>0.96）

    原始表——存储原始数据的

    .meta——原始表

- 寻址过程（）





### 数据删除

- delete







### hbase的Java API

#### HbaseConfiguration

- hbase的夹在配置文件的对象，hbase-default.xml

#### connection

- hbase的连接对象

#### hbase的两个核心操作对象

- hbaseadmin|admin

    - hbase的ddl操作的句柄对象
    - hbase的管理对象
        - namespace
        - table

- htable|table

    - hbase的dml操作句柄对象
        - 表对象：对表数据进行操作

- hbase的相关操作对象

    - DDL
        - HTableDescriptor：表描述器，指定表名-列族
        - HColumnDescriptor：列族描述器，列族名|存储属性
    - DML
        - put
        - delete
        - get
        - scan

    - 结果集
        - Result      get   一条数据
        - ResultScanner      scan 数据集 Result[]

### 过滤器

- 比较
    - row
    - family
    - quilfilter
    - value(比较操作符)
    - Filter -- FilterList
- 专用
    - singlecolumnvaluefilter
    - pagefilter



## hbase批量数据倒入方案

### API put

### mapreduce

> 将hdfs的数据导入到hbase的表中

- 将moves.dat导入到hbase中

```sql
hbase
create "movies","movie_info"
#插入数据
	put对象
	行键	唯一性	movieid
	title	type
	
mapreduce实现
mr从hdfs读取数据，将数据输出到hbase
map端
	key:movieid
	value:else
reduce端：
	将数据封装成hbase需要的数据，put到hbase中
	
```

- 对hbase的数据进行分析，分析的结果导入到hdfs上

