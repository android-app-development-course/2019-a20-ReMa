package com.iwktd.rema.Network;

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

    private OkHttpClient client;
    public String session;

    public NetworkInit(String username, String password){
        this.username = username;
        this.password = password;
        this.latest_hash = "000000";

        client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .build();
    }

    public NetworkInit(String username, String password, String hash){
        assert hash.length() == 6;

        this.username = username;
        this.password = password;
        this.latest_hash = hash;
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
                .url("http://127.0.0.1:5000/autho/login")
                .post(requestBody)
                .build();

        try(Response response = client.newCall(request).execute()){
            try (ResponseBody responseBody = response.body()) {
                //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(responseBody.string());

                session = responseHeaders.get("Set-Cookie");
                System.out.println(session);
                String[] s = session.split(";");
                System.out.println(s[0]);
                session = s[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

