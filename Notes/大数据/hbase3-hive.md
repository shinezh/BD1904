## hbase和hive的整合

- hbase nosql分布式数据库
    - 实时随机查询
    - 没有分析函数
- hive 数据仓库
    - 表结构  -- hdfs 数据结构映射
    - 数据分析 函数

- 将hbase和hive进行整合，便于对habase的数据做统计分析

### 整合步骤

- hive读取hbase中的数据，将hbase中的数据转换成二维表数据

- hive-hbase-handler-2.3.2.jar

- hive进行整合hbase的核心包

- hive会将hbase中的数据进行压平

    ```sql
    hbase:
    user_info
    	base_info	extra_info
    rk1	name:zs
    	age:12
    rk2	name:ls
    	sex:f		address:bj
    
    压成3个字段 全表
    rowkey-->string	base_info --> map	extra_info--> map
    --------------------
    rk01			{name:zs,age:12}	{}
    rk02			{name:ls,sex:f}		{address:bj}
    
    =================
    对部分字段进行提取 
    rowkey-->string	
    base_info:name	--> string
    base_info:age	--> int
    extra_info:add	--> string
    
    ```

### 实例

#### hbase端

- 创建hbase表

    `create 'mingxing',{NAME => 'base_info',VERSIONS => 1},{NAME => 'extra_info',VERSIONS => 1}`

- 插入准备数据

    ```shell
    put 'mingxing','rk001','base_info:name','huangbo‘
    put 'mingxing','rk001','base_info:age','33'
    put 'mingxing','rk001','extra_info:math','44'
    put 'mingxing','rk001','extra_info:province','beijing'
    put 'mingxing','rk002','base_info:name','xuzheng'
    put 'mingxing','rk002','base_info:age','44'
    put 'mingxing','rk003','base_info:name','wangbaoqiang'
    put 'mingxing','rk003','base_info:age','55'
    put 'mingxing','rk003','base_info:gender','male'
    put 'mingxing','rk004','extra_info:math','33'
    put 'mingxing','rk004','extra_info:province','tianjin'
    put 'mingxing','rk004','extra_info:children','3'
    put 'mingxing','rk005','base_info:name','liutao'
    put 'mingxing','rk006','extra_info:name','liujialin
    ```

#### hive端

- 进入hive，进行参数设置
- 指定 hbase 所使用的 zookeeper 集群的地址：默认端口是 2181，可以不写
    `set hbase.zookeeper.quorum=hdp01，hdp02,hdp03,hdp04;`

- 指定 hbase 在 zookeeper 中使用的根目录
    `set zookeeper.znode.parent=/hbase;`

- 加入指定的处理 jar
    `add jar /opt/apache-hive-1.2.1-bin/lib/hive-hbase-handler-1.2.1.jar;`

- 创建基于 HBase 表的 hive 表：

    - 所有列簇

        ```sql
        create external table mingxing
        (rowkey string, 
         base_info map<string, string>, 
         extra_info map<string, string>) 
        row format delimited fields terminated by '\t' 
        stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
        with serdeproperties ("hbase.columns.mapping" = ":key,basicinfo:,extrainfo:") 
        tblproperties ("hbase.table.name" = "mingxing");
        ```

    - 部分列簇部分列：

        ```sql
        create external table mingxing1
        (rowkey string, name string, province string) 
        row format delimited fields terminated by '\t' 
        stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
        with serdeproperties ("hbase.columns.mapping" = ":key,basicinfo:name,extrainfo:province") 
        tblproperties ("hbase.table.name" = "mingxing");
        ```

    > org.apache.hadoop.hive.hbase.HBaseStorageHandler：处理 hive 到 hbase 转换关系的处理器
    > hbase.columns.mapping：定义 hbase 的列簇和列到 hive 的映射关系
    > hbase.table.name：hbase 表名



## hbase相关原理





### 列族设计

- 将具有相同io属性的列放在同一个列族中

- 列族不宜过多，最多不超过3个

    列族过多会产生跨文件访问

### 行键设置

- **原则：**
    - 唯一性
    - 不宜过长 0-100byge 最好是8的倍数
        - **原因**：
            - 行键存储在每一个列族文件中，storefile，如果太大，造成大量的磁盘空间浪费
            - 行键信息也会写入到每一个store的memstore中，如果行键过大，会造成内存空间的极大浪费
            - 计算机的底层存储 - 8通道
    - **散列性**：
        - 