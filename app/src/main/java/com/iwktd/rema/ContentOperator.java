package com.iwktd.rema;

import android.app.Activity;
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

// TODO: 这些操作都是需要跟服务器进行协商的.
// 如果成功则写入本地数据库， 如果失败则显示错误提示，等待用户再次执行.
public class ContentOperator {
    final static String TAG = "ContentOperator";

    final static int OP_SIGN_IN = 1;
    final static int OP_SIGN_UP = 2;
    final static int OP_CREATE_COURSE = 3;
    final static int OP_MODIFY_COURSE = 4;
    final static int OP_DELETE_COURSE = 5;
    final static int OP_CREATE_COMMENT = 6;
    final static int OP_MODIFY_COMMENT = 7;
    final static int OP_DELETE_COMMENT = 8;
    final static int OP_MODIFY_INFO = 9; // 修改个人信息
    final static int OP_LIKE = 10;
    final static int OP_UNLIKE = 11;
    final static int OP_GET_ALL_TABLE = 12;

    final static String SERVER_IP = "http://10.243.0.186:";
    final static String SERVER_PORT = "8080";
    final static String PATH_LOGIN = "/autho/login"; // post
    final static String PATH_REGISTER_ = "/autho/register"; // post
    final static String PATH_LOGOUT = "/autho/logout";

    final static String PATH_GET_DATA = "/mani/get_data/"; // + current_hash   0 -> all table
    final static String PATH_CREATE_COMMENT = "/mani/create_comment";
    final static String PATH_DELETE_COMMENT = "/mani/create_comment";
    // final static String PATH_MODIFY_COMMENT = "/mani/create_comment";
    final static String PATH_CREATE_COURSE = "/mani/create_course";
    final static String PATH_DELETE_COURSE = "/mani/create_course";
    // final static String PATH_MODIFY_COURSE= "/mani/create_comment";

    final static String SP_INFO = "local_info"; // 存储是否第一次打开app\是否登录， 账号名字等等信息

    ContentOperator(){
        //
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


    public static int signIn(Activity act, Bundle info){
        // 检查是否已经登录
        SharedPreferences sp = act.getSharedPreferences(ContentOperator.SP_INFO, Activity.MODE_PRIVATE);
        boolean is_signed_up = sp.getBoolean("is_signed_in", false);
        if (!is_signed_up){
            int status = askForSessionID(info);
            if (status >= 0){ // ok

            }
        }
        return -1;
    }

    public static int logOut(Activity act, Bundle){
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
            String session_id = request.header("Set-Cookie");
            if (response.code() == 302) {
                //处理网络请求的响应，处理UI需要在UI线程中处理
                info.putString("session_id", session_id);
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
