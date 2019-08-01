# Mapreduce基础

## 三道面试题

1. 一个超大文件，里面存储url，一行一个，求出现次数最多的url

   > 思路：
   >
   > 如果是小文件---> 记录每个url出现的次数---> 找出次数最多的
   >
   > 1. 定义一个输入流，读取文件
   > 2. 创建一个map记录读取，map<url，count>
   >    - 判断key是否存在，若存在则count+1，否则key，1
   > 3. 找出count最大的key

   - 大文件：

     将文件进行切分，对每个快文件进行统计，最后将每个块的统计结果进行汇总；

     汇总时可能需要多台机器进行处理，通过hash将map结果平均分发到机器进行处理，保证每台机器处理的数据唯一，然后求单台机器中的最大值，比较两台机器中的最值，得出最终值；



2. 有两个超大文件，里面存储IP，每行一个，求两个文件中相同的IP

   > 思路：小文件
   >
   > 1. 定义两个输入流和两个set集合
   > 2. 分别读取
   > 3. 循环遍历set，另一个set.countains()，如果包含则打印

   - 大文件

     将两个大文件进行分区，通过**hash取余**均分，保证相同ip位于同一区块，然后两两比较；

     **分区思想**：不一定相等，但必须是倍数关系



3. 一个超大文件，里面存储URL，一行一个，求用户给定的URL，如何**快速**判断文件中是否存在；

   布隆过滤器   计数排序

   快速查询

   数组-位数组-通过位数组下标进行查询
   
   存在误判问题：数组长度、数据量、hash算法的个数
   
   

## 单机版WordCount

> 词频统计，统计本地**5个文件**中每个单词出现的总次数；

### 思路

- 分别读取每个文件中每个单词出现的次数

  readOneFile(String path)：用于读取并统计每一个文件

  - 定义一个字符输入流BufferedReader;
  - 定义一个容器，Map集合，key放单词，value放词频
  - 开始进行读取，计数

- 汇总5个Map集合，定义方法 mergeAllResult(Map<> ... maps)  可变数组

  - 循环获取每一个map集合；
  - 将每一个map集合中相同的Key进行value加法

```java
/**
 * count words in one file
 *
 * @param path file path
 * @return a map ,key = word, value = count
 */
public static Map readOneFile(String path) {
    Map<String, Integer> map = new HashMap();
    try {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\t");
            for (int i = 0; i < words.length; i++) {
                if (map.containsKey(words[i])) {
                    map.put(words[i], map.get(words[i]) + 1);
                } else {
                    map.put(words[i], 1);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return map;
}
```

```java
/**
 * merge all the map results
 * @param maps single map
 * @return the final map result
 */
public static Map mergeAllResults(Map<String, Integer>... maps) {
    Map<String, Integer> map = new HashMap<>();
    for (int i = 0; i < maps.length; i++) {
        for (String key : maps[i].keySet()) {
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + maps[i].get(key));
            } else {
                map.put(key, maps[i].get(key));
            }
        }
    }
    return map;
}

```

```java

public class Driver {
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
		//1、获取集群配置文件
		Configuration conf = new Configuration();
		//2、启动job，构建一个job对象
		Job job = Job.getInstance(conf);

		//3、进行job封装
		job.setJarByClass(Driver.class);

		//指定map和reduce对应的类
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);

		/*
		指定map输出的 k - v 类型
		框架读取文件 ==> mapper  ==>  reducer
		泛型的作用周期：编译时生效，运行时自动擦除
		*/

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		//指定reduce输出的类型，指定最终输出
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);


		//指定输入路径
		FileInputFormat.addInputPath(job,new Path(args[0]));

		//指定输出路径
		FileOutputFormat.setOutputPath(job,new Path(args[1]));

		//完成，true ==> 并打印日志
		job.waitForCompletion(true);

	}
}
```







> 大数据中的计算----数据量大
>
> 1. 必须先切分数据，
> 2. 针对每一个小数据分别进行运算
> 3. 汇总计算
>
> 大数据中的分布式计算
>
> 1. 分而治之
> 2. 汇总统计



### 什么是Mapreduce

- 分布式计算的一种逻辑思维
- 将分布式计算，封装成要给框架
- 这个框架包含
  - Mapper --> map     分而治之
  - Reducer - > reduce 统计结果



## hadoop中的wordcount实现

> hadoop-mapreduce-examples-2.7.7.jar 封装mapreduce的样板案例
>
> hadoop jar jar包路径 jar包中需要运行的主类 参数
>
> hadoop jar hadoop-mapreduce-examples-2.7.7.jar wordcount /input /output
>
> `/output 一定不能存在`

输出结果：

- \_SUCCESS   0KB    成功标志文件

- part-r-00000         结果文件



### WorldCount.class

1. 分而治之，一个类继承Mapper，重新map方法
2. 



## mapreduce的运行方式

- 打jar包运行，不便于前期调试
- 本地运行，程序没有被提交到集群
- 本地运行，将代码提交到集群



## mapreduce的编程套路

### 文件解析

- 文件加载
  - FileInputFormat --> TextInputFormat

- 文件读取

  - RecordReader -->  

    - LineRecordReader

      ```java
      //偏移量
      public LongWritable getCurrentKey() {
              return this.key;
          }
      
      //一行内容
      public Text getCurrentValue() {
              return this.value;
          }
      ```

### !mapper

### shuffle 洗牌-混洗

> 将map输出的数据进行顺序调整；为reduce做数据准备；

- 分区：Partitioner

- 排序：按照map key相同的key在一起
  - WritableComparable
- 分组：将key相同的分到一组
  - WritableComparator

- 局部聚合：Combiner，减少到reduce端的数据量

### !reducer

### 文件写出

- FileOutputFormat 
  - --> TextOutputFormat （ 默认文件写出）
    - --> RecordWriter --> LineRecordWriter
    - 默认分隔符 \t



## maptask的并行度

- **maptask**：运行mapper端逻辑的任务

- **并行度**：有多少个maptask需要一起运行
  - 在进行运行的时候需要差分多少个任务
  - 一个task是job的最小单位
  - 一个task只能在一个节点运行
- **相关因素**
  - 大数据在存储时必然有数据跨节点，必然面临数据的跨节点传输，降低计算效率
  - 一个任务对应的数据，最合理的应该就是和hdfs数据存储的块的大小一致  128M
  - 这里的每一个任务只会被分配到每一个节点的一小部分资源；
  - 一个节点上可以执行多个任务--包含maptask|reducetask

#### 切片（SPLIT）

- 事实上，一个mapstask任务对应的数据量是一个**切片（SPLIT）**的大小
  - 一个切片大小理论上等于一个block块的大小；

- 切片是逻辑上的概念，代表的仅仅是一个范围的划分，针对maptask的计算

- 每一个maptask对应一个逻辑切片的数据；

  ```java
  protected long computeSplitSize(long blockSize, long minSize, long maxSize) {
  		return Math.max(minSize, Math.min(maxSize, blockSize));
  	}
  //最终 splitSize = blockSize 默认大小相同
  //修改
  //若希望	splitSize > blockSize 修改minSize
  //		  splitSize < blockSize 修改maxSize
  //mapreduce.input.fileinputformat.split.maxsize
  //mapreduce.input.fileinputformat.split.minsize
  ```

- 修改splitSize

  - 修改配置文件：mapred-site.xml -- 不推荐

  - 代码中修改：

    ```java
    FileInputFormat.setMaxInputSplitSize(job,100*1024*1024); 
    FileInputFormat.setMinInputSplitSize(job,200*1024*1024);
    ```

- **源码补充**
  - 如果文件大小小于blocksize大小，则独立成一个切片；

  - 若一个文件是130M，对应几个切片？

    blk0	0-128M

    blk1	129-130M

    ```java
    /*
    * 循环切分
    * (double) bytesRemaining / (double) splitSize > 1.1D
    * */
    for (bytesRemaining = length; (double) bytesRemaining / (double) splitSize > 1.1D; bytesRemaining -= splitSize) {
        blkIndex = this.getBlockIndex(blkLocations, length - bytesRemaining);
        splits.add(this.makeSplit(path, length - bytesRemaining, splitSize, blkLocations[blkIndex].getHosts(), blkLocations[blkIndex].getCachedHosts()));
    }
    //若最后一个切片大小小于 1.1blocksize，则为1.1*blockSize =140.8M
    if (bytesRemaining != 0L) {
        blkIndex = this.getBlockIndex(blkLocations, length - bytesRemaining);
        splits.add(this.makeSplit(path, length - bytesRemaining, bytesRemaining, blkLocations[blkIndex].getHosts(), blkLocations[blkIndex].getCachedHosts()));
    }
    ```

    







## hadoop中的自定义类型







## reducetask的并行度



