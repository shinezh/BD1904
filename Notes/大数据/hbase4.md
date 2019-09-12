## 课程回顾

> 2019-08-24

- 存储机制

    - 核心概念

        - region 一定rowkey范围的数据
            - 默认1个
            - 单个列族文件 大小10G 
            - 建表--预分区 唯一编号 目录
            - region -- 分布式存储 负载均衡
            - 不是无力存储的最小单位
        - Store 列族
        - memstore 内存 128M
        - storefile 磁盘文件
        - hifile文件，hbase中一种特殊文件存储格式，共6段
        - wal|hlog 预写日志|写前日志 -- 防止内存数据丢失

    - 索引机制

        - <0.96 三层索引
            - 原始
            - .meta
            - -root- 不可分割的hbase表，存在zk里
            - zk
        - \> 0.96
            - 原始
            - .meta 不可再分割
            - zk

    - 寻址机制

        - 0.96前 三次往返
        - 0.96后 两次往返

    - 读写机制

        - 0.96前
            - 三次往返获取原始数据region对应的regionserver ==》向对应的regionserver发送数据写请求 --> 进行数据校验 --> 开始写入 --> 先写到wal中，然后写到memstore --> 达到128M溢写 storefile -->  达到阈值 compact (minor  major(默认7天)) --> 判断region store是否达到分裂标准， ==》 分裂
            - 三次寻址 ==》 获取regionserver ==》 发送读请求 ==》 先在内存中读取，如果有，则直接返回，如果没有，则去磁盘的storefile中存取

    - zookeeper 作用

        - 选主
        - 寻址路径
        - rs的存活状态
        - hbase表的schema

    - hbase表设计

        - 表设计：
            - 优化 -- 预分区
            - 列族 -- 不超过3个

        - 列族
            - io属性相同
            - 不超过3个
        - 行键
            - 唯一性
            - 不能过长 0-100字节，建议8的倍数，越短越好
                - 回写到每一个storefile
                - memstore
                - 内存底层8通道
            - 散列性：防止数据热点
                - hash
                - 加salt
                - 时间戳反转
                - MD5 | uuid



## bulkload 海量数据导入

### 原理

```shell
mapreduce|put 数据导入 ==》 过程：table.put()
	文本数据 ==》 region store ==>wal
	==> memstore ==> storefile ==> hfile(hbase需要的文件格式)
```

> 将原始数据（文本数据 结构化|半结构化）直接转化为hfile格式的数据，转换完成之后再将这个hfile数据放置在hbase的对应的表的存储目录下
>
> 类似hive的load

### 优势

- 省去了中间的写入数据的复杂过程，直接得到最终结果，**效率极高**

### 如何实现

- 内部默认实现 csv ， tsv

- mapreduce 将原始文本 -- hfile

- hbase中提供一个进行数据转化的类

- `PutSortReducer`将数据进行封装为hfile需要的格式

    ```java
    public class PutSortReducer extends
        Reducer<ImmutableBytesWritable, Put, ImmutableBytesWritable, KeyValue>
    //ImmutableBytesWritable 行键
    //Put 需要插入的数据对象
    //ImmutableBytesWritable mapper输出
    //KeyValue CELL 单元格
    ```

- 最终转换

    - `FileOutputFormat`	输出文本
        - `FileOutputFormat2`	输出hfile

- 最终实现
    - Mapper 类，读取每一行文本数据
    - 封装 PutSortReducer 需要输入数据
        - key:ImmutableBytesWritable
        - value:put
    - Reducer
        - `PutSoreReducer`	对任何文本数据有效，hbase内置的，准备好hbase需要的数据
    - main函数
        - 输出：`HfileOuputFormat2`

- 以上实现，仅仅是将hdfs文本转换成hfile文件，转换完成之后，再将这个hfile数据放置在hbase的对应的表的存储目录下

