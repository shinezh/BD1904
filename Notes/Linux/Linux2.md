

# Linux

## 用户和组

### 什么是用户和组

- **用户：**权限的集合
- **用户组：**管理用户、权限的集合



### 用户和组的分类

#### 用户分类

- **root用户：**即管理员用户，拥有所有权限，uid 0
- **系统用户：**保证系统正常运行授权的用户，没有密码不能登陆， uid 1-499
- **普通用户：**权限受限，uid500-60000

#### 用户组分类

- **系统组：**存放系统用户；
- **私有组：**如果创建用户，同时会创建同名的用户组；
- **普通组：**存放普通用户；



### 查看用户和组

#### 查看用户

```shell
cat /etc/passwd

root     用户名

```

#### 查看组







### 操作用户和组

```shell
useradd
usermod
userdel

修改密码： passwd user

groupadd
groupmod
groupdel

添加/删除用户：

```



### 配置普通用户sudoer权限





## 文件权限

### 查看权限

```shell
shineu@SHINE-MI:~$ ll
total 28
drwxr-xr-x 1 shineu mi-ubuntu 4096 Jul 15 10:23 ./
drwxr-xr-x 1 root   root      4096 Jul 15 09:22 ../
-rw------- 1 shineu mi-ubuntu  718 Jul 15 10:21 .bash_history
-rw-r--r-- 1 shineu mi-ubuntu  220 Jul 15 09:22 .bash_logout
-rw-r--r-- 1 shineu mi-ubuntu 3771 Jul 15 09:22 .bashrc
-rw-r--r-- 1 shineu mi-ubuntu  807 Jul 15 09:22 .profile
-rw-r--r-- 1 shineu mi-ubuntu    0 Jul 15 09:23 .sudo_as_admin_successful
-rw------- 1 shineu mi-ubuntu 4443 Jul 15 10:23 .viminfo

 111000000
  7  0  0
  
r   read     可读       ls
w   write    可写       创建/删除
x   excute   可执行     cd
```



### 修改权限

chmod

chmod 777 文件/文件夹

​             -R 修改文件及其子文件权限



### 修改所属权（必须root权限）

```shell
chown 用户:组 文件
```



## 打包压缩

### 压缩（gzip）

```shell
gzip 文件列表
```

### 解压缩

```shell
gzip -d 压缩包
```

### 打包

```shell
#将多个文件打到一个包
tar -cvf 打包文件名称.tar  文件列表
c  创建打包文件
v  显示打包过程
f  指定打包文件
```

### 解包

```shell
tar -xvf 打包文件名称.tar -C 目录
```

### 打包并压缩

```shell
tar -zcvf 文件.tar.gz 文件列表
```

### 解包并解压缩

```shell
tar -zxvf 文件.tar.gz -C 目录
```



## 系统管理

### 服务管理命令

```shell
service --status-all      查看所有后台进程
  | 管道符，将前面命令的输出作为后面命令的输入
  grep   检索包含指定内容的行
  
chkconfig --list     查看所有后台进程在各种系统启动级别下的开机状态
chkconfig --level 24 httpd off    设置指定系统启动级别下的开机状态
```

### 挂载

- 什么是挂载

  > 将设备文件连接到一个已经存在的目录，称之为挂载；
  >
  > 挂载源：需要被挂载的设备文件（/dev/）
  >
  > 挂载点：连接到的目录()

- 如何实现挂载





### 日期操作

- 查看和修改时区

  ```shell
  #查看时区
  cat /etc/sysconfig/clock
  #定义时区的文件（序列化数据）
  /etc/localtime 
  
  #修改时区文件
  cp /usr/share/zoneinfo/Asia/Shanghai 
  /etc/localtime
  ```

- 查看日期时间

  ```shell
  date
  date -R
  date "+%Y-%m-%d %H:%M:%S"
  ```

- 修改日期时间

  ```shell
  #手动修改
  date -s "2019-7-15 15:15:00"
  date "mmddHHMMCCYY.SS"
  
  #同步网络时间，从时间服务器获取时间->修改本地时间
  ntpdate 时间服务器地址(ntp1.aliyun.com)
  ```

- 日期计算

  ```shell
  date -d "next day"
  ```

  

## 软件安装

### 二进制发布包

- jdk安装
  - 下载jdk包
  - 上传到linux
  - 解压
  - 配置
    - /etc/profile    全局（所有用户）
    - /.bash_profile   /.bashhrc
  - 测试

### rpm安装包

- 程序已经按照redhat的包管理规范打包，获取改rpm包，然后使用rpm进行安装；

### yum方式

在线安装，前端软件管理器。拥有一个服务器，在该服务器上存放了按照rpm方式打包的文件，利用yum命令从服务器查找，下载安装这些文件，能够解决包依赖问题；

### 源码编译安装

- 项目是以源码工程方式发布，必须先编译然后再安装部署；
- 



/usr/lib/jvm/java-11-openjdk-amd64