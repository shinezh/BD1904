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

     将两个大文件进行分区，通过hash取余均分，保证相同ip位于同一区块，然后两两比较；

     **分区思想**：不一定相等，但必须是倍数关系



3. 一个超大文件，里面存储URL，一行一个，求用户给定的URL，如何**快速**判断文件中是否存在；

   布隆过滤器   计数排序

   快速查询

   





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

