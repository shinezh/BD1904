# Redis

## Nosql分类

### 键值（key-value）存储数据库

- 相关产品：Tokyo Cabinet/Tyrant,**Redis**,Voldemort,Berkeley DB
- 典型应用：内容缓存，主要用于处理大量数据的高访问负载
- 数据模型：一系列键值对
- 优势：快速查询



## Redis

### 什么是Redis

> 是一个用C语言编写的`键值型`的非关系型数据库管理系统，`开源免费`的，主要应用于高速内容缓存；
>
> redis还支持数据的持久化和事务管理；
>
> reids同时支持多种数据类型，例如：string、hash、list、set、sortedset等；

### Redis的下载和安装

```shell
#1、下载文件到Linux，然后解压
tar -zxvf redis-4.0.10.tar.gz -C /opt/

#2、切换到目录，进行编译
make#编译
	#如果编译失败，则先清理，后编译
	#清理: make distclean
	
#3、安装到指定文件夹
make install PREFIX=/usr/local/redis

#4、复制redis.conf到etc
cp redis.conf /etc/

#5、配置环境变量并更新
export REDIS_HOME=/usr/local/redis
export PATH=$PATH:$JAVA_HOME/bin:$REDIS_HOME/bin
source /etc/profile
```

### 启动与暂停

```shell
#前台启动
redis-server
#新建终端，通过redis-cli连接
#关闭
ctrl+c
kill -9 pid
redis-cli -> shutdown
```

```shell
#后台启动
#1、修改redis.conf
daemonize yes #作为守护进程运行在后台

#2、不要绑定127.0.0.1
#bind 127.0.0.1

#3、设置密码
requirepass 666666

#4、后台启动
redis-server /etc/redis.conf
```

- 启动服务端

  `redis-server /etc/redis.con；`

- 客户端连接

  `redis-lic -h ip(默认127.0.0.1) -p port(默认6379)`

- 服务端关闭

  `kill -9`

  `redis-lic shutdown`

- 客户端连接

  > redis-desktop-manager 图形化连接

- java连接

  > 需要jar包 `commons-pool2-2.3.jar` `jedis-2.7.0.jar`

  ```java
  public class JedisTest {
  	//单客户端连接
  	@Test
  	public void test(){
  	    //1、获取jedis对象
  		Jedis jedis=new Jedis("192.168.3.101",6379);
  		//2、操作
  		//授权
  		jedis.auth("666666");
  		//默认读取的是0数据库
  		jedis.set("k1","v2");
  		String k1 = jedis.get("k1");
  		System.out.println(k1);
  		//3、释放
  		jedis.close();
  	}
  
  
  	/**
  	 * 连接池管理连接
  	 */
  	@Test
  	public void test2(){
  		GenericObjectPoolConfig config = new JedisPoolConfig();
  		config.setMaxTotal(100);
  		config.setMaxWaitMillis(20000);
  		config.setMinIdle(5);
  
  		//1、创建连接池，在连接池中管理连接
  		JedisPool pool = new JedisPool(config,"192.168.3.101",6379);
  
  		//2、从连接池获取jedis
  		Jedis jedis = pool.getResource();
  		jedis.auth("666666");
  		//3、操作
  		jedis.set("k1","v3");
  		System.out.println(jedis.get("k1"));
  
  		//4、归还连接
  		jedis.close();
  	}
  }
  ```

  

## Redis 数据类型

### string

```shell
#存储数据
    set key value
    mset key value [key2 value2 ...]
#获取数据
	get key
	mget key [key2 ...]   #批量获取
	getset key value      #先获取，后设值
#删除
	del key
#自减
	decr key
	decrby key decrement
#其他

```

### hash

散列类型，key扩展出来一个filed字段，value和filed之间实现映射；

**value不允许拓展，只能是string类型**

```shell
#存储数据
	hset key feild value
	hmset key filed value [filed value..]
	hsetnx keyi filed value     #若某字段无值，则设置值
#获取数据
	hget key filed value
	hmget key filed value [filed value..]
	hgetall key   #获取整个key的值
	
	hkesy key     #获取所有的字段
	hals key      #获取所有的值
#删除
	hdel keyi filed [filed2 ...]  #删除字段
#其他
	hlen key                    #字段个数
	hexists key filed           #判断字段是否存在
	hincrby key filed increment #字段实现自增
```

### list

> ArrayList和LinkedList区别：
>
> 	- 数据结构：ArrayList底层采用数组，LinkedList采用链表；
> 	- 特点：ArrayList随机访问速度快，增删慢，LikinedList相反；

```shell
#添加数据
	lpush key value [vlaue ...]   #左侧插入
	rpush key value [vlaue ...]   #右侧插入
#查看数据
	lrange key 0 -1               #-1代表结束标识
#查看长度
	llen key
#弹出数据（删除单个）
	lpop key                      #左侧弹出
	rpop key                      #右侧弹出
#删除数据
	lrem key count value          #根据值删除
	#count>0 表示从左到右删除count个value
	#count<0 表示从右到左删除count个value
	#count=0 表示删除所有value
#其他
	lindex key index              #查看指定所有的值
	ltrim key start stop          #保留指定的列表片段
	rpoplpush src dest            #从src弹出元素push到dest中
#应用场景
	#新闻评论
```

### set

> 唯一且无序

```shell
#添加元素
	sadd key member [member ..]
#查看所有元素
	smembers set01
#判断元素是否在集合中
	sismember key member
#根据元素删除值
	srem key member [member]
#弹出元素（随机）
	spop key
#获取长度
	scard key
	
#并集
	sunion key [key ..]
#交集
	sinter key [key ..]
#差集
	sdiff key [key ..]
```

### sortedset

> 也叫zset，唯一且有序；
>
> 每个元素添加了分数（权重score）

```shell
#添加元素
	zadd key score member [score member]
	#如果元素已存在，再次添加会将score更新
#查看元素
	zrange key start stop [withscore]      #按score升序排列
	srevrange key start stop [withscore]   #按score倒序排列
	zrangebyscore key min max [withscore][limit offset count]  
	#按score范围查看元素
#查看排序
	zrank key member
#删除
	zrem key member [member ..]
	zremrangebyrank key start stop
	zremrangebyscore key min max
#其他
	zsard key            #查看所有的元素数目
	zxount key min max   #查看指定socre范围内的所有元素
```



### keys命令

```shell
#按照规则查看key
keys pattern
#查看key的类型
type key
#查看key的存活时间（秒）
ttl key
#设定key的存活时间
expire key seconds         #秒
pexpire key millisecondes  #毫秒
```



## 数据持久化

### rdb模式

> 默认的持久化方案

- 持久化策略

  ```shell
  save 900 1
  save 300 10
  save 60 10000
  ```

- 持久化文件名称

  `dbfilename dump.rdb`

- 持久化文件目录(服务运行当前目录)

  `dir ./`

- 持久化原理（过程）
  1. redis服务在处理连接和持久化数据是多线程操作；
  2. redis的持久化策略一旦被触发，fork出一个子进程用于单独处理持久化；同时会拍摄当前数据库的快照，父进程处理连接问题，子进程根据快照持久化文件，等到持久化结束则销毁子进程；

- rdb问题

  rdb模式有可能都是丢失最后一次的写操作；

### aof方式

> 将每次的写操作都写到aof持久化文件中；默认关闭的；

- 开启aof的持久化方案

  `appendonly yes`

- aof持久化文件

  `appendifilename "appendonly.aof"`

- aof持久化策略

  ```shell
  #appendfsync always
  appendfsync everysec
  #appendfsync no
  ```

- rbd&aof
  - 两种模式可以同时存在；
  - 但是aof会生效；



## 主从复制

> 为了提升数据库的查询效率，将数据库的读和写进行分离；
>
> 所有的写操作在主数据库(master)中执行，所有的读操作在从数据库(slave)中执行；
>
> 为了保证数据的的一致性，主数据库需要将自己的数据复制到从数据库，这个过程称之为**主从复制**，解决单点故障问题；
>
> 主从数据库之间采用优化的二进制协议进行通信，效率非常高；
>
> 主从架构：1 master + n slave

### 搭建主从架构

- 搭建两个redis服务，1 master 1 lalve

- 启动两个redis服务

- 设置主从关系

  > 注：持久化文件；
  >
  > - 不要在同一个目录下启动；
  >
  > - 给每个redis的服务配置具体的确定的持久化文件目录
  >
  > - 如果master设置了requirepass，那么slave要连上master，需要有master的密码才行。masterauth就是用来配置master的密码，这样可以在连上master后进行认证。
  >
  >   `masterauth <master-password>`



## redis集群

### redis-cluter集群图

![redis-cluster](../pics/pic.png)

- 每个节点之间彼此相连通讯，通讯采用的是ping/pong机制（启动节点时会有两个进程，连接client，实现master之间连接）
- 整个redis集群中，每个节点有可能fail，超过半数的节点没有办法实现通信则该节点fail；
- client和redis集群之间连接没有proxy，直接连接，client连接到任意一个节点都代表连接到整个集群；
- cluster负责维护整个集群；
- 如何实现整个集群负载均衡？cluster将整个集群划分为16384个slot（hash槽），将这些slot尽可能的均匀分布到每个节点。如果客户端需要建立与集群的连接，需要采用crc16的算法计算出hash值，然后对16384进行取余，让后将其分配到指定的节点；



### 投票机制

![投票机制](../pics/1382509-20190607123913384-143688658.png)

- 某个节点是否fail

  超过一半的节点都无法跟该节点进行通信，此时该节点fail；

- 集群是否fail
  - 如果某个master节点fail，但是该节点下有slave，那么集群不会fail，如果没有slave，则集群fail；
  - 如果超过一半的master都fail，则不论是否有slave，集群fail；



### redis集群搭建

> redis节点数是奇数（3+）
>
> redis是主从架构，至少需要一主一从
>
> 每个节点的master不能和该节点的slave在同一服务器；

- 准备6个redis服务（配置并启动）

  ```shell
  de
  ```

- 搭建redis集群

  - redis-trib.rb(集群管理器)

    `cp`

  - 安装ruby环境

    ```
    yum -y install ruby
    yum -y install rubygems
    ```

  - 安装接口文件（集群管理器和redis服务之间通讯）

    ```shellgem 
    在线安装接口文件：gem install redis
    ```

  - 搭建集群

    ```shell
    redis-trib.rb --replicas 1 create host1:port1 ... hostn:portn 
    ```













