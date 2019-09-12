# 经典案例

## join 多表关联

```mysql
#统计评分 男女比例
SELECT * FROM ratings a JOIN users b ON a.users = b.users;
```



- mapreduce实现

  map端能够同时读取多个数据；

- map端：

  面向split，识别数据源，对数据加标记

  **对每一条数据打标记--value**

   - key：	相同的关联键

   - value： TAGU+剩下数据         TAGR+剩下数据

     **标记建议在起始位置，不要太长；**

  ```java
   /**
     * Called once at the beginning of the task.
     * 每一行都会运行一次
     * 初始化方法
     */
  protected void setup(Context context) throws IOException, InterruptedException {
      // NOTHING
    }
  
  /**
     * Called once at the end of the task.
     * 
     */
  protected void cleanup(Context context) throws IOException, InterruptedException {
      // NOTHING
    }
  ```

  

### 数据倾斜

- reducejoin

- 在分布式并行计算中，当某一个task的计算任务的数据量很大，其他task的数据量很小，这时候计算过程中就会产生==数据倾斜==，一旦产生数据倾斜，会极大降低计算性能；

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import split.FileInputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @fileName: MyMapper
 * @author: orange
 * @date: 2019/8/6 14:51
 * @description:
 * @version: 1.0
 */

public class MyUserDriver {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJarByClass(MyUserDriver.class);

		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		job.setNumReduceTasks(3);

		FileInputFormat.addInputPath(job, new Path("MapProjectsMaven/input/movie"));
		FileSystem fs = FileSystem.get(conf);
		Path out = new Path("MapProjectsMaven/output/movie/");
		if (fs.exists(out)) {
			fs.delete(out, true);
		}
		FileOutputFormat.setOutputPath(job, out);

		job.waitForCompletion(true);
	}
}


class MyMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	String fileName = "";
	IntWritable mk = new IntWritable();
	Text mv = new Text();

	/**
	 * @param context 上下文对象
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		//获取文件的输入切片
		FileSplit inputSplit = (FileSplit) context.getInputSplit();
		//get file name
		fileName = inputSplit.getPath().getName();
	}

	/**
	 * 上下文对象，对上--逻辑切片，对下--reducetask
	 * @param key
	 * @param value
	 * @param context
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		//根据每一行内容，根据不同文件名打不同标识
		String line = value.toString();
		String[] datas = line.split("::");
		mk.set(Integer.parseInt(datas[0].trim()));
		if ("users.dat".equals(fileName)) {
			mv.set("U" + datas[1] + "\t" + datas[2] + "\t" + datas[3] + "\t" + datas[4]);
		} else {//rating.dat
			mv.set("R" + datas[1] + "\t" + datas[2] + "\t" + datas[3]);
		}
		context.write(mk, mv);
	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		super.cleanup(context);
	}
}

class MyReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
	Text rv = new Text();
	@Override
	protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		//循环遍历values 拼接 先存储在一个容器中，后拼接
		List<String> ulist = new ArrayList<>();
		List<String> rlist = new ArrayList<>();
		for (Text value : values) {
			String line = value.toString();
			if (line.startsWith("U")) {
				ulist.add(line.substring(1));
			} else {
				rlist.add(line.substring(1));
			}
		}
		//进行拼接，数据过滤
		for (String u : ulist) {
			for (String r : rlist) {
				String res = u+"\t"+r;
				rv.set(res);
				context.write(key,rv);
			}
		}
	}
}
```



## mapjoin

- **优势：**可以有效避免join过程中的数据倾斜；
- 整个join过程在map端发生，只需要maptask，不需要reducetask

- 在map端只读取一个大文件，另一个文件加载在每一个maptask运行节点的内存中（小文件），每当map端进行读取一个文件的一行，就去内存中找是否可以匹配另一个文件；



- **实现：**将小文件加载到本地缓冲（磁盘）中，`job.addCacheFile(uri)`，将指定的路径的文件加载到每一个maptask的运行节点上；

  - setup  中
    定义一个流
    定义一个集合
    流开始读取   放在集合中

  - map 
    读取大文件   每次读取一行  和内存集合中的数据做关联；关联完成 写出hdfs

  - 注意：

    只有maptask 时候  将reducetask设置0

    否则默认运行一个   Reducer   job.setNumReduceTasks(0); 

    输出结果：part-m-00000

    本地缓存：/home/hadoop/data/hadoopdata/nm-local-dir/filecache/10

- shuffle
  - 分区：
  - 排序：数值，升序
  - 分组：将相同的userid

- reduce端

  
