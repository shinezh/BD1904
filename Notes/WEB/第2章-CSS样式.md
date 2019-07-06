# 第2章 网页样式

## 1. 什么是CSS

- **CSS**（Cascading style sheets）：**层叠样式表**，一种用于表现html或xml等文件样式的计算机语言；



## 2. CSS引用

- **内联样式**

  - style="样式的属性：值"   写在标签内

  ```html
  <p style="color: red;">这是一个段落</p>
  ```

- **内部样式**

  - \<head>中\<style type="text/css" />\<\head>

- **外部样式**

  ```html
  <link rel="stylesheet" type="text/css" href="css/sytle.css"/>
  ```

- **优先级**：内联样式>（内部和外部样式优先级取决于调用顺序）

  相同的样式属性有优先级，不同的样式属性都会采用；

## 3. CSS基本语法

```css
选择器{
    样式属性：属性值；
}
```

## 4. CSS选择器

### 基本选择器

- 标签选择器

- ID选择器

  ```css
  #id名{
      样式属性：属性值；
  }
  ```

- 类选择器

  ```css
  .class名称{
      样式属性：属性值；
  }
  ```

- 通用选择器

  ```css
  *{
      样式属性：属性值；
      padding:0px;   内边距
      margin:0px;    外边距
  }
  ```

  

### 组合选择器

- 并集选择器

  ```css
  span,.red{
      color:red;
  }
  ```

- 交集选择器

  ```css
  div.red{
      color:red;
  }
  ```

- 后代选择器

  ```css
  #div span{
      font-size:28px;
  }
  ```

- 子集选择器

  ```css
  p>span{
      font-size:30px;
  }
  ```

  

### 属性选择器





## 5. CSS常用样式

### 字体样式

```css
font-size:20px;           //字体大小，单位 px
font-family:Consolas;     //字体类型，说明 中文+英文，
font-weight:bold;         //字体粗细
```

### 文本样式

```css

```





### 宽高样式

```css
width:100px;      //宽度
height:100px;     //高度
```

### 背景样式

```css
background-color:blue;              //背景颜色
background-image: url(img/1.png);   //背景图片
background-repeat: repeat;          //背景图片的平铺方式 repeat默认

```





### 列表样式

```css
list-style: none;   //去掉列表符号
line-heigh: 20px;   //多行文本的间距
```

### 其他样式

```css
float:left;       //浮动，左|右；元素浮动后脱离文档流

display:inline;   //转为行级元素
display:block;    //转为块级元素

//定位 position
static;           //静态定位，默认
relative;         //相对定位  没有脱离文档流，
absolute;         //绝对定位  脱离文档流，参照父容器绝对定位到某一位置，默认body；
fixed;            //固定定位  固定定位在页面的某一位置
```



## 6.盒子模型

### 外边距 margin

```html
margin-top:
margin-right:
margin-bottom:
margin-left:
```



### 内边距 padding

### 边框 border

```html
border: 2px red solid;    //solid 实线，  
```



