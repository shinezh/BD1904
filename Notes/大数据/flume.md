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

    

- exec UNIX command

- 数据源来自于一个unix命令执行内容的结果

- 监听一个操作文档的内容的命令 执行结果的

    - cat	数据源来自一个文件的所有内容
    - tail    数据源来自一个文件的部分内容



### sink类型

> 指定数据的目标

```properties
#hdp03 tail-avro.properties
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = exec
a1.sources.r1.command = tail -F /home/shineu/data/tmpdata/date.log
a1.sources.r1.channels = c1

# Describe the sink
a1.sinks.k1.type = avro
a1.sinks.k1.channel = c1
a1.sinks.k1.hostname = hdp04
a1.sinks.k1.port = 41414
a1.sinks.k1.batch-size = 2

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
```

```properties
#hdp04 avro-hdfs.properties
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = avro
a1.sources.r1.channels = c1
a1.sources.r1.bind = 0.0.0.0
a1.sources.r1.port = 41414

# Describe k1
a1.sinks.k1.type = hdfs
a1.sinks.k1.hdfs.path =hdfs://bd1904/testlog/flume-event/%y-%m-%d/%H-%M
a1.sinks.k1.hdfs.filePrefix = date_
a1.sinks.k1.hdfs.maxOpenFiles = 5000
a1.sinks.k1.hdfs.batchSize= 100
a1.sinks.k1.hdfs.fileType = DataStream
a1.sinks.k1.hdfs.writeFormat =Text
a1.sinks.k1.hdfs.rollSize = 102400
a1.sinks.k1.hdfs.rollCount = 1000000
a1.sinks.k1.hdfs.rollInterval = 60
a1.sinks.k1.hdfs.round = true
a1.sinks.k1.hdfs.roundValue = 10
a1.sinks.k1.hdfs.roundUnit = minute
a1.sinks.k1.hdfs.useLocalTimeStamp = true

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
```

