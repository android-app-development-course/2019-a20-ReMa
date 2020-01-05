## 客户端

 - 登陆信息、用户名等信息用`SharedPreferences`存储，数据用SQLite存储。
 - `Model`类继承了`SQLiteHelper`，并封装了一些静态方法。
 - 第一次启动，将向服务端请求所有数据来初始化数据库。
 - 应用读取数据库，直接使用`Model`类的静态方法读取。
 - 应用需要创建、修改、删除内容，将使用`ContentOperation`中的`SessionOperator`类的静态实例的方法，向服务端询问操作是否可行。当服务器确认操作可行并且在服务端修改成功后，向本地端发回确认信息，客户端随后执行操作。

## Model类:
展示了部分方法， 详细内容见源码.
### ModelUser
```java
    // 获得所有用户
    public static ArrayList<HashMap<String, String>> getAllUsers(Context cnt);
    
    // 获得一个用户
    public HashMap<String, String> getUserByUid(Context cnt, int uid);
    
    // 插入一个新用户，返回插入后的uid
    public static int addNewUser(Context cnt, int uid, String type, String username, String password);

    // 通过username来获得用户，可能有多个
    public static ArrayList<HashMap<String, String>> getUsersByUserName(Context context, String username);

    // 修改指定的用户
    public static int modifyByUid(Context context, int uid, String type, String username, String password);

    // 通过指定uid来删除用户，返回被删除用户的数目。
    public static int deleteByUid(Context context, int uid);

    // 删除user表
    public static void dropAll(Context context);
```

### ModelCourse
```java
    
    public static ArrayList<HashMap<String, String>> getAllCourse(Context cnt);

    // 可能有重名
    public static ArrayList<HashMap<String, String>> getCoursesByCname(Context cnt, String cname);

    // 获得用户创建的课程
    public static ArrayList<HashMap<String, String>> getMyIssues(Context cnt, int uid);

    // 指定cid获得一个课程
    public static HashMap<String, String> getCoursesByCid(Context cnt, int cid);

    public static int addNewCourse(Context cnt, int cid, String cname, String tname, String intro, int likes, int uid);

    // 这个给UI用的
    public static HashMap<Integer, String> getMapCid2Cname(Context context);

    // 修改指定cid的内容
    public static int modifyByCid(Context context, int cid, String cname, String tname, int likes, String intro, int uid);

    // 返回删除了多少条，因为cid是主码，返回值不应该大于1.
    public static int deleteByCid(Context context, int cid);
    
    // 删除表
    public static void dropAll(Context context);
```

### ModelComments
```java
    // 注意，这里接受的是cid，也就是课程的id。这个方法专门用来找某个课程下的评论
    public static ArrayList<HashMap<String, String>> getCommentsByCid(Context cnt, int cid);
    
    public static int addNewComment(Context cnt, int coid, int uid, String content, int cid);

    // 这个函数接受用户的uid， 专门用来寻找某个用户的所有评论
    public static ArrayList<HashMap<String, String>> getCommentsByUid(Context cnt, int uid);

    // 这个是个完整的接口，但是参数比较多
    public static int modifyByCoid(Context context, int coid, int uid, String content, int cid);

    // 用户修改自己的评论，建议用这个，因为一般不需要修改 cid 和 uid.
    // 内部用db.execSQL(), 可能抛出异常
    public static void updateContentByCoid(Context context, int coid, String newIntro);

    // 返回删除了多少条，应该不大于1
    public static int deleteByCoid(Context context, int coid);

    // 删除表
    public static void dropAll(Context context);
```

### ModelMyCollection
这个是给本地的收藏用的:
```java
    String createSQL = "create table " + ModelMyCollection.tblName +"(\n" +
                    "        cid integer primary key not null, \n" +
                    "        date integer not null);";
                
    
    public static int addNewRecord(Context context,int cid);

    // 删了多少条
    public static int deleteRecord(Context context, int cid)；

    // 按时间降序排列
    public static HashSet<Integer> getAllCollection(Context context)；
```

### ContentOperator
提供一些预定义的全局变量和静态方法：
```java
    public final static String TAG = "ContentOperator"; // 给Log用的

    public final static int OP_SIGN_IN = 1;
    public final static int OP_SIGN_UP = 2;
    public final static int OP_CREATE_COURSE = 3;
    public final static int OP_MODIFY_COURSE = 4;
    public final static int OP_DELETE_COURSE = 5;
    public final static int OP_CREATE_COMMENT = 6;
    public final static int OP_MODIFY_COMMENT = 7;
    public final static int OP_DELETE_COMMENT = 8;
    public final static int OP_MODIFY_INFO = 9; // 修改个人信息
    public final static int OP_LIKE = 10;
    public final static int OP_UNLIKE = 11;
    public final static int OP_GET_ALL_TABLE = 12;
    
    public final static String SERVER_IP = "http://10.243.0.186:";
    public final static String SERVER_PORT = "8080";
    public final static String PATH_LOGIN = "/autho/login";  
    public final static String PATH_REGISTER_ = "/autho/register"; 
    public final static String PATH_LOGOUT = "/autho/logout";

   // 需要补充两条 修改 的路由！
   public final static String PATH_GET_DATA = "/mani/get_data/"; // 后接current_hash
    public final static String PATH_CREATE_COMMENT = "/mani/create_comment";
    public final static String PATH_DELETE_COMMENT = "/mani/create_comment";
    //public final static String PATH_MODIFY_COMMENT = "/mani/create_comment";
    public final static String PATH_CREATE_COURSE = "/mani/create_course";
    public final static String PATH_DELETE_COURSE = "/mani/create_course";
    //public final static String PATH_MODIFY_COURSE= "/mani/create_comment";

    public final static String SP_INFO = "local_info"; // 存储是否第一次打开app\是否登录， 账号名字等等信息
    public final static int MAX_COMMENT_LEN = 100; // 最大100个字符(英文也是)
    public final static String IS_LOGINED = "is_signed_in";
    public final static String KEY_SESSION = "Set-Cookie"; // 用来从Bundle中提取sessionID
    public final static String KEY_USERNAME = "username";
    public final static String KEY_PWD = "password";
    public final static String KEY_HASH = "seesion_id";
    
// =============================================================== 方法
    // 注册, 没弄好, 返回uid
    public static int register(Bundle info);

    // 前置条件: Info 里面已经输入了账号密码, key统一用ContentOperator.KEY_XXX!
    // 正常返回 非负值 , 异常返回-1
    public static int isLogined(Activity act, Bundle info);

    // 读取uid
    public static int getUid(Context context);
    
    // 退出登录，返回uid
    public static int logOut(Context act, Bundle info);

    // 传递username, password来询问服务器sessionID
    // 如果成功，将sessionID放入info中，并且返回 0; 其他情况都返回-1。
    // 应该先判断返回值，再取 sessionID(key = ContentOperator.KEY_SESSION)
    public static int askForSessionID(Bundle info);
```

## UI

 - myIssueActivity， 我的发布（课程） 
 - myViewHistoryActivity，我的浏览历史
 - myCollectionActivity，我的收藏 
 - LoginActivity，登陆页面 
 - RegisterActivity，注册页面
 - ui/activity/AddActivity, 点击创建课程按钮所弹出的界面，提供创建课程功能
 - ui/activity/CommentsActivity, 点击课程的评论按钮所进入的画面， 显示某个课程下所有评论。
 - ui/activity/MainActivity, 登陆后进入的主界面，显示所有课程。
 - ui/activity/UserProfileActivity, 个人主页 ui/adapter/commentsAdapter,
 - ui/adapter/FeedAdapter, 容纳课程信息的adapter，给MainActivity.rvFeed用.有一个公共成员pos2cid.
 - ui/view/FeedContextMenu, 对应某个课程的“收藏”按钮。

## 其他
- Utils：提供一些获得屏幕大小的函数
- LruCache，Lru缓存, 用于实现浏览历史