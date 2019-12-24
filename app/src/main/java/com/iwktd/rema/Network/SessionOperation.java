package com.iwktd.rema.Network;

import android.content.Context;
import android.content.pm.ModuleInfo;

import com.google.android.material.tabs.TabLayout;
import com.iwktd.rema.ContentOperator;
import com.iwktd.rema.Models.ModelComments;
import com.iwktd.rema.Models.ModelCourse;
import com.iwktd.rema.Models.ModelTeacher;
import com.iwktd.rema.Models.ModelTeaching;
import com.iwktd.rema.Models.ModelUser;
import com.iwktd.rema.Objects.ResponseDB;
import com.iwktd.rema.Objects.TableObjects;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;
import android.os.Handler;
import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.iwktd.rema.ContentOperator.client;

public class SessionOperation implements Serializable {
    private String session;
    public String latest_hash;
    private int uid;

    public SessionOperation(String session){
        this.session = session;
        this.latest_hash = "000000";
    }

    public SessionOperation(String session, String latest_hash){
        this.session = session;
        this.latest_hash = latest_hash;
    }

    public void update_db(){
        Log.v("SessionOperation", "Hash = " + ContentOperator.getCurrentHash());
        if(latest_hash == "000000"){
            // get the whole db
            init_whole_db();
        }
        else{
            get_increment_table();
        }
    }

    public void init_whole_db() {
        HashMap<String, Class> t2c = new HashMap<>();
        String [] t = new String[]{"user","teaching","teacher","course","comments"};
        t2c.put(t[0], TableObjects.user.class);
        t2c.put(t[1], TableObjects.teaching.class);
        t2c.put(t[2], TableObjects.teacher.class);
        t2c.put(t[3], TableObjects.course.class);
        t2c.put(t[4], TableObjects.comments.class);
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_GET_DATA + "0")
                .header("Cookie", session)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        Log.v("SessionOperation", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    String body = responseBody.string();
                    //Log.v("SessionOperation", body);
                    //JSONObject jsonObject = JSONObject.parseObject(body);
                    //List<String> table_list = JSON.parseArray(jsonObject.getJSONArray("table_list").toJSONString(), String.class);
                    //for (String s : table_list){
                    //    Log.v("SessionOperation", s);
                    //}

                    //List<JSONObject> tables = JSON.parseArray(jsonObject.get("tables").toString());
                    Log.v("SessionOperation", "Body = " + body);
                    JSONObject obj = new JSONObject(body);
                    Log.v("SessionOperation", String.valueOf(request.url()));
                    JSONArray table_list = obj.getJSONArray("table_list");

                    //for(int i = 0; i < table_list.length(); i++){
                    //    String table = table_list.getString(i);
                    //    Log.v("SessionOperation", table);
                    //}

                    latest_hash = obj.getString("last_hash");

                    JSONArray tables = obj.getJSONArray("tables");
                    ResponseDB db = new ResponseDB();

                    for(int i = 0; i < tables.length(); i++){
                        JSONObject table = tables.getJSONObject(i);
                        //Log.v("SessionOperation", table.toString());
                        String table_name = table.getString("table");
                        Log.v("SessionOperation", "Current table is " + table_name);
                        JSONArray values = table.getJSONArray("values");
                        Class c = t2c.get(table_name);
                        TableObjects tableObjects = new TableObjects();

                        switch (table_name){
                            case "user":{
                                for(int j = 0; j < values.length(); j++) {
                                    //Log.v("SessionOperation", values.getJSONObject(j).toString());
                                    db.userVector.add((TableObjects.user)fastJSON.JSON.parseObject(values.getJSONObject(j).toString(), t2c.get(table_name)));
                                }
                                break;
                            }
                            case "teaching" :{
                                for(int j = 0; j < values.length(); j++) {
                                    db.teachingVector.add((TableObjects.teaching) fastJSON.JSON.parseObject(values.getJSONObject(j).toString(), t2c.get(table_name)));
                                }
                                break;
                            }
                            case "teacher" :{
                                for(int j = 0; j < values.length(); j++) {
                                    db.teacherVector.add((TableObjects.teacher) fastJSON.JSON.parseObject(values.getJSONObject(j).toString(), t2c.get(table_name)));
                                }
                                break;
                            }
                            case "course" :{
                                for(int j = 0; j < values.length(); j++) {
                                    db.courseVector.add((TableObjects.course) fastJSON.JSON.parseObject(values.getJSONObject(j).toString(), t2c.get(table_name)));
                                }
                                break;
                            }
                            case "comments" :{
                                for(int j = 0; j < values.length(); j++) {
                                    db.commentsVector.add((TableObjects.comments) fastJSON.JSON.parseObject(values.getJSONObject(j).toString(), t2c.get(table_name)));
                                }
                                break;
                            }
                        }
                    }
                    // initialize whole database base on db
                    //db.printVec();
                    ContentOperator.updateAllTable(ContentOperator.getGlobalContext(), db);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // mode differ
    public void get_increment_table(){
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_GET_DATA + "/" + latest_hash)
                .header("Cookie", session)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // deal with error code first
                try (ResponseBody responseBody = response.body()){
                    String body = responseBody.string();
                    Log.v("SessionOperation", body);

                    JSONObject obj = new JSONObject(body);
                    int status = obj.getInt("status");
                    switch (status){
                        case 601:{
                            // success but already latest
                            // done
                            break;
                        }
                        case 602:{
                            // get increment table
                            assert obj.getString("table") == "incrementTable";
                            JSONArray op_list = obj.getJSONArray("values");
                            String s = op_list.toString();
                            Log.v("SessionOperation", s);
                            JSONArray arr = obj.getJSONArray("values");
                            //List<update_db> update_dbList = fastJSON.JSON.parseArray(s, update_db.class);
                            //for(update_db u : update_dbList){
                            //    u.print();
                            //}
                            Vector<TableObjects.update_db> update_list = new Vector();
                            for(int i = 0; i < arr.length(); i++){
                                String obj_s = arr.getJSONObject(i).toString();
                                Log.v("SessionOperation", obj_s);
                                update_list.add(fastJSON.JSON.parseObject(obj_s, TableObjects.update_db.class));
                            }
                            for(TableObjects.update_db u : update_list){
                                //u.print();
                                u.execute();
                            }
                            // mode controls
                            // or use latest_hash
                            break;
                        }
                        case 603:{
                            // get the wrong hash
                            // UNIMPLEMENTED
                            // probably clean the database
                            drop_all_db();
                            latest_hash = "000000";
                            update_db();
                        }
                    }
                    // UNIMPLEMENTED
                    // initialize whole database base on db

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void register(String username, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_REGISTER)
                .post(requestBody)
                .header("Cookie", session)
                .build();

        try(Response response = client.newCall(request).execute()){
            try (ResponseBody responseBody = response.body()) {
                String body = responseBody.string();

                JSONObject jsonObject = new JSONObject(body);
                // UNIMPLEMENTED
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_LOGOUT)
                .header("Cookie", session)
                .post(new FormBody.Builder().build())
                .build();

        try(Response response = client.newCall(request).execute()){
            try(ResponseBody body = response.body()){
                String bodyString = body.string();
                Log.v("SessionOperation", bodyString);
                JSONObject obj = new JSONObject(bodyString);
                String obj_s = obj.toString();
                Log.v("SessionOperation", obj_s);
                int status = obj.getInt("status");
                assert status == 0;

                session = "";
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Return status code
    // * 1 is the comment too long
    // * 2 is course does not exist
    public int create_comment(int coid, String comment){
        if(comment.length() > 100){
            // -1 represent the comment is too long
            return 1;
        }
        HashMap<Integer, String> map = ModelCourse.getMapCid2Cname(ContentOperator.getGlobalContext());
        String s = map.get(coid);
        if (s.length() == 0){
            // no such course
            return 2;
        }

        // Be able to add now
        RequestBody requestBody = new FormBody.Builder()
                .add("coid", String.valueOf(coid))
                .add("comment", comment)
                .build();
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_CREATE_COMMENT)
                .post(requestBody)
                .header("Cookie", session)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()){
                    String body = responseBody.string();
                    //Log.v("SessionOperation", "Body = " + body);
                    JSONObject jsonObject = new JSONObject(body);
                    int status = jsonObject.getInt("status");
                    if (status == 102){
                        // UNIMPLEMENTED
                        // Use handler to ???
                        Log.v("create comment", "No enough parameter");
                        return;
                    }
                    else if (status == 100){
                        // 100 is the normal mode
                        Log.v("create comment", "Success");
                        update_db();
                        return;
                    }
                    else{
                        Log.v("create comment", "Unknow error" + body);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return 0;
    }

    public void delete_comment(int coid){
        RequestBody requestBody = new FormBody.Builder()
                .add("coid", String.valueOf(coid))
                .build();
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_DELETE_COMMENT)
                .post(requestBody)
                .header("Cookie", session)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()){
                    String body = responseBody.string();
                    JSONObject jsonObject = new JSONObject(body);
                    int status = jsonObject.getInt("status");

                    if (status == 202 || status == 203){
                        // No such comment
                        // UNIMPLEMENTED
                        Log.v("delete comment", "No such coid or no enough parameter");
                        return;
                    }
                    else if (status == 204){
                        // No priviledge
                        Log.v("delete comment", "No priviledge");
                        return;
                    }
                    else if (status == 200){
                        Log.v("delete comment", "Success");
                        update_db();
                        return;
                    }
                    Log.v("delete comment", "Unknown error" + body);
                    //String new_hash = jsonObject.getString("new_hash");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void update_comment(int coid, String comment){
        RequestBody requestBody = new FormBody.Builder()
                .add("coid", String.valueOf(coid))
                .add("comment", comment)
                .build();
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_UPDATE_COMMENT)
                .post(requestBody)
                .header("Cookie", session)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()){
                    String body = responseBody.string();
                    JSONObject jsonObject = new JSONObject(body);
                    int status = jsonObject.getInt("status");

                    if (status == 202 || status == 203){
                        // No such comment
                        // UNIMPLEMENTED
                        Log.v("update comment", "No such coid or no enough parameter");
                        return;
                    }
                    else if (status == 204){
                        // No priviledge
                        Log.v("update comment", "No priviledge");
                        return;
                    }
                    else if (status == 200){
                        Log.v("update comment", "Success");
                        update_db();
                        return;
                    }
                    Log.v("update comment", "Unknown error" + body);
                    //String new_hash = jsonObject.getString("new_hash");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int create_course(String cname, String tname, String intro){
        // Be able to add now
        RequestBody requestBody = new FormBody.Builder()
                .add("cname", cname)
                .add("tname", tname)
                .add("intro", intro)
                .build();
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_CREATE_COURSE)
                .post(requestBody)
                .header("Cookie", session)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()){
                    String body = responseBody.string();
                    JSONObject jsonObject = new JSONObject(body);
                    int status = jsonObject.getInt("status");
                    if (status == 302){
                        // UNIMPLEMENTED
                        // Use handler to ???
                        Log.v("create course", "No enough parameter");
                        return;
                    }
                    else if (status == 300){
                        Log.v("create course", "Success");
                        update_db();
                    }
                    else{
                        Log.v("create course", "Unknown Error: " + body);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return 0;
    }

    public void delete_course(int cid){
        RequestBody requestBody = new FormBody.Builder()
                .add("cid", String.valueOf(cid))
                .build();
        Request request = new Request.Builder()
                .url(ContentOperator.SERVER_IP + ContentOperator.PATH_DELETE_COURSE)
                .post(requestBody)
                .header("Cookie", session)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()){
                    String body = responseBody.string();
                    JSONObject jsonObject = new JSONObject(body);
                    int status = jsonObject.getInt("status");

                    if (status == 402 || status == 403){
                        // No such comment
                        // UNIMPLEMENTED
                        Log.v("delete course", "No such course or enough parameter");
                        return;
                    }
                    else if (status == 404){
                        // No priviledge
                        Log.v("delete course", "No priviledge");
                        return;
                    }
                    else if (status == 400){
                        Log.v("delete course", "Success");
                        //String new_hash = jsonObject.getString("new_hash");
                        update_db();
                    }
                    else{
                        Log.v("delete course", "Unknown Error: " + body);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void drop_all_db(){
        ModelCourse.dropAll(ContentOperator.getGlobalContext());
        ModelComments.dropAll(ContentOperator.getGlobalContext());
        ModelTeacher.dropAll(ContentOperator.getGlobalContext());
        ModelUser.dropAll(ContentOperator.getGlobalContext());
        // UNIMPLEMETED
        //ModelTeaching.dropAll(this.context);
    }
}
