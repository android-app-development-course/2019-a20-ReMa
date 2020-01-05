# ReMa

ReMa是一个可定制化的通用安卓评论应用，[ReMa_server](https://github.com/Karl-Han/ReMa_server)为用户管理和数据存储的服务端应用。


## 客户端使用说明

> 具体代码中类方法说明请见[ReMa client Documentation](./ReMa client Documentation.md)

### 特点

 - 登陆信息、用户名等信息用`SharedPreferences`存储，数据用SQLite存储。
 - `Model`类继承了`SQLiteHelper`，并封装了一些静态方法。
 - 第一次启动，将向服务端请求所有数据来初始化数据库。
 - 应用读取数据库，直接使用`Model`类的静态方法读取。
 - 应用需要创建、修改、删除内容，将使用`ContentOperator`中的`SessionOperation`类的静态实例的方法，向服务端询问操作是否可行。当服务器确认操作可行并且在服务端修改成功后，向本地端发回确认信息，客户端随后执行操作。

### 注意事项

服务端的**地址、端口、路由信息**等都硬编码在`ContentOperator`的静态变量中，请根据自身运行环境来修改！

原有测试环境为内网： `10.243.0.186:8080`



## 服务端使用说明

详见服务端中的[ReMa_doc](https://github.com/Karl-Han/ReMa_server/blob/master/ReMa_doc.pdf)。