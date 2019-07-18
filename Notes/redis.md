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

- hash

  散列类型，key扩展出来一个filed字段，value和filed之间实现映射；

  **value不允许拓展，只能是string类型**

```shell
#存储数据
	hset key filed value
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

