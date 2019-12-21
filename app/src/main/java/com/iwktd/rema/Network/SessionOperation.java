package com.iwktd.rema.Network;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SessionOperation implements Serializable {
    private String session;
    public String latest_hash;

    public SessionOperation(String session){
        this.session = session;
        this.latest_hash = "000000";
    }

    public SessionOperation(String session, String latest_hash){
        this.session = session;
        this.latest_hash = latest_hash;
    }

    public void get_db(){
        if(latest_hash == "000000"){
            // get the whole db
            get_whole_db();
        }
        else{
            get_increment_table();
        }
    }

    public void get_whole_db() {
        HashMap<String, Class> t2c = new HashMap<>();
        String [] t = new String[]{"user","teaching","teacher","course","comments"};
        t2c.put(t[0], TableObjects.user.class);
        t2c.put(t[1], TableObjects.teaching.class);
        t2c.put(t[2], TableObjects.teacher.class);
        t2c.put(t[3], TableObjects.course.class);
        t2c.put(t[4], TableObjects.comments.class);
        Request request = new Request.Builder()
                .url("http://127.0.0.1:5000/mani/get_data/0")
                .header("Cookie", session)
                .build();

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
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
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    String body = responseBody.string();
                    System.out.println(body);
                    //JSONObject jsonObject = JSONObject.parseObject(body);
                    //List<String> table_list = JSON.parseArray(jsonObject.getJSONArray("table_list").toJSONString(), String.class);
                    //for (String s : table_list){
                    //    System.out.println(s);
                    //}

                    //List<JSONObject> tables = JSON.parseArray(jsonObject.get("tables").toString());
                    JSONObject obj = new JSONObject(body);
                    JSONArray table_list = obj.getJSONArray("table_list");

                    for(int i = 0; i < table_list.length(); i++){
                        String table = table_list.getString(i);
                        System.out.println(table);
                    }

                    latest_hash = obj.getString("last_hash");

                    JSONArray tables = obj.getJSONArray("tables");
                    ResponseDB db = new ResponseDB();

                    for(int i = 0; i < tables.length(); i++){
                        JSONObject table = tables.getJSONObject(i);
                        //System.out.println(table.toString());
                        String table_name = table.getString("table");
                        System.out.println("Current table is " + table_name);
                        JSONArray values = table.getJSONArray("values");
                        Class c = t2c.get(table_name);

                        switch (table_name){
                            case "user":{
                                for(int j = 0; j < values.length(); j++) {
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
                    // UNIMPLEMENTED
                    // initialize whole database base on db
                    db.printVec();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void get_increment_table(){
        Request request = new Request.Builder()
                .url("http://127.0.0.1:5000/mani/get_data/" + latest_hash)
                .header("Cookie", session)
                .build();

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
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
                    System.out.println(body);

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
                            System.out.println(s);
                            JSONArray arr = obj.getJSONArray("values");
                            //List<update_db> update_dbList = fastJSON.JSON.parseArray(s, update_db.class);
                            //for(update_db u : update_dbList){
                            //    u.print();
                            //}
                            Vector<TableObjects.update_db> update_list = new Vector();
                            for(int i = 0; i < arr.length(); i++){
                                String obj_s = arr.getJSONObject(i).toString();
                                System.out.println(obj_s);
                                update_list.add(fastJSON.JSON.parseObject(obj_s, TableObjects.update_db.class));
                            }
                            for(TableObjects.update_db u : update_list){
                                u.print();
                            }
                            break;
                        }
                        case 603:{
                            // get the wrong hash
                            // UNIMPLEMENTED
                            // probably clean the database
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

    public void logout(){
        Request request = new Request.Builder()
                .url("http://127.0.0.1:5000/autho/logout")
                .header("Cookie", session)
                .post(new FormBody.Builder().build())
                .build();

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .build();

        try(Response response = client.newCall(request).execute()){
            try(ResponseBody body = response.body()){
                String bodyString = body.string();
                System.out.println(bodyString);
                JSONObject obj = new JSONObject(bodyString);
                String obj_s = obj.toString();
                System.out.println(obj_s);
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
}
