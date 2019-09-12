# yarn

## 产生背景

### hadoop1

- hdfs	分布式存储

- mapreduce 分布式计算，提供套路及计算流程；

  运行计算任务的时候有两个进程

  - jobtracker	计算老大，主节点，存在单点故障

    ​	既要负责整个集群的资源调度，一个集群中执行多个mr任务

    ​	还要负责任务启动及进度跟踪；启动mapreduce任务，再负责跟踪maptask和reducetask进度；如果集群中有多个，同时要负责跟踪多个；

  - tasktracker   计算从节点，提供资源，负责计算；

    ​	将整个节点资源强制分为2部分，**造成资源极大浪费**

    ​	mapslot   ---maptask

    ​	reduceslot ---reducetask

- 缺点：
  - 单点故障
  - 扩展性差；
  - 资源利用率低；
  - 资源调度只能为mr任务服务，计算框架的资源调度使用受限；



### hadoop2

> 将资源调度和进度跟踪 分开
>
> 任务跟踪下放到每一个任务里，每一个job启动之后，负责自己的任务跟踪；

- hdfs
- mapreduce 编程套路，逻辑
- yarn 负责资源调度

### yarn架构

- resourcemanager：主节点

  负责接收客户端的请求，提供资源调度，负责整个集群的资源调度

  - ASM：applicationsmanager，所有任务的管理

    每一个任务的启动销毁，每一个任务的进度跟踪，监控运行状态

  - Scheduler：

    调度器，决定任务的执行顺序

    FIFO：firstInFirstOut，内部维护的是单一的队列，哪一个任务先提交则哪个任务先进行分配；

    ​	缺点：如果前面有一个大任务，则会造成后面的任务堵塞；

    FARI：公平调度器，所有任务平分资源；

    ​	缺点：没有根据任务大小进行资源分配；

    CAPACITY：计算能力，容量；可以根据任务组的真实需要，手动配置资源占比；

    ​	资源栈，单一队列，FIFO

- nodemanager：从节点

  负责真正的资源提供者，为计算任务提供资源；

  需要的时候提供，运行完成回收资源，动态提供

  提供资源的时候，maptask | reducetask 单位提供

  1 maptask | 1 reducetask  --- 1 container 资源

  container 抽象资源容器，逻辑资源容器

  nodemanager提供资源的基本单位，内部封装了一定的资源（CPU 内存 磁盘 网络 IO）

  一个container运行的是一个maptask | reducetask任务

### 补充 MRAppMaster 

- MRAppMaster 对应一个应用程序，职责是：向资源调度器申请执行任务的资源容器，运行 任务，监控整个任务的执行，跟踪整个任务的状态，处理任务失败以异常情况 







## 资源调度过程

1. 客户端提交hadoop jar ... 客户端先去请求rm
2. rm会返回一个资源节点，用于启动当前应用程序的mrappmaster
3. rm会到对应的节点上启动mrappmaster
4. mrappmaster向resourcemanager进行资源申请，
5. resourcemanager进行向mrappmaster返回对应的资源节点
6. mrappmaster就会到对应的资源节点上启动maptask任务
7. maptask任务就会向mrappmaster进行汇报自己的运行状态和进度；
8. 当mrappmaster获取到有一个maptask执行完成，就开始启动reducetask（container）
9. reducetask在启动之后也会向mrappmaster进行汇报自己的状态和进度；
10. 当每一个maptask或reducetask运行完成，mrappmaster就回到对应的节点上进行资源回收
11. 当整个任务完成，mrappmaster会向resourcemanager进行汇报，并注销自己；
12. resourcemanager将整个运行结果返回给客户端





## job提交

1. client向ResourceManager发送提交job的请求；
2. RM返回job_id 以及 共享资源路径；
   - 共享资源路径内容包含：
     - job.jar运行程序
     - job.split运行的切片信息；

3. 客户端将共享资源提交到共享资源路径下；
4. 客户端向RM返回响应，共享资源放置完成；并开始真正的提交这个job；job转为应用程序application_id；

5. RM会为当前的application进行分配一个节点，用于启动当前应用程序的mrappmaster；

6. RM到对应的资源节点启动mrappmaster；

7. mrapp会到共享资源路径下下载共享资源；

8. mrapp初始化当前的应用程序，生成当前application的工作簿（进度）；

9. mrapp向RM申请maptask&reducetask；

10. RM向mrapp返回对应的资源节点；

11. mrapp到对应的资源节点启动maptask任务

12. maptask会到共享资源路径下下载共享资源

13. maptask开始运行container

14. maptask运行期间向mrapp汇报自己的进度和状态；

15. mrapp获取一个maptask运行完成到对应的节点就启动reducetask

16. reducetask到资源共享路径下下载共享资源；

17. reducetask任务开始运行；

18. reducetask运行期间向mrapp汇报自己的进度和状态；

19. maptask|reducetask运行完成，mrapp就会运行资源回收；

20. 整个application运行完成，mrapp向RM汇报，注销自己并进行资源释放；向客户端返回运行结果；

    

## 各角色作用

### ResourceManager

- 接收客户端请求，job提交
- 接收mrappmaster请求
- 进行资源分配和调度
- 接收nodemanager心跳
- 监控nodemanager资源使用状况；

### nodemanager

- 接收RM的命令，
- 接收mrappmaster的命令；
- 向RM定期发送心跳
- 负责监控自身的资源使用状况