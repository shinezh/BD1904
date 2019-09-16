# SparkCore-RDD编程

## 1. Spark程序执行过程

### 1.1 WordCount案例程序的执行过程





### 1.2 Spark作业运行架构原理图

![spark作业运行架构](../pics/TIM%E5%9B%BE%E7%89%8720190912093523.png)

## RDD操作

### 2.1 RDD的初始化

> 原生API提供两种操作，一种就是读取文件`textFile()`，一种就是加载一个scala集合`parallelize`，当然也可以通过`transformation`算子来创建RDD；



### 2.2 RDD操作

> 需要知道RDD操作算子的分类，基本上分为两类，`transformation`和`action`，当然更加细致的分，可以分为输入算子，转换算子，行动算子，缓存算子；

![img](../pics/spark-4.png)

#### 2.2.1 transformation转换算子

##### Map

- 说明：rdd.map(func) : RDD   对RDD中的每一个元素都作用一次该`func`函数，所有返回值为生成的元素构成的新的RDD

- 编码：对rdd中的每一个元素×7,变成一个新的元素
- 数据在加载过程中，被分到多个分区中的方式，采用range分区
- rdd计算过程中，shuffle的分区方式， 采用hash分区

![map计算](../pics/1568256686424.png)

##### flatmap

- **说明：**

    `rdd.flatMap(func) : RDD`   rdd集合中的每一个元素，都要作用`func`函数，返回0个或多个新的元素，这些新的元素共同构成一个新的RDD，所以和上述map算子进行总结：

    - 

    ```scala
    def flatMapOps(sc:SparkContext): Unit ={
    		val list = List(
    			"hello world",
    			"hi tomorrow",
    			"nononononono"
    		)
    		val listRDD = sc.parallelize(list)
    		listRDD.flatMap(line => line.split("\\s+")).foreach(println)
    	}
    ```



##### filter

- **说明**

    `rdd.filter(func):RDD`   对rdd中的每一个元素操作func操作，该函数的返回值为Boolean类型，保留返回值为True的元素

    ```scala
    def flatMapOps(sc:SparkContext): Unit ={
    		val list = List(
    			"hello world",
    			"hi tomorrow",
    			"nononononono"
    		)
    		val listRDD = sc.parallelize(list)
    		listRDD.flatMap(line => line.split("\\s+")).foreach(println)
    	}
    ```

##### sample

- **说明**

    `rdd.sample(withReplacement:Boolean,fraction:Double[,seed:Long])`   ==> 抽样，需要注意的是，spark的sample抽样不是一个精确的抽样。一个非常重要的作用就是：看rdd中数据的分布情况，根据数据分布的情况，进行各种调优与优化；

    - withReplacement：抽样的方式，有放回抽样(true)or无放回抽样(false)
    - fraction：抽样比例，取值范围（0-1）
    - seed：抽样的随机数种子

- 编码

    ```scala
    //从10万个数中抽取千分之一
    def sampleOps(sc:SparkContext): Unit ={
        val listRDD = sc.parallelize(1 to 100000)
        val smapleRdd = listRDD.sample(true,0.001)
        println("sample空间的元素个数："+smapleRdd.count())
    }
    ```

##### union

- **说明**

    rdd1.union(rdd2)，联合rdd1和rdd2中的数据，形成一个新的rdd，相当于sql中的unionall

- **编码**

    ```scala
    def unionOps(sc:SparkContext): Unit ={   
        val listRDD1 = sc.parallelize(List(1,2,3,4,5))   
        val listRDD2 = sc.parallelize(List(1,6,7,8,9))   
        val unionRDD = listRDD1.union(listRDD2)   
        unionRDD.foreach(println)
    }
    ```

##### join

- **说明**

    join就是SQL中的`inner join`，join的效果总共7种

    ![sqljoin](../pics/sql-join.png)

- 从具体写法有如下几种：

    - 交叉链接

        `A a accross join B b` ==》这种操作方式会产生**笛卡尔积**，工作中一定要避免

    - 内连接

        `A a [inner] join B b [on | where]` ==》 有时候也写成`A a,B b`,等值连接，就是获取A和B的交集；

    - 外连接

        - **左外连接：**以左表为主体，查找右表中能够关联上的数据，如果关联不上则现实==null==
        - **右外连接**
        - 全连接
        - 半连接



##### groupByKey

1. 说明\- aggregateByKey







##### aggregateByKey

- **说明：**

- **总结：**如果是对相同类型的数据进行聚合统计，倾向于使用aggregateByKey更为简单，但是如果聚合前后数据类型不一致，建议使用combineByKey；如果初始化操作较为复杂，也建议使用`combineByKey`



#### 2.2.2 action行动算子

> 所有的这些算子都是在rdd上的partition上执行的，不是在driver本地执行的；

##### foreach





##### count

##### take

> 如果rdd的数据是有序的，那么take(n)就是TopN

##### first

> take(n)中比较特殊的一个take(1)

##### collect

> 字面意思就是收集，



##### reduce

> reduce是一个action操作，reduceByKey是一个transformation，reduce是对rdd执行聚合操作，返回一个值

```scala

```



##### countByKey

> 统计key出现的次数



##### saveAsTextFile

> 本质上是`saveAsHadoopFile()`



##### saveAsHadoopFile

##### saveAsNewHadoopFile

> 这两者的主要区别就是`outputformat`的区别；

```scala
//接口
org.apache.hadoop.mapred.OutPutFormat
//抽象类
org.apache.hadoop.mapreduce.OutPutFormat
//建议使用抽象类
```



##### saveAsObjectFile和saveAsSequenceFile

#### 2.2.3 持久化操作

##### 什么是持久化

##### 如何进行持久化

> 持久化的方法就是`rdd.persist()`或者`rdd.cache()`

##### 持久化策略

> 可以通过`persist(StoreageLevle的对象)`来制定持久化策略

| 持久化策略          | 含义                                                         |
| ------------------- | ------------------------------------------------------------ |
| MEMORY_ONLY（默认） | rdd中的数据，以未经序列号的java对象格式，存储在内存中，如果内存不足，那就剩余部分不持久化。使用的时候，未持久化的部分重新加载；这种效率最高，但是是对内存要求最高； |
| MEMORY_ONLY_SER     | 比`MEMORY_ONLY`多了一个ser序列化，保存在内存中的数据是经过序列化之后的字节数组，同时每一个Partition此时就是一个比较大的字节数组； |
| MEMORY_AND_DISK     | 和`MEMORY_ONLY`相比，内存若存储不下，则存储到磁盘中；        |
| MEMORY_AND_DISK_SER | 和`MEMORY_AND_DISK`相比，多了序列化                          |
| DISK_ONLY           | 都保存在磁盘，效率太低，一般不用                             |
| xxx_2               | 就是上述策略后面加了一个\_2，就多了一个`replicate`(备份)，所以性能会下降；但是容错或者高可用加强了；所以需要再二者之间做权衡；如果说要求数据备份高可用，同时容错的时间花费比重新计算小，此时可以使用，否则一般不用； |
| HEAP_OFF            | 使用非Spark的内存，也即堆栈内存；比如`Tachyon、Hbase`等内存，来补充Spark数据的缓存； |

##### 如何选择

- 首选默认，但是对空间要求较高；
- 如果空间满足不了，退而求其次，选择`MEMORY_ONLY_SER`，此时性能还是蛮高的；
- 如果内存满足不了，选择`MEMORY_AND_DISK_SER`，因为走到这一步，说明数据蛮大的，要想提高性能，关键是就是基于内存的计算，所以应该尽可能在内存在存储数据；

#### 持久化和非持久化的差异

> 持久化后效率更高



### 2.3 共享变量

#### 2.3.1 boradcast广播变量

> 原先为每一个task都拷贝一份变量，此时将num包装成一个广播变量，只需要在executor中拷贝一份，

```scala
val num:Any = xxx
val numBC:Broadcast[Any] = sc.broadcast(num)
//调用
val n = numBC.value
//需要注意的一点，显然num需要进行序列化
```

```scala
object _02BroadCast {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
			.setAppName("ActionSpark")
			.setMaster("local[*]")

		val sc = new SparkContext(conf)

		val genderMap=Map(
			"0" -> "Girl",
			"1" -> "Boy"
		)

		val stuRDD =sc.parallelize(List(
			Student("01","白普州","0",18),
			Student("02","伍齐城","0",19),
			Student("03","曹小佳","1",19),
			Student("04","刘文浪","1",20)
		))
		stuRDD.map(stu => {
			val gender = stu.gender
			Student(stu.id,stu.name,genderMap.getOrElse(gender,"ladyBoy"),stu.age)
		}).foreach(println)

		println("==========\n使用广播变量=============")
		val genderBC:broadcast.Broadcast[Map[String, String]] = sc.broadcast(genderMap)
		stuRDD.map(stu => {
			val gender = genderBC.value.getOrElse(stu.gender,"ladyBoy")
			Student(stu.id,stu.name,gender,stu.age)
		}).foreach(println)

		sc.stop()
	}
}

case class Student(id:String,name:String,gender:String,age:Int)
```



#### 2.3.2 accumulator累加器

- **说明：**accumulator累加器的概念和mr中出现的counter计数器的概念有异曲同工之妙，对默认具备某些特征的数据进行累加；累加器的一个好处是，不需要修改程序的业务逻辑来完成数据累加，同时也不需要额外的触发一个action job来完成一个累加；反之，必须要添加新的业务逻辑，必须要出发一个新的action job来完成；显然这个accumulator的操作性能更佳 ；

- **使用**

    ```scala
    //构建一个累加器
    val accu = sc.longAccumulator()
    ```

    

- **注意**

    - 累加器的调用，必须在action触发之后

    - 多次使用同一个累加器，应该尽量做到用完即重置

        `accumulator.reset()`

    - 尽量给累加器指定name，方便在web-ui上面查看；

    - 如果有多个累加器，则应该使用**自定义累加器**；

    ```scala
    import org.apache.spark.util.AccumulatorV2
    import scala.collection.mutable
    
    /**
      * 自定义累加器
      * IN 指的是accmulator.add(sth.)中sth.类型
      * OUT 指的是value返回值的类型
      */
    class MyAccumulator extends AccumulatorV2[String,Map[String,Long]]{
    
        private var map = mutable.Map[String,Long]()
    
        /**
    	  * 当前累加器是否有初始化
    	  * 如果为一个long值，则0为初始化值;list -》 Nil ；map -》 Map（）
    	  * @return
    	  */
        override def isZero: Boolean = true
    
        override def copy(): AccumulatorV2[String, Map[String, Long]] ={
            val accu = new MyAccumulator
            accu.map = this.map
            accu
        }
    
        override def reset(): Unit = map.clear()
    
        /**
    	  * 分区类累加
    	  * @param word
    	  */
        override def add(word: String): Unit = {
            if(map.contains(word)){
                val newWord = map(word)+1
                map.put(word,newWord)
            }
            else {
                map.put(word,1)
            }
    
        }
        /**
    	  * 分区间累加
    	  * @param other
    	  */
        override def merge(other: AccumulatorV2[String, Map[String, Long]]): Unit = {
            other.value.foreach{case (word,count) =>
                map.put(word,map.getOrElse(word,1))
            }
        }
    
        override def value: Map[String, Long] = map.toMap
    }
    ```

    



## 3. 高级排序





























