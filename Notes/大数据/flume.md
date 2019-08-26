# flume

> 数据采集
>
> 将多个服务器上的

## 数据来源

### 自己的业务数据

- 订单数据
- 用户信息等

### 爬虫爬取的数据

### 第三方 征信

### 网站日志数据 flume收集



## flume核心概念

### event--事件

- **事件：**数据采集的单位，flume数据传输的基本单位
- 一条数据称为一个event



### agent

- 一个 Agent 包含 source， channel， sink 和其他组件。
- 它利用这些组件将 events 从一个节点传输到另一个节点或最终目的地
- agent 是 flume 流的基础部分。
- flume 为这些组件提供了配置，声明周期管理，监控支持

### source

- 指定数据源的

### channel

- 指定通道的

### sink

- 指定数据的目的地的，用的比较多，hdfs上



## flume的部署

### 单个agent

### 多个agent串联

### 多个agent合并串联

### 多路复用

### 高可用





## 安装

- 上传解压
- 环境变量一般不配置
- 修改flume的配置文件
- `flume-env.sh`修改`JAVA_HOME`



## 配置文件

- example.conf

    ```properties
    # Finally, now that we've defined all of our components, tell
    # agent1 which ones we want to activate.
    # 指定别名
    agent1.channels = ch1
    agent1.sources = avro-source1
    agent1.sinks = log-sink1
    
    # Define a memory channel called ch1 on agent1
    # 指定channel
    agent1.channels.ch1.type = memory
     
    # Define a logger sink that simply logs all events it receives
    # and connect it to the other end of the same channel.
    # 指定sink类型，logger控制台打印
    agent1.sinks.log-sink1.channel = ch1
    agent1.sinks.log-sink1.type = logger
     
    # Define an Avro source called avro-source1 on agent1 and tell it
    # to bind to 0.0.0.0:41414. Connect it to channel ch1.
    agent1.sources.avro-source1.channels = ch1
    agent1.sources.avro-source1.type = netcat
    agent1.sources.avro-source1.bind = localhost
    agent1.sources.avro-source1.port = 41414
    ```

- 启动

- `bin/flume-ng agent --conf ./conf/ -f conf/example.conf -Dflume.root.logger=DEBUG,console -n agent1`

- bin/flume-ng agent -c conf -f conf/example.conf -n agent1 -
    Dflume.root.logger=INFO,console

- ```shell
    --conf-file	指定配置文件的路径
    --name		指定agent的别名，一定要和配置文件保持一致
    -Dflume.root.logger
    
    bin/flume-ng agent -c conf -f conf/example.conf -n agent1 -Dflume.root.logger=INFO,console
    
    bin/flume-ng agent -c conf -f conf/example.conf -n agent1 - Dflume.root.logger=DEBUG,console
    
    ```



- 向指定的端口发送数据



### source 类型

- **netcat**：数据来自于主机的网络端口数据

    - 一旦指定的主机端口有数据，就会被作为数据源采集

    ```properties
    agent1.sources.avro-source1.type = netcat
    agent1.sources.avro-source1.bind = 指定监控的主机
    agent1.sources.avro-source1.port = 指定监控的端口
    ```

    

- **exec UNIX command**

    - 数据源来自于一个unix命令执行内容的结果

    - 监听一个操作文档的内容的命令 执行结果的
    - cat	数据源来自一个文件的所有内容
        - tail    数据源来自一个文件的部分内容

- **Spooling Directory**   spooldir

  - 数据源来自于一个文件夹下的所有文件 

  ```properties
  # a1  当前这个agent的名字
  a1.sources = r1
  a1.sinks = k1
  a1.channels = c1
  
  # 指定source   来自于端口的
  a1.sources.r1.type = spooldir
  a1.sources.r1.spoolDir = /home/hadoop/datas
  a1.sources.r1.fileSuffix = .finished
  
  # 指定channel
  a1.channels.c1.type = memory
   
  # 指定sink的类型  logger 控制台打印
  a1.sinks.k1.type = logger
  
  # 绑定 channel   sink   source 
  a1.sources.r1.channels = c1
  a1.sinks.k1.channel = c1
  ```

- **avro**

    - 数据源来自于  avro port 多agent串联
    - 一个agent 向另一个agent 发送数据的时候,agent1--avro port-- agent2 

    ```properties
    案例：
    规划：
    agent1  hadoop01   source -- netcat       channel--memory   sink--avro  
    agent2  hadoop02   source -- avro source  channel--memory   sink--logger 
    
    agent1 :
    # a1  当前这个agent的名字
    a1.sources = r1
    a1.sinks = k1
    a1.channels = c1
    
    # 指定source   来自于端口的
    a1.sources.r1.type = netcat
    a1.sources.r1.bind = hadoop01
    a1.sources.r1.port= 44455
    
    # 指定channel
    a1.channels.c1.type = memory
     
    # 指定sink的类型  logger 控制台打印
    a1.sinks.k1.type = avro
    a1.sinks.k1.hostname = hadoop02
    a1.sinks.k1.port = 44466
    
    # 绑定 channel   sink   source 
    a1.sources.r1.channels = c1
    a1.sinks.k1.channel = c1
    
    agent2:
    # a1  当前这个agent的名字
    a1.sources = r1
    a1.sinks = k1
    a1.channels = c1
    
    # 指定source   
    a1.sources.r1.type = avro
    a1.sources.r1.bind = hadoop02
    a1.sources.r1.port= 44466
    
    # 指定channel
    a1.channels.c1.type = memory
     
    # 指定sink的类型  logger 控制台打印
    a1.sinks.k1.type = logger
    
    # 绑定 channel   sink   source 
    a1.sources.r1.channels = c1
    a1.sinks.k1.channel = c1
    
    
    多个agent串联  启动顺序  从后向前
    先启动agent2:  hadoop02
    $ ./flume-ng agent --conf conf --conf-file /home/hadoop/app/apache-flume-1.8.0-bin/conf/ex03_agent02_avrosource.conf --name a1 -Dflume.root.logger=INFO,console
    
    启动 agent1   hadoop01 
    $ ./flume-ng agent --conf conf --conf-file /home/hadoop/app/apache-flume-1.8.0-bin/conf/ex03_agent01_avrosink.conf --name a1 -Dflume.root.logger=INFO,console
    ```

    

### channel类型

- **memory** channel   数据存储内存中

  ```properties
  a1.channels.c1.type = memory
  a1.channels.c1.capacity = 10000  memory中存储的数据的最大条数
  a1.channels.c1.transactionCapacity = 10000  每次提交的数据量
  ```

- **file** channel  基于磁盘的
- **jdbc**   数据库
- **kafka** 





### sink类型

- **avro：**指定数据的目标

```properties
a1.sinks.k1.type = avro 
a1.sinks.k1.hostname = 指定绑定的主机  一般指定下一个的agenT的主机
a1.sinks.k1.port= 指定数据存放的端口
```

- **logger：**将结果打印到控制台中

- **hdfs：**将结果收集到hdfs中

```properties
a1.sinks.k1.type = hdfs
a1.sinks.k1.hdfs.path = 指定hdfs 的路径 
```

```properties
案例：
source exec    channel--memory  sink -- hdfs 
# a1  当前这个agent的名字
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# 指定source   来自于端口的
a1.sources.r1.type = exec
a1.sources.r1.command = tail -f /home/hadoop/datas/2.txt

# 指定channel
a1.channels.c1.type = memory
 
# 指定sink的类型  logger 控制台打印
a1.sinks.k1.type = hdfs
a1.sinks.k1.hdfs.path = /flume/data

# 绑定 channel   sink   source 
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
 
启动：
$ ./flume-ng agent --conf conf --conf-file /home/hadoop/app/apache-flume-1.8.0-bin/conf/ex04_hdfs_sink.conf --name a1 -Dflume.root.logger=INFO,console

#hdfs文件：
FlumeData.1566811391386
FlumeData.1566811477603.tmp  还没有编辑完成

#文件回滚的条件：
hdfs.rollInterval	30	30s  0 参数失效
hdfs.rollSize	1024	文件大小  字节  0 参数失效
hdfs.rollCount	10	events的数量达到10开始回滚  0 参数失效

#满足一个就可以回滚的
a1.sinks.k1.hdfs.path = /flume/events/%y-%m-%d/%H%M/%S
```

