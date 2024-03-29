# HTML

## HTML介绍

- **HTML(Hyper Text Markup Language)**超文本标记语言；
- 超文本：展示内容更加丰富（文本、音视频等）；
- 标记：标签；
- 网页相当于HTML文档，HTML文档由文档标签和内容组成；



## 软件开发架构

### B/S 浏览器/服务器

- **优点**：部署简单，维护方便
- **缺点：**协议，安全性较差；



### C/S 客户端/服务器

- **优点：**部署到本地，安全性较高，数据本地化，速度更快；
- **缺点：**维护不方便，需要对每个客户端单独维护；



## HTML标签及规范

### 什么是标签

- 用一对尖括号包裹，如：\<html>

### 规范

1. 标签中内容一般是成对出现；
2. 标签要正确嵌套；
3. 标签可以定义属性，属性值要用引号引起来；
4. 标签名称不区分大小写，建议使用小写名称；
5. 也可以定义空元素（空标签：没有内容的标签）；



## HTML基本组成部分

```html
<!DOCTYPE html>
<html><!--根标签1个-->
	<head><!--头部信息-->
		<meta charset="utf-8" />
        <!--meta keywords content 关键词，描述信息-->
		<title>这是标题</title>
	</head>
	<body>
		这里是内容
	</body>
</html>
```



## 常见标签

### 标题标签

- 作用，强调，加粗，黑体，搜索引擎加索引

```html
<h1>一级标题</h1>
<h3>三级标题</h3>
<h6>六级标题</h6>
```

### 段落标签

- 分段使用，换行（块级元素），行与行之间有空白

```html
<h1>悯农</h1>
<p>锄禾日当午，</p><p>汗滴禾辛苦。</p>
<p>谁知盘中餐，</p><p>粒粒皆辛苦。</p>
```

### 换行标签

- 空标签，不成对
- 作用：用于文本换行，行与行之间无空白

```html
这是内容1<br />
这是内容2<br />
这是内容3<br />
```



### 水平线（分隔符）

```html
<hr color="crimson" size="6" width = "300" align = "left"/>
<!--属性-->
color   颜色
size    粗细
width   宽度
align   对齐
```



### 超链接标签

- 作用：用于跳转页面；
- 路径：（外网，本网站的某一个页面）
  - 相对路径：参照物是当前文件，下级：直接写相关目录路径；上级：../
  - 绝对路径

```html
<!--跳转外网-->
<a href="http://shineee.win">闲谈梦落花</a>
<!--跳转本网站的另一个页面--><br />
<a href="index.html">返回主页</a><br />
<!--跳转到上上级的一个页面--><br />
<a href="../../test.html"></a>
```

- 相关属性：
  - target属性：
    - \_blank     弹出一个新窗口
    - \_self        在当前窗口打开新页面（默认的）
    - \_parent   在父级窗口打开（框架frameset 中实现）
    - \_top        在顶级窗口打开

  - 锚链接

  ```html
  
  ```

  

### 图像标签\<img />

- 行级元素
- 作用：用于网页中显示图像；
- 属性：
  - src：图片链接、地址
  - align：对齐
  - title：
  - alt：



### 列表标签

- 无序列表
  - 标签：\<ul>\<li>列表项\</li>\</ul>
  - 应用场景：列表、导航
- 有序列表
- 自定义描述标签

```html
<ul type="square">
    <li>列表1</li>
    <li>列表2</li>
    <li>列表3</li>
</ul>

<ol type="I">
    <li>列表1</li>
    <li>列表2</li>
    <li>列表3</li>
</ol>

<dl>
    <dt>定义信息</dt>
    <dd>111</dd>
    <dd>222</dd>
</dl>
```

### 表格标签

- 作用：

- 属性：
  - border：设置表格的**外**边框
  - valign：垂直对齐方式（top、center、bottom）
  - **colspan：合并列（横着）**
  - **rowspan：合并行（竖着）**
  - **cellpadding：设为0，内容与单元格间距离；**
  - **cellspacing：单元格与单元格间距离；**
  - bgcolor：背景颜色



### 表单标签\<form>

- **作用**：客户端和服务端交互（提交数据）

- **属性**：
  - form：标签
  - name：表单名称
  - action：提交服务的处理地址
  - method：提交方式（get | post）
    - 区别：get只能提交少量的数据，post提交大量数据
    - 区别：get提交的数据会显示在地址栏中，不安全；post的数据不会在地址栏出现，相对安全；
  - 表单内部标签：\<input type="">
- 表单元素
  - type 属性：
    - text 默认，单行文本框
    - password：密码框
    - submit：提交按钮
    - name：**一定要加**





## 布局标签

- 块级元素 div

- 行级元素 span





## 特殊符号

| HTML原始码 |      | 描述 |
| ---------- | ---- | ---- |
|            |      |      |
|            |      |      |
|            |      |      |





#### 框架集