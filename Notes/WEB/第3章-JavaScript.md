---
typora-root-url: ../pics
---

# 第3章 JavaScript

## JS简介

- JS是一种动态类型，弱类型，基于原型直译式的脚本语言；
- **特点**
  1. 设计用于html的交互行为；
  2. 一种脚本语言；
  3. JS也是一种解释性的语言；
  4. 要区分大小写；
  5. 跨平台；

- **作用**
  1. JS可以修改页面的属性
  2. JS 可以对浏览器事件做出响应；
  3. JS可以改变元素的样式；
  4. 用于表单动态验证；
  5. 检测浏览器的信息；
- **组成部分**
  1. ECMASciript 核心
  2. DOM （Document Object Module）文档对象模型
  3. BOM 

- **引用方式**

  ```html
   <!--外部引入-->
  <script src="js/test.js"></script>
  
   <!--内部引入-->
  <script type="text/javascript" charset="UTF-8">
  	alert("外部引入JS成功")
  </script>
  
  <input
         type="button"
         value="点我"
         onclick="javasript:alert('可以啊朋友')" 
  />
  ```

  

## 变量和类型

- **变量**：
  - JS弱类型的，定义变量的方式用 var 声明；
  - 变量以字母、下划线、$开头，可以有数字；
  - 区分大小写；
  - 给变量赋值文本类型值是要用单引号or双引号；

- **类型**
  - String        字符串
  - Number     数字
  - Boolean    布尔类型
  - Array          数组类型
- 类型转换（隐式）
  - false/null -> 0
  - true         -> 1

## 运算符

### 算术运算符

| 运算符 | 名称 | 作用                 | 示例      |
| :----- | :--- | :------------------- | :-------- |
| `+`    | 加法 | 两个数相加。         | `6 + 9`   |
| `-`    | 减法 | 从左边减去右边的数。 | `20 - 15` |
| `*`    | 乘法 | 两个数相乘。         | `3 * 7`   |
| `/`    | 除法 | 用右边的数除左边的数 | `10 / 5`  |
| `%`    | 求余 | 返回余数             | `8 % 3`   |

### 比较运算符

| 运算符 | 名称       | 作用                           | 示例          |
| :----- | :--------- | :----------------------------- | :------------ |
| `===`  | 严格等于   | 测试左右值是否相同             | `5 === 2 + 4` |
| `!==`  | 严格不等于 | 测试左右值是否相同             | `5 !== 2 + 3` |
| `<`    | 小于       | 测试左值是否小于右值         | `10 < 6`      |
| `>`    | 大于       | 测试左值是否大于右值           | `10 > 20`     |
| `<=`     | 小于或等于 | 测试左值是否小于或等于右值。   | `3 <= 2`      |
| `>=`   | 大于或等于 | 测试左值是否大于或等于正确值。 | `5 >= 4`      |



## 控制语句

```javascript
//java
for(int arr:arrays)     //arr 元素
    
//js
for(var arr in Arrays)  //arr 下标
```



## 函数

- **系统函数**

  ```js
  parseInt();    //Convets A String to integer;
  parseFloat();  //Convets A String to floating-point number;
  isNaN();       //非数字返回TRUE
  eval();        //
  ```



## BOM模型

- 用于控制浏览器的行为，window对象表示是浏览器窗口

- **方法**

  ```javascript
  alert();          //弹出一个带有确定按钮的消息框 
  confirm();        //弹出一个带有确定、取消的消息框
  open();           //弹出一个新的浏览器窗口
  setInterval();    //间隔相应毫秒数会无限次启动函数
  setTimeout();     //间隔相应毫秒数只调用一次函数(递归)
  clearInterval();  // 清除 setInterval()
  clearTimeout();   // 清除 setTimeout()
  
  history对象
  back();           // (左键头)  相当于go(-1)
  forward();        // (右键头)  相当于go(1)
  go(N)             //跳转到相关页面
  
  location对象  对象包含有关当前 URL 的信息;
  属性：
  	href;         //返回完整的URL
  方法：
  	reload();     //重新加载 F5
  ```

  ```html
  <html>
      <head>
          <meta charset="UTF-8">
          <title></title>
          <script type="text/javascript">
              function show(){
                  location.href="https://www.baidu.com";
  
              }
              function show1(){
                  location.reload();  //刷新F5
                  //location.replace()
              }
          </script>
      </head>
      <body>
          <input type="button" value="刷新页面" onclick="show1()" />
          <input type="button" value="点我跳转页面" onclick="show()" />
          <input type="button" value="显示当前页面URL" onclick="javascript:alert(location.href)" />
      </body>
  </html>
  ```

  

## DOM文档对象模型

- **dom:可以通过DOM方法HTML页面中的所有元素，同时也可以修改页面中元素。**

```javascript
document.getElementById(”ID值“);             //根据元素的ID值获取元素对象
document.getElementsByName(”name值“);        // 根据元素的name属性值获取元素对象（数组）
document.getElementsByTagName(”标签名“);     //根据标签的名称获取元素对象（数组）
document.getElementsByClassName(“类名”);     //根据类名称获取元素对象 （数组）
var div = document.getElementById("div");
div.innerHTML="abc";
var inp = document.getElementsByName("uname");
console.log(inp);
var inp1 = document.getElementsByTagName("input");
console.log(inp1);
var inp2 = document.getElementsByClassName("red");
console.log(inp2);
```

![1561683490121](/1561683490121.png)

### 案例：实现全选功能

```html
<script type="text/javascript">
    function checkall(){
        var objAll = document.getElementById("all");
        var selAll =  document.getElementsByName("sel");
        //console.log(selAll);
        for(var i in selAll){
            selAll[i].checked=objAll.checked;
        }
    }
    function selall(){
        var objAll = document.getElementById("all");
        var selAll =  document.getElementsByName("sel");
        var count=0;
        for(var i in selAll){
            if(selAll[i].checked){
                count++;
            }
        }
        if(count===selAll.length){
            objAll.checked=true;
        }else{
            objAll.checked=false;
        }
    }
</script>
```



#### 获取或设置标签中的内容

```css
innerHTML=””;   //识别Html代码
innerText=””;   //纯文本
```



#### 获取或设置标签中的属性

```css
dom对象.src=”11.jpg”;
```

 

#### 标签样式

```javascript
dom对象.style.样式属性=“属性值”; 
注意：如果样式属性有两个以上的单词组成，去掉横线第二个单词首字母大写。

function gteVal(){
    //给div赋内容 
    var div =document.getElementsByTagName("div")[0];
    //alert(div.innerHTML);
    div.innerHTML="<h2>第一个层</h2>";
    //改图片
    var img = document.getElementById("img");
    img.src="img/reg.jpg";
    //alert(img.src);
    //改样式
    var div1 = document.getElementById("div1");
    div1.style.color="red";
    div1.style.backgroundColor="blue"
}
```



## 事件

### 页面加载

- window.onload

  ```html
  <script type="text/javascript">
      window.onload=function(){
          document.getElementById("div").innerHTML="内容";
      }
  </script>
  ```

  

### 点击事件

- 鼠标单击事件 onclick=function(){ }  

- 双击事件 ondblclick=function(){  } 

- 鼠标移入 onmouseover 

- 鼠标移出 onmouseout

  ```javascript
  /*鼠标事件*/
  function show1(){
  	document.getElementById("div1").style.backgroundColor="#ccc";
  }
  function show2(){
  	document.getElementById("div1").style.backgroundColor="#f00";
  }
  ```

- 获取焦点 onfocus()

- 失去焦点 onblur()

  ```javascript
  /*焦点的事件*/
  function show3(){
      document.getElementById("span").innerHTML="请输入用户名";
  }
  function show4(){
      document.getElementById("span").innerHTML="";
      var name = document.getElementById("uname").value;	
  
      if(name.length<6){
          document.getElementById("span").innerHTML="用户名不正确";
      }else{
          document.getElementById("span").innerHTML="用户名正确";
      }
  }
  ```



### 键盘事件

- onkeyup  弹起   

- onkeydown 按下    

- onkeypress   产生打印字符

  ```javascript
  /*键盘事件*/
  document.onkeydown=function(){
      console.log("keydown");
  }
  document.onkeyup=function(eve){
      //console.log("keyup");
      if(eve.keyCode==13){
          alert("你按下了回车键");
      }
  
  }
  //产生打印字符
  document.onkeypress=function(){
      console.log("keypress");
  }
  ```



### 表单验证事件

- onsubmit

  ```html
  <script>
  function checkform(){
  var name = document.getElementById("uname").value;
  if(name.length<6){  //用户名小于6位表示不合法					
  	return false;
  }
  //其它
  	return true;
  }
  </script>
  <!--页面代码:-->
  <form action="window.html" method="post" onsubmit="return checkform()">
  	<input type="text" name="uname" id="uname" value="" />
  	<input type="submit" />
  </form>
  ```



### onchange事件

```html
<script type="text/javascript">
    var arrData = new Array();			
    arrData['北京'] = ['昌平区','朝阳区','海淀区','大兴区'];
    arrData['上海'] = ['黄浦区','徐汇区','上宁区'];
    arrData['陕西省'] = ['西安市','渭南市','榆林市','延安市'];
    //1 页面加载 绑定省 province 
    window.onload=function(){
        var objpro = document.getElementById("province");
        var objcity = document.getElementById("city");
        //绑定了省
        for(var i in arrData){
            var option = new Option(i,i);  //显示的值      value值 
            objpro.add(option);
        }
        //绑定市
        objpro.onchange=function(){
            var citys = arrData[objpro.value];
            objcity.innerHTML="<option>请选择</option>";
            for(var j in citys){
                var option =new Option(citys[j],citys[j]);
                objcity.add(option);
            }
        }
    }
</script>
<!--页面代码；-->
<select id="province">

</select>省
<select id="city">
    <option>请选择</option>
</select>市<br/>
```



## 正则表达式

- **正则表达式**，又称规则表达式。（英语：Regular Expression，在代码中常简写为regex、regexp或RE）。正则表通常被用来检索、替换那些符合某个模式(规则)的文本。

| **符号** | **描述**                                                |
| -------- | ------------------------------------------------------- |
| /…/      | 代表一个模式的开始和结束                                |
| ^        | 匹配字符串的开始                                        |
| $        | 匹配字符串的结束                                        |
| \s       | 任何空白字符                                            |
| \S       | 任何非空白字符                                          |
| \d       | 匹配一个数字字符，等价于[0-9]                           |
| \D       | 除了数字之外的任何字符，等价于[^0-9]                    |
| \w       | 匹配一个数字、下划线或字母字符，等价于[A-Za-z0-9_]      |
| .        | 除了换行符之外的任意字符  用”.”  可以”\.”               |
| {n}      | 匹配前一项n次                                           |
| {n,}     | 匹配前一项n次，或者多次                                 |
| {n,m}    | 匹配前一项至少n次，但是不能超过m次                      |
| *        | 匹配前一项0次或多次，等价于{0,}                         |
| +        | 匹配前一项1次或多次，等价于{1,}                         |
| ？       | 匹配前一项0次或1次，也就是说前一项是可选的，等价于{0,1} |

```html
<!--检验手机号码-->
<script type="text/javascript">
    var regx = /^1[3-9][0-9]{9}$/;  //   * {0,} + {1,}  ? {0,1}
    var str ="13799998888";
    if(regx.test(str)){   //true false
        alert("正确的手机号码");
    }else{
        alert("不正确");
    }
    if(str.match(regx)!=null){  //null 不通过
        alert("通过");
    }else{
        alert("不通过");
    }
</script>
```



## JSON表达式

- json:是一个轻量级的数据交换格式。

```javascript
var obj = new Object();
obj.name="李四";
obj.age=12;
console.log(obj.name+"---"+obj.age);
//2 自定义对象 
var obj2 = {name:"张三","age":23};
console.log(obj2.name+"-------"+obj2.age);
//3自定义json串
var json = '{"name":"张三","age":23}';   //json串属性必须加引号
//核心
//js对象转json串
var json1 = JSON.stringify(obj2);
console.log(json1);
//json串转js对象 
var objjson = JSON.parse(json1);
console.log(objjson);
console.log(objjson.name);	
```

