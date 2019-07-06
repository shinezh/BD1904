# 第11章 JAVA常用API类

## 11.1 String/StringBuilder/StringBuffer

### String具有不可变性；

```java
public boolean equals(Object o);
public char charAt(int index);
public int compareTo(String another);//正 0 负
public int indexOf(String s);//找参数字符串的索引位置
public int indexOf(String s,int startpoint);//
public int lastIndexOf(String s);//从右开始匹配
public int lastIndexOf(String s,int startpoint);//从右开始匹配
public boolean startWith(String s)://判断字符串是否以s开头
public boolean endWith(String s);//判断字符串是否以s结尾
public String substring(int startpoint);//返回开始位置至最后一个字符
public String substring(int star,int end);//返回[start-end)最后一个字符
public String trim(String s);//去除字符串首尾的空格，不能改变本字符串内容；
```

- 类型转换

### StringBuilder/StringBuffer

- **StringBuffer**：线程安全，可变的字符序列；
- 456







## 11.2 日期类型（Date/Calendar）





## 11.3 Math类





