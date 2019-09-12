# 第9章 IO流

## 9.1 File类操作

- **File**：该类是对文件和目录操作，文件删除、修改、复制等操作；不对文件内容进行操作；
- **路径**：
  - 相对路径：相对于当前文件夹的路径；
  - 绝对路径：完整磁盘路径；





## 9.2 I/O原理

> IO是设备之间的数据传递，eg: 
>
> 磁盘-》内存（输入操作input）
>
> 内存->磁盘（输出操作output）



## 9.3 IO分类(3类)

- **按操作数据单位**：
  - 字节流（8bit）     一般操作**非文本文件**
  - 字符流（16bit）   操作文本文件

- **按流向分类**：输入流（内存数据到磁盘）    输出流（磁盘->内存）

- **按角色不同**：节点流（4）    处理流（N）

| 数据单位 | 抽象父类     | 节点流           | 缓冲（处理）流       |
| -------- | ------------ | ---------------- | -------------------- |
| 字节流   | InputStream  | FileInputStream  | BufferedInputStream  |
| 字节流   | OutputStream | FileOutputStream | BufferedOutputStream |
| 字符流   | Reader       | FileReader       | BufferedReader       |
| 字符流   | Writer       | FileWriter       | BufferedWriter       |

```java
File file3 = new File("test2.txt");
FileInputStream fis = new FileInputStream(file3);
System.out.println((char)fis.read());
System.out.println((char)fis.read());
System.out.println((char)fis.read());

int b;
while((b=fis.read())!=-1){
    //循环读取，直至读完
    System.out.print((char)b);
}
//释放资源
fis.close();
```



- 字符串->字节数组：byte [] bytes = ”String“.getBytes();
- 字节数组->字符串：String str = new String(byte[] bytes);
- 字符数组->字符串：String str = new String(char[] chars);



## 9.4 缓冲流

- BufferedInputStream
- BufferedOutputStream
- BufferedReader
- BufferedWriter







## 9.5 转换流（字节流->字符流）

- InputStreamReader   //输入流
- OutputStreamWriter //输出流



#### 标准的输入输出流

- 输入：Sacnner
- 输出：System.in





## 9.6 打印流







## 9.7 数据流









## 9.8 对象流（重点）

序列化和反序列化

- **序列化：**对象信息存储在文件中的这个过程；
- **反序列化**：从文件中将信息返回给对象的过程；

- ObjectInputStream/ObjectOutputStream
- **注意：**无法序列化的属性：static transient







## 9.9 随机流

**RandomAccessFile**

- 该类可以作为输入，也可以作为输出

- 可以实现修改内容（替换、插入）

  > RandomAccessFile(File file,String mode)
  >
  > "r" -> 读      "w"-> 写