# zookeeper

## 背景

分布式情景下，如何实现数据一致性？

### 强一致性

- 写入什么就读出什么；例如从任意节点写入，其他节点即时读出；
- 集群中只有一个节点，一致性最强；
- 集群中节点个数越多，强一致性越难保证；



### 弱一致性

- 写入什么，尽量保证读到什么结果
- 不保证最终读取的结果一定是对的



### 最终一致性

- 弱一致性的特殊情况，
- 允许一定的时间延迟；





## 安装

- 安装准备
  - JKD 1.8+

- 安装节点：奇数台，至少3台，3--11

- 步骤：
  - 上传
  - 安装
  - 配置环境变量
  - 修改zookeeper配置文件
  - 添加id文件，在dir文件下创建myid文件，存储当前节点的id
    - myid不要有多余空格和换行符
  - 远程发送安装包到其他节点



## zookeeper主从结构

### 全新集群的选主

- 启动顺序

- 第一个节点hdp01启动时，会寻找集群中的leader，发现没有leader，发起选主，但只有一个节点，hdp01会给自己投票，但没有过半，选主失败，此时hdp01无状态
- hdp02启动，寻找leader，发起选主，开始投票，zk内部规定，id小的节点强制将自己的票投给id大的，此时hdp02获取两票，过半，成为leader
- hdp03启动，寻找leader，发现已有leader；



## zookeeper文件系统

- zk的文件系统类似Linux，以 / 开始
- 访问只能以绝对路径，不能用相对路径；
- zk中没有文件的概念，也没有目录的概念，只有znode节点，既有文件功能，又有目录功能

### znode分类

```java

PERSISTENT(0, false, false)
PERSISTENT_SEQUENTIAL(2, false, true),
EPHEMERAL(1, true, false),
EPHEMERAL_SEQUENTIAL(3, true, true);
```







- 按照生命周期
  - 临时节点	`create -e 节点路径 节点存储内容`
    - 临时节点只对当前客户端生效，当前客户端退出，临时节点被zk删除；
    - 临时节点不可以有子节点
  - 永久节点    `create 节点路径 节点存储内容`
    - 对所有客户端生效
    - 想要删除必须手动删除
    - 永久节点可以有子节点
    - 有子节点的节点一定是永久节点

- 按照有无编号分
  - 有编号节点 `create -s znode `
    - 编号由父节点维护，从0开始顺序递增（无论有无编号，序号都会递增）
    - 同一个节点，可以重复创建，每次都是不同编号
  - 无编号节点 `create path data`
    - 无编号节点只能创建一次

- 注意：
  - 每个节点创建之前必须只能声明周期



- 对于zk来说，有几个几点，数据就会被存储几份
- zk中每一个节点存储的数据都是一致的
- 只要在一个节点进行操作，其他节点自动同步



- zk中znode存储数的时候，每一个znode存储的数据不要超过1M，最好不要超过1kb



- zk中监听对象必须是znode



### 监听机制

> 客户端对某一个znode感兴趣，这个时候可以对这个znode添加监听；

- **监听事件**
  - 子节点变化	nodechildrenchanged
  - 内容变化        nodedatachanged
  - 节点被创建    nodecreated
  - 节点被删除    nodedeleted

- **添加监听**

  `shell watch`

  ```shell
  ls path watch	#子节点变化    nodechildrenchanged
  get path watch	#节点内容变化  nodedatachanged
  exists          #nodecreated
  ```

- 触发监听
  - create
  - delete | rmr
  - set path data



- zk的核心就是用监听机制去监听文件系统，保证分布式数据的一致性；



### 测试监听

- 子节点变化

  - ls path watch

  - ls /bb watch

    ```shell
    WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/bb
    Created /bb/cc
    ```

- ==监听只监听一次，生效一次==





## 常用命令

- 创建节点

- 查看节点

  - 查看节点内容 `get path watch`

  - 查看子节点     `ls path`

- 修改节点内容        `set path data`

- 删除节点                
  - `delete path` 空节点，么有字节点的节点
  - `rmr data`

- 查看状态信息      `stat path` 





## 应用场景

- 配置文件管理；
  - 管理分布式集群中的配置文件
  - 实现配置文件管理



## zookeeper原理

### zookeeper角色

- leader
  - 发起提议
  - 接收客户端的读写请求，主要处理写请求
  - 具有选举权和被选举权
  - 时刻具备最新数据
- follower
  - 接收客户端的读写请求，如果接收到客户端的写请求，将写请求转发给leader
  - 读请求可以自己处理
- observer
  - 配置 server.4=hdp04:2888:3888: observer
  - 不具备选举权和被选举权
  - **作用**：分担集群读数据压力



- 整个写入数据过程中，leader最先写入，后follower|observer从leader进行数据同步
- 若在follower|observer 在数据同步中





```shell
cZxid = 0x300000004    #数据版本ID，越大版本越新
ctime = Wed Aug 07 20:47:29 PDT 2019
mZxid = 0x300000004    #修改节点事件ID，全局的，修改节点内容时会发生变化
mtime = Wed Aug 07 20:47:29 PDT 2019
pZxid = 0x300000005    #子节点变化事件ID，
cversion = 1
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 5
numChildren = 1
```

- cZxid mZxid pZxid 整体无论修改了哪一个，都会全局顺序递增，上面三个id共同标识
- 只要zxid最大，则表明这个机器中的数据是最新的；





### 非全新集群选主

> 集群运行一段时间后，leader宕机，集群的重新选主；

- 依据：
  - myid
  - 数据版本 zxid
  - 逻辑时钟，投票轮数

- 现根据逻辑时钟，若逻辑时钟不统一，则会先统一逻辑时钟；
- 统一完成逻辑时钟，按照zxid，zxid大的胜出
- 在zxid大的里面选myid大的胜出；



### 集群数据同步过程

> 选完 leader 以后，zk 就进入状态同步过程。

- leader 等待 server 连接；
- follower 连接 leader，将最大的 zxid 发送给 leader；
- leader 根据 follower 的 zxid 确定同步点；
- 完成同步后通知 follower 已经成为 uptodate 状态；不再接收客户端的读数据请求；
- follower 更新完成数据，将自己状态修改为updated；收到 uptodate 消息后，又可以重新接受 client 的请求进行服务了。



### 写数据过程

1. 客户端写入zk数据时候，请求最终被leader进行处理；
2. leader进行数据写入
3. leader写入数据成功，不代表数据真的写入成功，当follower|observer进行数据同步
4. 当过半的follower更新数据成功，leader将这条数据标记为真正的可读数据；剩下的机器自己慢慢更新；



### 配置文件管理





## zk的应用场景

### 配置文件管理

### 集群管理

- 集群节点的上下线
- 集群选主

### 命名服务

- 分布式下的全局统一命名

### 锁管理

- **读锁：**共享锁，所有线程都可以公用这把锁，
- **写锁：**排他锁，只允许一个线程进行操作
- **时序锁：**按照先后顺序获取锁，通过编号

### 队列管理

