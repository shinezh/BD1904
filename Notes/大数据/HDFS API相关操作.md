## 相关类及相应方法

### FileSystem

> 一个通用文件系统的**抽象基类**。它可以作为分布式文件系统实现，也可以作为反映本地连接磁盘的本地文件系统实现。
>
> 由于该类为**抽象类**，故没有办法直接创建该类的对象，但该类提供了静态方法用于创建对象，分别为：

| **方法**                                               | **描述**                                       |
| :----------------------------------------------------- | ---------------------------------------------- |
| get(Configuration conf)                                | 根据conf获取具体的文件系统对象                 |
| get(URI uri,  Configuration conf)                      | 基于uri和conf创建文件系统对象                  |
| get(URI uri,  Configuration conf, String user)         | 基于uri，conf和user获取文件系统                |
| getLocal(Configuration  conf)                          | 获取本地文件系统                               |
| newInstance(Configuration  conf)                       | 返回唯一的文件系统对象，该方法总是返回新的对象 |
| newInstance(URI uri, Configuration  conf)              | 基于uri返回新的文件系统对象                    |
| newInstance(URI uri,  Configuration conf, String user) | 基于uri，conf和user获取文件系统                |
| newInstanceLocal(Configuration  conf)                  | 返回新的本地文件系统对象                       |











