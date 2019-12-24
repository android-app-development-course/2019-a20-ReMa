package com.iwktd.rema.Network;

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
                .add("username","rema")
                .add("password", "admin")
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
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String body = responseBody.string();
                System.out.println(body);
                Log.v("NetworkInit", "Body " + body);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

