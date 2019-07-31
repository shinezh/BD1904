## 相关类及相应方法

### FileSystem

> 一个通用文件系统的**抽象基类**。它可以作为分布式文件系统实现，也可以作为反映本地连接磁盘的本地文件系统实现。

#### 主要方法

| 方法                | 返回值                                                       | 描述                     |
| ------------------- | ------------------------------------------------------------ | ------------------------ |
| copyFromLocalFile() | void                                                         | 从本地复制文件到hdfs系统 |
| copyToLocalFile()   | void                                                         | 从hdfs系统复制文件到本地 |
| create()            | [FSDataOutputStream](https://hadoop.apache.org/docs/r2.6.0/api/org/apache/hadoop/fs/FSDataOutputStream.html) |                          |





