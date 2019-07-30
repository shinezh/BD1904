

# Hadoop

## 什么是Hadoop

-  是 Apache 旗下的一套**开源软件平台**

## Hadoop的核心组件

- **Common** （基础 功能 组件）（工具包，RPC 框架）JNDI 和 RPC
- **HDFS**       （Hadoop Distributed File System 分布式文件系统）
- **YARN**       （Yet Another Resources Negotiator 运算资源调度系统）
- **MapReduce** （Map 和 和 Reduce 分布式运算编程框架）



## Hadoop的发行版

* apache hadoop: 2008年,初学者入门,简单易入手
* cloudera hadoop: 2009年,企业中使用 CDH，性能强
* hortonworks hadoop: 2011年,文档全面

## Hadoop生态圈

## hadoop集群搭建

### hdfs集群搭建

| 节点                   | 作用                                                         |
| ---------------------- | ------------------------------------------------------------ |
| namenode(nn)           | 存放元数据信息，存储目录树、文件名称、属性、块列表，所在的dn的节点列表 |
| datanode(dn)           | 存放具体的block数据，校验文件                                |
| secondarynamenode(2nn) | 降低namenode压力，辅助作用                                   |

### yarn组成

- ResourceManager(rm)：整个集群的资源调度

- NodeManager(nm)：单个节点的资源调度



| hdp01           | hdp02         | hdp04       | hdp05       |
| --------------- | ------------- | ----------- | ----------- |
| namenode        | datanode      | datanode    | datanode    |
| datanode        | secondarynode |             |             |
| resourcemanager | nodemanager   | nodemanager | nodemanager |





### 配置文件

- core-site.xml

  ```xml
  <!--
  	1、使用分布式
  	2、
  
  -->
  <configuration>
   <property> 
       <!--临时文件的目录-->
          <name>hadoop.tmp.dir</name>
          <value>/opt/hadoop-2.7.7/data</value>
          <description>Abase for other temporary directories.</description>
     </property>
      
     <property>
        <!--默认的文件系统，默认值：file:///本地文件系统
  		  hdfs://ip:port  hdfs集群的入口地址
  		--> 
          <name>fs.default.name</name>
          <value>hdfs://hdp01:9000</value>
     </property>
  </configuration>
  ```

- hdfs-site.xml

  ```xml
  <configuration>
  	<!-- 配置文件副本数 -->
      <property>
          <name>dfs.replication</name>
          <value>3</value>
      </property>
      
      <!--namenode元数据存储目录-->
      <property>
          <name>dfs.namenode.name.dir</name>
          <value>/opt/hadoop-2.7.7/hadoopdata/name</value>
      </property>
  
      <!--namenode的block信息存储目录-->
      <property>
          <name>dfs.datanode.data.dir</name>
          <value>/opt/hadoop-2.7.7/hadoopdata/data</value>
      </property>
      
      <!--secondarynode节点配置-->
      <property>
          <name>dfs.namenode.secondary.http-address</name>
          <value>hdp02:50090</value>
      </property>
  </configuration>configuration>
  ```

- mapred-site.xml

  ```xml
  <!--mapreduce作业运行的平台-->
  <property>
  	<name>mapreduce.framework.name</name>
  	<value>yarn</value>
  </property>
  ```

- yarn-sit.xml

  ```xml
  <property>
  	<name>yarn.resourcemanager.hostname</name>
  	<value>hadoop04</value>
  </property>
  <property>
  	<name>yarn.nodemanager.aux-services</name>
  	<value>mapreduce_shuffle</value>
  </property>
  ```

  





## Federation机制

### 问题

- 集群拓展问题，单点故障，每个namenode的内存和磁盘受损，业务之间干扰问题，性能问题，成为集群拓展的瓶颈；



### 解决





### 好处

- 集群便于拓展；
- 业务分离干扰减少
- 性能更好



### 缺点

- 不能解决单点故障问题







## HDFS

### 设计思想

- 分而治之：大文件分布存储到各个几点
  - 分散存储
  - 冗余备份

### hdfs架构（主从架构）

- 适合一次写入，多次读出，不支持修改，但是支持追加；

  

#### namenode 主节点

- 保存元数据信息，包含目录树、名称、属性、文件块列表、块所在的节点列表等；
- 接收客户端的请求和响应
- 接收datanode汇报（节点状态，快状况），传递命令给datanode

#### datanode 从节点

- 存储具体的block信息，block的校验文件

- 真正执行读写操作的地址；
- 定期汇报节点的block状态；

#### secondarynamenode

- 辅助namenode，减轻压力；

#### 优点

- 可构建在廉价机器上
  通过多副本提高可靠性，提供了容错和恢复机制
- 高容错性
  数据自动保存多个副本，副本丢失后，自动恢复
- 适合批处理
  移动计算而非数据，数据位置暴露给计算框架
- 适合大数据处理
  GB、TB、甚至 PB 级数据，百万规模以上的文件数量，10K+节点规模
- 流式文件访问
  一次性写入，多次读取，保证数据一致性

#### shell操作

```shell
hadoop fs   #运行fs   
hdfs dfs    #运行fs的命令




```



#### jar包中默认设置

- core-default.xml
- hdfs-default.xml
- mapred-defalut.xml
- yarn-default.cml

#### 自定义配置（classpath：默认加载）

- 

#### 设置

- 自定义设置文件不自动加载（没有使用默认名称/没有放到指定位置）

  conf.addResource("")

- conf.set(name,value)



### FileSystem 文件系统

> 代表client和hdfs的连接

- 如何获得fs对象

  uri：代表hdfs文件系统的访问路径（默认file:///）

  conf：配置文件

  FileSystem.get(uri,conf,user);

- 调用api

  fs.mkdirs()

  fs.copyFromLocalFile()

  fs.copyToLocalFile()

- 关闭连接

  fs.close;

### 流式数据访问

- fs.open()       --> InputStream
- fs.create()     --> OutputStream

- IOUtils.copyBytes()  --