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