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

- **事件：**数据采集的单位，flume数据传输的**基本单位**
- 一条数据称为一个event
- header{k=v}|body{value}

### agent

- 代理，真正的数据采集点

- 一个 Agent 包含 source， channel， sink 和其他组件。
- 它利用这些组件将 events 从一个节点传输到另一个节点或最终目的地
- agent 是 flume 流的基础部分。
- flume 为这些组件提供了配置，声明周期管理，监控支持

### source

- 指定数据源的

### channel

- 指定通道的
- 缓存

### sink

- 目标

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



## 架构

### OG

- collector - master



### NG

- source -- channel -- sink



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

    - 数据源来自于一个UNIX命令执行内容的结果

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

- hdfs回滚条件

    ```properties
    # 等待时间
    hdfs.rollInterval = 30
    # 文件大小
    hdfs.rollSize = 1024
    # 数据条数
    hdfs.rollCount = 10
    # 文件空闲时间
    hdfs.idleTimeout = 0 
    ```

    



## 拦截器

> 拦截数据源，给数据源进行初步处理，可以给数据源添加一个标识
>
> 添加标识是添加到了数据的header信息
>
> 可以在sink端进行不同标识数据的不同处理方式

### 时间戳：Timestamp Interceptor

```properties
a1.sources = r1
a1.channels = c1
a1.sources.r1.channels = c1
a1.sources.r1.type = seq
a1.sources.r1.interceptors = i1
a1.sources.r1.interceptors.i1.type = timestamp
```



### 主机名 Host Interceptor

> 拦截数据源，添加主机名，添加到header中，header{host=IP|host name}

```properties
a1.sources = r1
a1.channels = c1
a1.sources.r1.interceptors = i1
a1.sources.r1.interceptors.i1.type = host
#显示hostname
a1.sources.r1.interceptors.i1.useIP = false 
```

```properties
# Finally, now that we've defined all of our components, tell
# agent1 which ones we want to activate.
# 指定别名
a1.channels = c1
a1.sources = r1
a1.sinks = s1
a1.interceptors = i1

a1.sources.r1.interceptors.i1.type = host
#显示hostname
a1.sources.r1.interceptors.i1.useIP = false 

# Define a memory channel called ch1 on agent1
# 指定channel
a1.channels.c1.type = memory
 
# Define a logger sink that simply logs all events it receives
# and connect it to the other end of the same channel.
# 指定sink类型，logger控制台打印
a1.sinks.s1.channel = c1
a1.sinks.s1.type = logger
 
# Define an Avro source called avro-source1 on agent1 and tell it
# to bind to 0.0.0.0:41414. Connect it to channel ch1.
a1.sources.r1.channels = c1
a1.sources.r1.type = netcat
a1.sources.r1.bind = localhost
a1.sources.r1.port = 41414
a1.sources.r1.interceptors = i1
```



### 静态拦截器 Static Interceptor

> 这个拦截器可以手动指定 k==v

```properties
a1.sources = r1
a1.channels = c1
a1.sources.r1.channels = c1
a1.sources.r1.type = seq
a1.sources.r1.interceptors = i1
a1.sources.r1.interceptors.i1.type = static
a1.sources.r1.interceptors.i1.key = datacenter
a1.sources.r1.interceptors.i1.value = NEW_YORK
```

```properties
# a1  当前这个agent的名字
a1.sources = r1
a1.sinks = k1
a1.channels = c1
a1.interceptors = i1

# 指定source   来自于端口的
a1.sources.r1.type = netcat
a1.sources.r1.bind = localhost
a1.sources.r1.port= 41414

# 指定channel
a1.channels.c1.type = memory
 
# 指定sink的类型  logger 控制台打印
a1.sinks.k1.type = logger

# 绑定 channel   sink   source 
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1

```



## 综合案例

> A、 B 两台日志服务机器实时生产日志主要类型为 access.log、 nginx.log、 web.log
> 现在要求：
> 把 A、 B 机器中的 access.log、 nginx.log、 web.log 采集汇总到 C 机器上然后统一收集到 hdfs
> 中。
> 但是在 hdfs 中要求的目录为：
> /source/logs/access/20160101/**
> /source/logs/nginx/20160101/**
> /source/logs/web/20160101/**

### 分析

- agent 1 | 2 

    - source：r1 r2 r3exec	tail -f file

    - channel：memory
    - sink：avro

- agent 3
    - source：avro
    - channel：memory
    - sink：hdfs

```properties
#agent 1 | 2
# Name the components on this agent
a1.sources = r1 r2 r3
a1.sinks = k1
a1.channels = c1

# Describe/configure the source r1 access.log
a1.sources.r1.type = exec
a1.sources.r1.command = tail -F /home/shineu/data/tmpdata/testlogs/access.log
a1.sources.r1.interceptors = i1
a1.sources.r1.interceptors.i1.type = static
a1.sources.r1.interceptors.i1.key = filename
a1.sources.r1.interceptors.i1.value = access

# Describe/configure the source r1 nginx.log
a1.sources.r2.type = exec
a1.sources.r2.command = tail -F /home/shineu/data/tmpdata/testlogs/nginx.log
a1.sources.r2.interceptors = i1
a1.sources.r2.interceptors.i1.type = static
a1.sources.r2.interceptors.i1.key = filename
a1.sources.r2.interceptors.i1.value = nginx

# Describe/configure the source r3 web.log
a1.sources.r3.type = exec
a1.sources.r3.command = tail -F /home/shineu/data/tmpdata/testlogs/web.log
a1.sources.r3.interceptors = i1
a1.sources.r3.interceptors.i1.type = static
a1.sources.r3.interceptors.i1.key = filename
a1.sources.r3.interceptors.i1.value = nginx


# Describe the sink
a1.sinks.k1.type = avro
a1.sinks.k1.hostname = hdp01
a1.sinks.k1.port = 41414

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 20000
a1.channels.c1.transactionCapacity = 10000

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sources.r2.channels = c1
a1.sources.r3.channels = c1
a1.sinks.k1.channel = c1
```

```properties
# hdp01 汇总点
#定义agent名， source、channel、sink的名称
a1.sources = r1
a1.sinks = k1
a1.channels = c1


#定义source
a1.sources.r1.type = avro
a1.sources.r1.bind = hdp01
a1.sources.r1.port =41414


#定义channels
a1.channels.c1.type = memory
a1.channels.c1.capacity = 20000
a1.channels.c1.transactionCapacity = 10000

#定义sink
a1.sinks.k1.type = hdfs
a1.sinks.k1.hdfs.path=hdfs://bd1904/testlog/%{filename}/%y-%m-%d/
# 文件前缀
a1.sinks.k1.hdfs.filePrefix =events
# 定义文件输出类型 - 数据流
a1.sinks.k1.hdfs.fileType = DataStream
# 定义最终输出格式 - 文本格式
a1.sinks.k1.hdfs.writeFormat = Text

#时间类型
a1.sinks.k1.hdfs.useLocalTimeStamp = true

# 设置回滚条件
# 等待时间
hdfs.rollInterval = 0
# 文件大小
hdfs.rollSize = 10240
# 数据条数
hdfs.rollCount = 0
# 文件空闲时间
hdfs.idleTimeout = 30

#组装source、channel、sink
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
```



## 高可用配置





## 多路复制&多路复用

### 多路复制

同一个数据源，复制多份给多个channel，最终到达不同的sink



### 多路复用

同一个数据源，分别到不同的channel