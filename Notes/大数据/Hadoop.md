# Hadoop

## 什么是Hadoop

-  是 Apache 旗下的一套**开源软件平台**

## Hadoop的核心组件

- **Common** （基础 功能 组件）（工具包，RPC 框架）JNDI 和 RPC
- **HDFS**       （Hadoop Distributed File System 分布式文件系统）
- **YARN**       （Yet Another Resources Negotiator 运算资源调度系统）
- **MapReduce** （Map 和 和 Reduce 分布式运算编程框架）



## Hadoop的发行版





## Hadoop生态圈

## hadoop集群搭建

### hdfs集群搭建

| 节点                   | 作用                                                         |
| ---------------------- | ------------------------------------------------------------ |
| namenode(nn)           | 存放元数据信息，存储文件名称、属性、块列表，所在的dn的节点列表 |
| datanode(dn)           | 存放具体的block的校验文件                                    |
| secondarynamenode(2nn) | 降低namenode压力，辅助作用                                   |

### yarn组成

- ResourceManager(rm)：整个集群的资源调度

- NodeManager(nm)：



| hdp01           | hdp02         | hdp04       | hdp05       |
| --------------- | ------------- | ----------- | ----------- |
| namenode        | datanode      | datanode    | datanode    |
|                 | secondarynode |             |             |
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

  





