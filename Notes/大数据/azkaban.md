# azkaban

## 是啥

### 任务调度

- 数据采集（上游任务）
- 数据清洗（下游任务）
- 决定多个任务，什么时候执行，执行的先后顺序
- 数据清洗依赖于数据采集





## azkaban与oozie的区别

### 配置文件不同

- azkaban采用properties  key=value
- oozie采用xml类型

### 工作流传输

- Azkaban 支持直接传参，例如${input}

- Oozie 支持参数和 EL 表达式，例如${fs:dirSize(myInputDir)} strust2(ONG)

### 定时执行

- Azkaban 的定时执行任务是基于时间的

- Oozie 的定时执行任务基于时间和输入

### 资源管理

- Azkaban 有较严格的权限控制，如用户对工作流进行读/写/执行等操作
- Oozie 暂无严格的权限

### 高可用支持

- azkaban 3.0 之前版本不支持高可用，3.0之后支持
- oozie 完善的高可用

### 任务调度机制

> azkaban的任务调度机制没有oozie完善
>
> 都提供了完善的可时候操作界面

- 基于时间
- 基于事件
- 基于依赖关系
- 基于用户输入的



## 安装

### 安装准备

- 任务调度：
    - `shell` 脚本任务，java，mapreduce，hive，spark。。



### 安装版本

- 2.5.0



### 安装节点

- 一个节点
- **需求**：可以访问到所有可以调度任务的客户端



### 安装包

- **Azkaban Web 服务器**： azkaban-web-server-2.5.0.tar.gz
- **Azkaban Excutor 执行服务器**： azkaban-executor-server-2.5.0.tar.gz
- **Azkaban 初始化脚本文件**： azkaban-sql-script-2.5.0.tar.gz    mysql脚本，依赖于关系型数据库

### 配置环境变量

```shell
export AZKABAN_HOME=/opt/azkaban/azkaban-web-2.5.0
export PATH=$PATH:$AZKABAN_HOME/bin

export AZKABAN_EXE_HOME=/opt/azkaban/azkaban-executor-2.5.0
export PATH=$PATH:$AZKABAN_EXE_HOME/bin
```

### 检查时间同步

- 确定时间相同
- 检查所有节点时区

### 生成密钥文件 keystore

> 在哪个目录下运行，就在哪个目录下生成
- ` keytool -keystore keystore -alias jetty -genkey -keyalg RSA`

### 到mysql中进行准备需要的数据库&表

- 建立database
    - `create database azkaban;`
    - `use azkaban;`
    - `source /opt/azkaban/azkaban-2.5.0/create-all-sql-2.5.0.sql`

### 修改配置文件

#### azkaban-executor

```properties
#Azkaban
default.timezone.id=Asia/Shanghai

# Azkaban JobTypes Plugins
azkaban.jobtype.plugin.dir=/opt/azkaban/azkaban-executor-2.5.0/plugins/jobtypes

#Loader for projects
executor.global.properties=/opt/azkaban/azkaban-executor-2.5.0/conf/global.properties
azkaban.project.dir=/home/shineu/data/azkaban/projects

database.type=mysql
mysql.port=3306
mysql.host=hdp01
mysql.database=azkaban
mysql.user=root
mysql.password=mysql
mysql.numconnections=100

# Azkaban Executor settings
executor.maxThreads=50
executor.port=12321
executor.flow.threads=30
```

#### azkaban-web

```properties
#Azkaban Personalization Settings
azkaban.name=AzkabanTest
azkaban.label=My Local Azkaban
azkaban.color=#FF3601
azkaban.default.servlet.path=/index
web.resource.dir=/opt/azkaban/azkaban-web-2.5.0/web/
default.timezone.id=Asia/Shanghai

#Azkaban UserManager class
user.manager.class=azkaban.user.XmlUserManager
user.manager.xml.file=/opt/azkaban/azkaban-web-2.5.0/conf/azkaban-users.xml

#Loader for projects
executor.global.properties=/opt/azkaban/azkaban-executor-2.5.0/conf/global.properties
azkaban.project.dir=/home/shineu/data/azkaban/projects

database.type=mysql
mysql.port=3306
mysql.host=hdp01
mysql.database=azkaban
mysql.user=root
mysql.password=mysql
mysql.numconnections=100

# Velocity dev mode
velocity.dev.mode=false

# Azkaban Jetty server properties.
jetty.maxThreads=25
jetty.ssl.port=8443
jetty.port=8081
jetty.keystore=/opt/azkaban/azkaban-web-2.5.0/keystore
jetty.password=ishine
jetty.keypassword=ishine
jetty.truststore=/opt/azkaban/azkaban-web-2.5.0/keystore
jetty.trustpassword=ishine

# Azkaban Executor settings
executor.port=12321

# mail settings
mail.sender=
mail.host=
job.failure.email=
job.success.email=

lockdown.create.projects=false

cache.directory=cache
```



### 启动

- 启动executor   

    - `azkaban-executor-start.sh`

    - `nohup azkaban-executor-start.sh 1>/home/shineu/data/azkaban/logs/azexstd.out 2>/home/shineu/data/azkaban/logs/azexerr.out &`

- 启动web

    ```shell
    azkaban-web-start.sh
    nohup azkaban-web-start.sh 1>/home/shineu/data/azkaban/logs/azwebstd.out 2>/home/shineu/data/azkaban/logs/azweberr.out &
    ```

#### 访问

`https://hdp01:8443`



## azkaban的使用

- 任务描述 - - properties文件

- 必须以`.job`结尾，内容是 key=value 类型

    ```properties
    #command.job
    type=command	#类型
    command= echo "hello world"	#内容 
    ```

- 上传必须将job文件打包成`.zip`文件，这个文件的根目录下包含描述文件