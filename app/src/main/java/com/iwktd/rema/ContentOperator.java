package com.iwktd.rema;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
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

   public final static String SERVER_IP = "http://10.243.0.186:";
   public final static String SERVER_PORT = "8080";
   public final static String PATH_LOGIN = "/autho/login"; // post
   public final static String PATH_REGISTER_ = "/autho/register"; // post
   public final static String PATH_LOGOUT = "/autho/logout";

   public final static String PATH_GET_DATA = "/mani/get_data/"; // + current_hash   0 -> all table
   public final static String PATH_CREATE_COMMENT = "/mani/create_comment";
   public final static String PATH_DELETE_COMMENT = "/mani/create_comment";
    //public final static String PATH_MODIFY_COMMENT = "/mani/create_comment";
   public final static String PATH_CREATE_COURSE = "/mani/create_course";
   public final static String PATH_DELETE_COURSE = "/mani/create_course";
    //public final static String PATH_MODIFY_COURSE= "/mani/create_comment";

    public final static String SP_INFO = "local_info"; // 存储是否第一次打开app\是否登录， 账号名字等等信息
    public final static int MAX_COMMENT_LEN = 100; // 最大100个字符(英文也是)

    // 2019-12
    ContentOperator(){
        Log.d(ContentOperator.TAG, "Constructor");
    }

    // if ok, return
    public static int askForOK(){
        return 0;
    }

    public static boolean doOperation(int op_type, String hint){
        // hint 错误提示
        return true;
    }


    // 注册
    public static int signUp(Bundle info){
        int id = -1;

        return id;
    }


    // 前置条件: Info 里面已经输入了账号密码
    // 正常返回 非负值 , 异常返回-1
    public static int signIn(Activity act, Bundle info){
        // 检查是否已经登录
        int resp = -1; // 登陆失败
        SharedPreferences sp = act.getSharedPreferences(ContentOperator.SP_INFO, Activity.MODE_PRIVATE);
        boolean is_signed_up = sp.getBoolean("is_signed_in", false);
        if (!is_signed_up){
            // 如果成功，则更新了info的内容
            int status = askForSessionID(info);
            if (status >= 0){ // ok
                String username = info.getString("username");
                sp.edit()
                        .putBoolean("is_signed_in", true)
                        .putString("username", username)
                        .apply();
                resp = 0;
            }
        }else{
            resp = 1; // 表示已经登录了
        }
        return resp;
    }

    public static int getUid(Context act){
        return act.getSharedPreferences(ContentOperator.SP_INFO, Activity.MODE_PRIVATE)
                .getInt(ModelUser.uid, -1);
    }

    public static int logOut(Context act, Bundle info){
        SharedPreferences sp = act.getSharedPreferences(ContentOperator.SP_INFO, Activity.MODE_PRIVATE);
        sp.edit().putBoolean("is_signed_up", false).apply();
        return -1;
    }

    // sign
    public static int askForSessionID(Bundle info){

        String username = info.getString("username");
        String password = info.getString("password");
        String current_hash = info.getString("current_hash");

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
                .add("username", username)
                .add("password", password)
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
            String session_id = response.header("Set-Cookie");
            if (session_id == null){
                Log.e("signIn", "Did not get session_id");
                return -1;
            }
            if (response.code() == 302) {
                //处理网络请求的响应，处理UI需要在UI线程中处理
                info.putString("session_id", session_id);
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

    // 前提是info中有 session_id
    public static int getAllTable(Bundle info){
        // ok -> 0
        // else -> -1
        return 0;
    }

    // if ok, return id; else, return -1
    public static int modifyCourse(Bundle info){
        int id = -1;

        return -1;
    }

    public static int modifyComment(Bundle info){
        int id = -1;

        return -1;
    }

    public static int createCourse(Bundle info){
        int id = -1;

        return -1;
    }

    public static int createComment(Bundle info){
        int id = -1;

        return -1;
    }

    public static int deleteCourse(Bundle info){
        int id = -1;

        return -1;
    }

    public static int deleteComment(Bundle info){
        int id = -1;

        return -1;
    }


}
