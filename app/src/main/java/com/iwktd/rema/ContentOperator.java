package com.iwktd.rema;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.iwktd.rema.Models.ModelComments;
import com.iwktd.rema.Models.ModelCourse;
import com.iwktd.rema.Models.ModelMyCollection;
import com.iwktd.rema.Models.ModelTeacher;
import com.iwktd.rema.Models.ModelUser;
import com.iwktd.rema.Network.NetworkInit;
import com.iwktd.rema.Network.SessionOperation;
import com.iwktd.rema.Objects.ResponseDB;
import com.iwktd.rema.Objects.TableObjects;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


// TODO: 部分操作需要询问服务器
// 2019-12 这里包含了很多常量
public class ContentOperator {
   public final static String TAG = "ContentOperator";

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

   public final static String SERVER_IP = "http://10.243.0.186:8080";
   //public final static String SERVER_PORT = "8080";
   public final static String PATH_LOGIN = "/autho/login"; // post
   public final static String PATH_REGISTER = "/autho/register"; // post
   public final static String PATH_LOGOUT = "/autho/logout";

   // 需要补充两条 修改 的路由！
   public final static String PATH_GET_DATA = "/mani/get_data/"; // + current_hash   0 -> all table
   public final static String PATH_CREATE_COMMENT = "/mani/create_comment";
   public final static String PATH_DELETE_COMMENT = "/mani/delete_comment";
    public final static String PATH_UPDATE_COMMENT = "/mani/update_comment";
   public final static String PATH_CREATE_COURSE = "/mani/create_course";
   public final static String PATH_DELETE_COURSE = "/mani/delete_course";
    //public final static String PATH_MODIFY_COURSE= "/mani/create_comment";

    public final static String SP_INFO = "local_info"; // 存储是否第一次打开app\是否登录， 账号名字等等信息
    public final static int MAX_COMMENT_LEN = 100; // 最大100个字符(英文也是)
    public final static String IS_LOGINED = "is_signed_in";
    public final static String KEY_SESSION = "Set-Cookie"; // 用来从Bundle中提取sessionID
    public final static String KEY_USERNAME = "username";
    public final static String KEY_UID = "uid";
    public final static String KEY_PWD = "password";
    public final static String KEY_HASH = "current_hash";

    private static Context GlobalContext = null;

    // Karl Han
    static SessionOperation sessionOperation = null;
    static NetworkInit networkInit = null;
    static OkHttpClient client;

    public synchronized static void setGlobalContext(Context context){
        GlobalContext = context;
    }

    public synchronized static Context getGlobalContext(){
        return GlobalContext;
    }

    public synchronized static void saveCurrentHash(String hash){
        assert(GlobalContext != null);
        GlobalContext
                .getSharedPreferences(ContentOperator.SP_INFO, Context.MODE_PRIVATE)
                .edit()
                .putString(ContentOperator.KEY_HASH, "")
                .apply();
    }

    public synchronized static String getCurrentHash(){
        assert(GlobalContext != null);
        return GlobalContext
                .getSharedPreferences(ContentOperator.SP_INFO, Context.MODE_PRIVATE)
                .getString(ContentOperator.KEY_HASH, "000000");
    }

    ContentOperator(){
        Log.d(ContentOperator.TAG, "Constructor");
    }

    public static void init(Context context){
        ModelUser db_user = new ModelUser(context, null, 1);
        ModelTeacher db_t = new ModelTeacher(context, null, 1);
        ModelCourse db_course = new ModelCourse(context, null, 1);
        ModelComments db_command = new ModelComments(context, null, 1);
        ModelMyCollection db_mycollection = new ModelMyCollection(context, null, 1);

        ContentOperator.client = new OkHttpClient.Builder()
                .callTimeout(20_000, TimeUnit.MILLISECONDS)
                .connectTimeout(20_000, TimeUnit.MILLISECONDS)
                .readTimeout(20_000, TimeUnit.MILLISECONDS)
                .followRedirects(false)
                //.writeTimeout(20_000, TimeUnit.MILLISECONDS)
                .build();
    }

    // 调用了这个方法会清空所有表， 要重启app以清空内存中保留的过时信息！
    public synchronized static void updateAllTable(Context context, ResponseDB resp){
        ModelMyCollection.dropAll(context);
        ModelComments.dropAll(context);
        ModelCourse.dropAll(context);
        ModelUser.dropAll(context);

        {
            ModelUser tb_user = new ModelUser(context, null, 1);
            SQLiteDatabase db_user = tb_user.getWritableDatabase();
            for(TableObjects.user u: resp.userVector){
                ModelUser.addNewUser(context,u.uid, u.username);
            }
        }

        {
            HashMap<Integer, String> mapTid2Tname = ModelTeacher.getMapTid2Tname(context);
            ModelCourse tb_course = new ModelCourse(context, null, 1);
            SQLiteDatabase db_course = tb_course.getWritableDatabase();
            for (TableObjects.course course : resp.courseVector) {
                ModelCourse.addNewCourse(
                        context,
                        course.cid,
                        course.cname,
                        mapTid2Tname.get(course.tid),
                        course.intro,
                        course.likes,
                        course.uid);
            }
        }

        {
            ModelComments tb_comment = new ModelComments(context, null, 1);
            SQLiteDatabase db_comment = tb_comment.getWritableDatabase();
            for(TableObjects.comments com: resp.commentsVector){
                ModelComments.addNewComment(context, com.coid, com.uid, com.content, com.cid);
            }
        }
    }

    // 注册
    public static int register(Bundle info){
        int id = -1;

        return id;
    }

    /*
    // 前置条件: Info 里面已经输入了账号密码
    // 正常返回 非负值 , 异常返回-1
    public static int isLogined(Activity act, Bundle info){
        // 检查是否已经登录
        int resp = -1; // 登陆失败
        SharedPreferences sp = act.getSharedPreferences(ContentOperator.SP_INFO, Activity.MODE_PRIVATE);
        boolean is_signed_up = sp.getBoolean(ContentOperator.IS_LOGINED, false);
        if (!is_signed_up){
            // 如果成功，则更新了info的内容
            int status = askForSessionID(info);
            if (status >= 0){ // ok
                String username = info.getString(ContentOperator.KEY_USERNAME);
                sp.edit()
                        .putBoolean(ContentOperator.IS_LOGINED, true)
                        .putString(ContentOperator.KEY_USERNAME, username)
                        .apply();
                resp = 0;
            }
        }else{
            resp = 1; // 表示已经登录了
        }
        return resp;
    }
    */

    public static int getUid(Context context){
        return context
                .getSharedPreferences(ContentOperator.SP_INFO, Context.MODE_PRIVATE)
                .getInt(ModelUser.uid, -1);
    }


    public static int logOut(Context act, Bundle info){
        SharedPreferences sp = act.getSharedPreferences(ContentOperator.SP_INFO, Activity.MODE_PRIVATE);
        sp.edit().putBoolean(ContentOperator.IS_LOGINED, false).apply();
        return -1;
    }

    // 传递username, password来询问服务器sessionID
    // 如果成功，将sessionID放入info中，并且返回 0; 其他情况都返回-1。
    // 应该先判断返回值，再取 sessionID(key = ContentOperator.KEY_SESSION)
    /*
    public static int askForSessionID(Bundle info){

        String username = info.getString(ContentOperator.KEY_USERNAME);
        String password = info.getString(ContentOperator.KEY_PWD);
        String current_hash = info.getString(ContentOperator.KEY_HASH);

        //创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(20_000, TimeUnit.MILLISECONDS)
                .connectTimeout(20_000, TimeUnit.MILLISECONDS)
                .readTimeout(20_000, TimeUnit.MILLISECONDS)
                .followRedirects(false)
                //.writeTimeout(20_000, TimeUnit.MILLISECONDS)
                .build();
        //创建Request
        String url = ContentOperator.SERVER_IP
                + ContentOperator.SERVER_PORT
                + ContentOperator.PATH_LOGIN;
        RequestBody body = new FormBody.Builder()
                .add(ContentOperator.KEY_USERNAME, username)
                .add(ContentOperator.KEY_PWD, password)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        //创建Call对象
        Call call = client.newCall(request);
        Log.d("signIn", "Create objects. url = " + url);
        try{
            // 同步
            Response response = call.execute();
            String session_id = response.header(ContentOperator.KEY_SESSION);
            if (session_id == null){
                Log.e("signIn", "Did not get session_id");
                return -1;
            }
            if (response.code() == 302) {
                //处理网络请求的响应，处理UI需要在UI线程中处理
                info.putString(ContentOperator.KEY_SESSION, session_id);
                Log.d("signIn", session_id);
                return 0;
            }else{
                Log.e("signIn", "Can't get set-cookie field.");
            }
        }catch (IOException e){
            Log.e("signIn", "Failed");
            e.printStackTrace();
        }
        return -1;

    }

     */

}
