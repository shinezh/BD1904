# Maven

## 什么是maven

- 项目构建，一览管理，项目信息等管理工具；
- 针对于java开发，瀑布式开发，敏捷开发；
- maven不涉及编码



### 项目构建

```mermaid
graph LR
A[清理]-->B[编译]
B-->C[单元测试]
C-->D[生成文档]
D-->E[打包]
E-->F[部署工作]
```



### 项目构建工具

- **Ant**：实现项目构建；
- **eclipse**：项目构建和依赖管理，手工操作比较多；
- **maven**：项目构建和依赖管理工具，一键式构建；
  - 使用pom（Project Object Model 项目对象模型）管理
  - 配置pom.xml文件来约定如何实现它的构建和依赖管理；
- **gradle：**项目构建+依赖管理；
  - 使用groovy语言来约定管理（键值对，相比xml更加简洁）；



### 依赖管理

依赖管理即架包管理；



## Maven优点

- **遵循原则**：约定优于配置；

- 便于团队开发；



### 依赖管理好处

- 之前：
  - jar包占用资源过多
  - 依赖冲突，尤其版本冲突；
- pom.xml
  - 约定依赖；

### maven配置setting.xml

- 本地仓库路径
- 私服配置
- jdk版本



## Maven项目开发

- 项目结构

  ```java
  |-- pom.xml               //整个项目的配置文件
  |-- src                   //源代码
  |   |-- main              
  |   |   `-- java          //java源代码
  |   |   `-- resources     //主业务逻辑的配置文件
  |   |   `-- filters
  |   `-- test              //单元测试代码
  |   |   `-- java          //测试源代码
  |   |   `-- resources
  |   |   `-- filters
  |   `-- target            //自动生成，编译，打包
  |   `-- site              //站点文件
  `-- LICENSE.txt           //项目许可文件
  `-- NOTICE.txt            //该项目依赖的库的注意事项
  `-- README.txt
  ```

- maven命令

  | 命令        | 作用                   |
  | ----------- | ---------------------- |
  | mvn compile | 编译主业务逻辑代码     |
  | mvn test    | 执行测试代码           |
  | mvn clean   | 清理                   |
  | mvn package | 打包                   |
  | mvn install | 安装到仓库（本地仓库） |



### mybatis项目创建

- 配置依赖：mvnrepsitory.com
- 配置文件







### web项目开发过程







