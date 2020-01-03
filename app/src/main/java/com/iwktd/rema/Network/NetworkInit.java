package com.iwktd.rema.Network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.iwktd.rema.ContentOperator;
import com.iwktd.rema.LoginActivity;
import com.iwktd.rema.Objects.ResponseDB;
import com.iwktd.rema.Objects.TableObjects.comments;
import com.iwktd.rema.Objects.TableObjects.user;
import com.iwktd.rema.Objects.TableObjects.course;
import com.iwktd.rema.Objects.TableObjects.teacher;
import com.iwktd.rema.Objects.TableObjects.teaching;
import com.iwktd.rema.Objects.TableObjects.update_db;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.*;


public class NetworkInit extends Thread{
    public String username;
    public String password;
    public String latest_hash;

    private Handler UIHandler;
    private LoginActivity loginActivity;

    private OkHttpClient client;
    public String session;

    public NetworkInit(String username, String password, Handler handler, LoginActivity loginActivity){
        this.username = username;
        this.password = password;
        this.latest_hash = "000000";

        this.UIHandler = handler;
        this.loginActivity = loginActivity;

        client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .build();
        Log.v("NetworkInit", "Finish object init");
    }

    public NetworkInit(String username, String password, String hash, Handler handler, LoginActivity loginActivity){
        assert hash.length() == 6;

        this.username = username;
        this.password = password;
        this.latest_hash = hash;

        this.UIHandler = handler;
        this.loginActivity = loginActivity;

        client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .build();
    }

    //public Map<String, String> fetch_data(){

    //}

    public void run(){
        this.retriveSession();
    }

    public void retriveSession(){
        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_LOGIN)
                .post(requestBody)
                .build();

        Log.v("NetworkInit", "Start to retriveSession");
        try(Response response = client.newCall(request).execute()){
            Log.v("NetworkInit", "Response");
            try (ResponseBody responseBody = response.body()) {
                Log.v("NetworkInit", "ResponseBody");
                //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                //for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                //    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                //}

                String body = responseBody.string();
                System.out.println(body);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    int status = jsonObject.getInt("status");
                    String hash = jsonObject.getString("last_hash");
                    Log.d("---hash", hash);
                    // 2019-12 把登录状态, sessionID 记录在SP中
                    // 如果考虑到session失效， 可以留个时间戳， 每次打开计算 duration < TimeLimit
                    ContentOperator
                            .getGlobalContext()
                            .getSharedPreferences(ContentOperator.SP_INFO, Context.MODE_PRIVATE)
                            .edit()
                            .putBoolean(ContentOperator.IS_LOGINED, true)
                            .putString(ContentOperator.KEY_SESSION, session)
                            .apply();
                    // 2019-12, update !
                    ContentOperator.saveCurrentHash(hash);
                    ContentOperator.saveSessionID(session);
                    //ContentOperator.sessionOperation = new SessionOperation(hash, session);
                    if (status == 1){
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loginActivity.dialog.dismiss();
                                loginActivity.loginFailed();
                            }
                        });
                        return;
                    }
                }
                catch (org.json.JSONException e){
                    // 2019-12  重复插入是走了这条路线
                    Log.v("NetworkInit-----", "Body " + body);
                    //Log.d("---", ga)
                    session = responseHeaders.get("Set-Cookie");
                    System.out.println(session);
                    String[] s = session.split(";");
                    System.out.println(s[0]);
                    session = s[0];
                    Log.v("NetworkInit", "Session = " + session);

                    UIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loginActivity.dialog.dismiss();
                            Toast.makeText(loginActivity, "Login success", Toast.LENGTH_SHORT).show();
                            loginActivity.switchToHomePage();
                        }
                    });

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

