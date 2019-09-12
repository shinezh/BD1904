# 全过程

- 文件上传，分块，切片
- maptask
  - 

- **元数据：**描述数据的数据，描述的是原始数据在数组中存储的位置；



## shuffle

- split  --->  maptask (Mapper.run)
  - 每一个maptask都是一个对Mapper的实例化对象
  - setup（1次） map（1行1次） cleanup（1次） run
- 数据经过clooetcor收集器 --->   数据到环形缓冲区中
- 环形缓冲区实质 byte[]      默认100M   阈值80%
- 记录数据时候寻顶一个`equator` 
- 记录元数据（分区Partitioner | getPartition kstart vstart vlength）| 原始数据 -- 0.8 -->split
- 溢写之前会进行数据排序，先按照分区编号；
- 然后按照map-key  --->   每一个maptask就会有多个溢写文件
- 每一个溢写文件都是按照分区排序的，分区界限；
- 每一个分区内部数据按照map-key排序；
- 同一个maptask的多个溢写文件   merge sort -->　一个maptask
- 会形成一个输出结果文件，通过mrapmaster通知reducetask
- 当有一个maptask执行完成，reducetask开始启动 ==》 内部启动多线程（默认5个），进行数据fetch
- 每一个reducetask只会fetch自己分区的数据；
- 每一个reducetask会merge自己所有相同分区的数据，形成一个文件；
- 数据交给reducetask之前，会进行数据分组 WritalbeComparator  compare()   -->  reducetask开始执行





## reducetask

Reducer setup reduce cleanup run



