# scala面向对象基础

# 0. 大纲

- 类的定义
- getter/setter
- 构造函数
- 嵌套(内部)类
- 伴生类(对象)
- 单例

# 1. 类的定义

​	在scala中使用关键字class去定义一个类，其概念和java中的类是一模一样的。

## 1.1. 类的声明和创建

```scala
class Person {
 //....   
}
```

## 1.2. 类中成员

​	基本也就包括成员变量和成员方法，如下代码，定义了一个Person类，其中有2个成员变量，一个成员方法。

```scala
class Person {
    //成员变量
    val name:String = _
    val age:Int = _

    def show(): Unit = {
        println(s"name: $name, age: $age")
    }
}
```

## 1.3. 类的使用

​	类的使用，就通过对象来完成

```scala
object _01ClassOps {
    def main(args: Array[String]): Unit = {
        val p:Person = new Person()//创建一个scala的Person对象
        p.name = "zhangsan"
        p.age = 38
        p.show()
    }
}
class Person {
    //成员变量 如果后续要修改成员变量，必须声明为var 如果声明为val则必须要初始化
    var name:String = _
    var age:Int = _
    def show(): Unit = {
        println(s"name: $name, age: $age")
    }
}
```

# 2. setter/getter操作

## 2.1. 为什么要有getter和setter操作

​	这些操作是javabean中不可或缺的部分。

​	一般对成员变量要私有private,同时对其提供getter和setter，主要原因在于如下两个方面：

1. 为了数据安全

    设置age的时候

    public void setAge(int age) {

    ​	if(age < 0) throws new RuntimeException("看样子，得回娘胎重造了~")

    ​	if(age > 150) throws new RuntimeException("确定是地球人吗？") 

    ​	this.age = age;

    }

 2. 为了完成更加复杂的操作

    同上

## 2.2. 如何提供getter和setter

```scala
import scala.beans.BeanProperty

object _02ClassOps {
    def main(args: Array[String]): Unit = {
        val p:Student = new Student()
        p.setName("詹远奇")
        p.setAge(26)
        p.show()
    }
}

class Student {
    private var name:String = _
  	 //使用注解@BeanProperty，模拟java中的setter/getter,但是得需要吧private去掉
    @BeanProperty var age:Int = _
    def show(): Unit = {
        println(s"name: $name, age: $age")
    }

//    def setName(n:String):Unit = {
//        name = n
//    }
    def setName(n:String) = name = n //单行函数
//    def setAge(a:Int):Unit = {
//        age = a
//    }
//    def getName():String = {
//        name
//    }
    def getName() = name //单行函数
//    def getAge():Int = {
//        age
//    }
}
```

> 注意：实际上getter和setter是java中的bean所拥有的的，但是java中的bean有啥特点？一般都是用来做数据传输，或者携带数据，通常不会有过多的行为操作。
>
> 同理在scala中，也有这样的一个需求，但是上述的这种setter和getter的定义方式太过于麻烦，不适合作为bean来传递数据，所以scala中有一个专门的结构来模拟java中的bean——**case class**。

# 3. 类的构造

## 3.1. 构造器

​	在java中，构造器或者构造函数，是一个非常特殊的方法，方法名称和类名一直，并且没有返回值，类对应的实例对象的创建，必须要通过构造器/函数来完成。在java中一个类，至少拥有1个构造器，最后的这一个构造器是默认的无参的构造器。

​	同理，在scala中创建一个类的实例对象，也需要这个构造器，从前面的案例中，也能够知晓，scala提供了默认的构造器。

​	在scala中构造器分为了两种：主构造器，辅助构造器

- 主构造器

    ​	scala中构造器的定义方式和java中的定义不一样，是在类名后面用小括号()括起来，当然如果没有参数列表，小括号可以省略。主构造器的函数体，就是类体，也就是说主构造器的定义和类的定义交织在了一起！

    ​	一个scala类中，有且仅有一个主构造器！

    1. 无参的主构造器

        ```scala
        class Teacher() {
            var name:String = _
            var age:Int = _	
            def Teacher(): Unit = {
            }
            println("---Teacher---")
        
            def show(): Unit = {
                println(s"name: $name, age: $age")
            }
        
        }
        ```

    2. 有参的主构造器

        ```scala
        class Teacher(var name:String, val age:Int) {
            def Teacher(): Unit = {
            }
            println("---Teacher---")
        
            def show(): Unit = {
                println(s"name: $name, age: $age")
            }
        }
        ```

- 辅助构造器

    ​	辅助构造器的写法就是将java中构造器的类名改成this即可，同时，构造器的第一句话必须调用本类的主构造器或者其它辅助构造器。但是归根到底，最后还是得要调用主构造器。

    1. 无参辅助构造器

    ```scala
    class Teacher(name:String, age:Int) {//主构造器
        def this() {//无参的辅助构造器
            this("彭国宏", 23)
        }
        println("---Teacher---")
    
        def show(): Unit = {
            println(s"name: $name, age: $age")
        }
    }
    ```

    2. 有参辅助构造器

    ```scala
    class Teacher(name:String, age:Int) {//主构造器
        def this() {//无参的辅助构造器
            this("彭国宏", 23)
            println("-----this() -----")
        }
        var gender:Int = _
    
        def this(gender:Int) {
            this()
            this.gender = gender//当局部变量名和成员变量名发生冲突，可以用this加以区分
            println("-----this(gender:Int)-----")
        }
        println("---(name:String, age:Int)---")
        def show(): Unit = {
            println(s"name: $name, age: $age")
        }
    }
    ```

    3. 案例说明

    ```scala
    val t3 = new Teacher(0)
    t3.show()
    ```

    执行顺序

    > ---(name:String, age:Int)---
    > -----this() -----
    > -----this(gender:Int)-----
    > name: 彭国宏, age: 23

# 4. 嵌套(内部)类

​	嵌套类，其实就是在类中再去定义一个类的结构，也就是内部类。

## 4.1. Java中内部类的案例

```java
public class Outer {
    public static void main(String[] args) {
        Outer.Inner oi = new Outer().new Inner();
        oi.show();
    }
    int x = 5;
    class Inner {
        int x = 6;
        public void show() {
            int x = 7;
            System.out.println("x=" + Outer.this.x);
        }
    }
}
```

​	外部类名.this代表的是外部类的引用

​	这里的this代表的是本类的引用

## 4.2. Scala中内部类的案例

```scala
object _04NestClassOps {
    def main(args: Array[String]): Unit = {
        val outer = new Outer
        val inner = new outer.Inner()
        inner.show()
    }
}

class Outer { oooooo =>//scala提供的非常灵活的写法
    val x = 5

    class Inner { iii =>
        val x = 6
        def show(): Unit = {
            val x = 7
            println("x: " + x)
            println("x: " + iii.x + "-->" + this.x)//6
            println("x: " + oooooo.x + "-->" + Outer.this.x)//5
        }
    }
}
```

# 5. 伴生类(对象)

## 5.1. object对象

​	此对象含义，不是类的实例的意思，就是object的直译。object是scala中一种和class平级的语法结构。为啥要有object？

​	scala运行最后要到jvm运行，jvm运行需要main函数，java中的main数，必须是public的，必须是static的，必须是void的！请问static在scala中能否做到？scala中没有静态这个概念！

​	所以于是乎，scala提供了一种结构object，来模拟java中的静态的行为，比如静态成员变量，静态方法。

## 5.2. 伴生类(对象)

​	scala中把在同一个scala源文件中定义的名称相同的class和object，互相称之为object的伴生类和class的伴生对象。

​	使用scala中的伴生对象的结构，就可以给一个普通的scala类添加类似java中的静态的行为，丰富class的api操作。

​	同时，有了伴生对象，并且覆盖了其中的apply(参数列表)方法，在创建对象的时候就可以省略new关键字，但是需要注意一点的是，apply的参数列表最好和class对应的构造器保持一致，同时返回值为class的类型。

```scala
//模拟java中的静态行为
object _05ObjectOps {
    def main(args: Array[String]): Unit = {
//        val worker = new Worker()
        val worker = Worker()
        val worker1 = Worker("heihe", 13)

        Worker.print()
    }
}

class Worker {
    var name:String = "工人"
    var workAge:Int = 0

    def show(): Unit = {
        println(s"$name ---> $workAge")
    }

    def this(name:String, workAge:Int)  {
        this()
        this.name = name
        this.workAge = workAge
    }
}

object Worker {
    def apply():Worker = {
        new Worker
    }
    def apply(name:String, workAge:Int):Worker = {
        new Worker(name, workAge)
    }
    
    def print(): Unit = {//模拟java中的类的静态方法
        println("----print")
    }
}
```

# 6. 单例！！！

## 6.1. java中的单例

- 恶汉式

    ```java
    //恶汉式
    class Singleton {
        private static Singleton istance = new Singleton();
        private Singleton(){}
        public static Singleton getIstance() {
            return istance;
        }
    }
    ```

- 懒汉式

    ```sca
    class Singleton {
        private static Singleton istance;
        private Singleton(){}
        public static /*synchronized*/ Singleton getIstance() {
            if(istance == null) {
                synchronized (Singleton.class) {
                    if (istance == null) {
                        istance = new Singleton();
                    }
                }
            }
            return istance;
        }
    }
    ```

## 6.2. scala中的单例

​	scala其实并没有单例，只不过大多数程序员都是由java转过来，类没有单例是不是乖乖的，所以边用object做了简单的模拟。

```scala
object _06SingletonOps {
    def main(args: Array[String]): Unit = {
        val x = Singleton.incr()
        val x1 = Singleton.incr()
        println("x: " + x)
        println("x1: " + x1)
    }
}
object Singleton {
    var x = 5
    def incr(): Int = {
        x = x + 1
        x
    }
}
```

作业：

​	1.使用scala完成选择排序