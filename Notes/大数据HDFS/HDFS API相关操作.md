# HDFS相关API操作

## 概述

> HDFS是Hadoop应用用到的一个最主要的分布式存储系统。一个HDFS集群主要由一个NameNode和很多个Datanode组成：Namenode管理文件系统的元数据，而Datanode存储了实际的数据。HDFS的体系结构在[这里](https://hadoop.apache.org/docs/r1.0.4/cn/hdfs_design.html)有详细的描述。本文档主要关注用户以及管理员怎样和HDFS进行交互。[HDFS架构设计](https://hadoop.apache.org/docs/r1.0.4/cn/hdfs_design.html)中的[图解](https://hadoop.apache.org/docs/r1.0.4/cn/images/hdfsarchitecture.gif)描述了Namenode、Datanode和客户端之间的基本的交互操作。基本上，客户端联系Namenode以获取文件的元数据或修饰属性，而真正的文件I/O操作是直接和Datanode进行交互的。
>
> ——[官方中文文档](https://hadoop.apache.org/docs/r1.0.4/cn/hdfs_user_guide.html)



## Maven 引入jar包

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-common</artifactId>
        <version>2.7.7</version>
    </dependency>

    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-hdfs</artifactId>
        <version>2.7.7</version>
    </dependency>

    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-mapreduce-client-core</artifactId>
        <version>2.7.7</version>
    </dependency>

    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-client</artifactId>
        <version>2.7.7</version>
    </dependency>

    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-yarn-api</artifactId>
        <version>2.7.7</version>
    </dependency>

    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
</dependencies>
```

> maven相关配置都可以去[mavnrepository](https://mvnrepository.com/)查找，各版本都有，非常方便；



## 访问HDFS文件系统

```java
FileSystem fs = null;
@Before
//初始化方法,通过URI访问hdfs系统
public void before() throws Exception {
    //hdfs访问uri
    URI uri = new URI("hdfs://hdp01:9000");
    Configuration conf = new Configuration();
    //设置副本数（默认3）
    conf.set("dfs.replication", "2");
    //获取文件系统
    fs = FileSystem.get(uri, conf, "hadoop");
}
```



## 上传文件到HDFS系统

### 直接复制

```java
//上传本地文件到hdfs
@Test
public void copyFromLocal() throws IOException {
    //本地文件路径
    Path src = new Path("D:\\hh.txt");
    //hdfs文件系统目标路径
    Path dst = new Path("/uu.txt");
    fs.copyFromLocalFile(src, dst);
    System.out.println("上传成功!");
}
```

### 流式数据上传

```java
/**
	 * upload files by stream
	 *
	 * @param hdfs    hdfs
	 * @param srcPath srcPath
	 * @param dstPath destination path
	 */
public void uploadFiles(FileSystem hdfs, Path srcPath, Path dstPath) {
    if (hdfs == null || srcPath == null || dstPath == null) {
        return;
    }
    try {
        //创建输入流
        InputStream in = new FileInputStream(String.valueOf(srcPath));
        //创建输出流
        OutputStream out = hdfs.create(dstPath);
        IOUtils.copyBytes(in, out, 4069, true);
        System.out.println("==upload success==");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



## 下载文件到本地

### 直接复制

```java
/*
类似上传，直接调用FileSystem的copyToLocalFile方法
*/
@Test
public void copytoLocal() throws IOException {
    //hdfs文件系统
    Path src = new Path("/b.txt");
    //本地文件系统
    Path dst = new Path("C:\\Users\\ilove\\Downloads\\Documents\\a.txt");
    fs.copyToLocalFile(false,src, dst,true);
}
```

### 流式数据下载文件

```java
/**
	 * download file by stream
	 *
	 * @param hdfs
	 * @param srcPath
	 * @param dstPath
	 */
public void downloadFiles(FileSystem hdfs, Path srcPath, Path dstPath) {
    if (hdfs == null || srcPath == null || dstPath == null) {
        System.out.println("quit");
        return;
    }
    try {
        FSDataInputStream open = fs.open(srcPath);
        FileOutputStream out = new FileOutputStream(String.valueOf(dstPath));
        IOUtils.copyBytes(open, out, 4069, true);
        System.out.println("==download success==");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

> 依旧是调用IOUTils的copyBytes()方法，将输入流输出流调换，复制完毕后关闭数据流；



## 删除文件

```java
/**
 * delete destination folder
 *
 * @param hdfs hdfs
 * @param dstPath target
 */
public void delFolder(FileSystem hdfs, Path dstPath) {
    if (hdfs == null || dstPath == null) {
        return;
    }
    try {
        //删除目标，若是目录且第二个参数为true，则会递归删除文件夹及其子文件
        boolean b = hdfs.delete(dstPath, true);
        System.out.println("delete " + dstPath.toString() + " successful");
        hdfs.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



## 删除所有空文件及空文件夹

```java
/**
 * delete empty folders and files
 *
 * @param hdfs
 * @param dst
 */
public void delEmptyFiles(FileSystem hdfs, Path dst) {
    if (hdfs == null || dst == null) {
        return;
    }
    try {
        //获取当前目录下所有子文件（夹）信息，通过迭代器循环
        RemoteIterator<LocatedFileStatus> status = hdfs.listLocatedStatus(dst);
        while (status.hasNext()) {
            LocatedFileStatus next = status.next();
            //获取子文件路径
            Path path = next.getPath();
            //如果该文件为目录文件
            if (next.isDirectory()) {
                if (next.getLen() == 0) {
                    //目录文件长度为0，表示为空文件夹，则删除文件夹及其子文件
                    hdfs.delete(path, true);
                    System.out.println("delete " + path.toString() + " successful");

                } else {
                    //如果文件夹不为空，则递归查询其子文件
                    if (hdfs.exists(path)) {
                        delEmptyFiles(hdfs, path);
                    }
                }
            } else if (next.isFile()) {
                //如果为文件，且长度为0，表示为空文件，直接删除
                if (next.getLen() == 0) {
                    hdfs.delete(path, true);
                    System.out.println("delete " + path.toString() + " successful");
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



## 删除指定目录下指定类型文件

```java
/**
 * Delete files with specified suffix names in target folder
 * @param hdfs FileSystem
 * @param suffix the specified suffix name
 * @param dst target folder
 */
public void delTargetFile(FileSystem hdfs, String suffix, Path dst) {
    if (hdfs == null || suffix == null || dst == null) {
        return;
    }
    try {
        //获取当前目录下所有子文件
        RemoteIterator<LocatedFileStatus> files = hdfs.listFiles(dst, false);
        while (files.hasNext()) {
            LocatedFileStatus next = files.next();
            if (next.isFile()) {
                //若当前是文件，获取其路径，并提取后缀名
                Path path = next.getPath();
                String name = path.toString();
                String s = findSuffix(name);
                if (suffix.equals(s)){
                    //如果后缀名是目标后缀，则进行删除
                    hdfs.delete(path);
                    System.out.println("delete "+name+" successfully");
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


/**
 * 通过正则表达式查询文件的后缀名
 * @param name file name
 * @return suffix 返回文件的后缀名
 */
public String findSuffix(String name) {
    String reg = "[^.]+$";
    Pattern compile = Pattern.compile(reg);
    Matcher matcher = compile.matcher(name);
    String suffix = null;
    if (matcher.find()) {
        suffix = matcher.group();
    }
    return suffix;
}
```

