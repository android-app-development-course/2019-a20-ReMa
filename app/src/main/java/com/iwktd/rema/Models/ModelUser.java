package com.iwktd.rema.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelUser extends SQLiteOpenHelper  {
    public final static String tblName = "user";
    public final static String uid = "uid";
    public final static String type = "type";
    public final static String username = "username";
    public final static String password = "password";

    public ModelUser(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, ModelUser.tblName, factory, version);
    }

    // 创建数据库， 添加初始数据.
    public void onCreate(SQLiteDatabase db) {
        //String drop = "drop table diary;";
        String createSQL = "create table user(uid integer primary key autoincrement,type text not null default 'U',username text unique not null,password text not null);";
        //db.execSQL(drop)
        db.execSQL(createSQL);
        String insert = "insert into user (uid, type, username, password) values (1, 'A', 'karl-han', 'admin'), (2, 'A', 'rema', 'admin');";
        db.execSQL(insert);
        Log.d("ModelUser", "create table.");
    }

    // db版本升级时调用
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    // 每次成功打开db后首先执行
    public void onOpen(SQLiteDatabase db ){
        super.onOpen(db);
        Log.d(ModelUser.tblName, "Open table "+ ModelUser.tblName + " ---------- ");
    }

    public static ArrayList<HashMap<String, String>> getAllUsers(Context cnt){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelUser model = new ModelUser(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
            ModelUser.tblName,
                null,
                null,
                null,
                null,
                null,
                ModelUser.uid,
                null
        );

        while(cursor.moveToNext()){
                HashMap<String, String> mapper = new HashMap<>();
                mapper.put(ModelUser.uid, String.valueOf(cursor.getInt(0)));
                mapper.put(ModelUser.type, cursor.getString(1));
                mapper.put(ModelUser.username, cursor.getString(2));
                mapper.put(ModelUser.password, cursor.getString(3));
                res.add(mapper);
        }
        cursor.close();
        db.close();

        return res;
    }

    public HashMap<String, String> getUserByUid(Context cnt, int uid){
        HashMap<String, String> res = new HashMap<>();
        ModelUser model = new ModelUser(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        // 只取一个
        Cursor cursor = db.query(
                ModelUser.tblName,
                null,
                ModelUser.uid + "=?",
                new String[]{uid+""},
                null,
                null,
                null,
                "1"
        );
        if (cursor.moveToNext()){
            res.put(ModelUser.uid, String.valueOf(cursor.getInt(0)));
            res.put(ModelUser.type, cursor.getString(1));
            res.put(ModelUser.username, cursor.getString(2));
            res.put(ModelUser.password, cursor.getString(3));
        }
        cursor.close();
        db.close();
        return res;
    }

    // return id
    public static int addNewUser(Context cnt, String type, String username, String password){
        int id = -1;
        ModelUser model = new ModelUser(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        // 只取一个

        ContentValues values = new ContentValues();
        values.put(ModelUser.username, username);
       // values.put(ModelUser.uid, uid);
        values.put(ModelUser.type, type);
        values.put(ModelUser.password, password);

        id = (int)db.insert(
                ModelUser.tblName, null, values
        );
        if (id <= 0){
            Log.e(ModelUser.tblName, "Failed to insert!");
        }
        return id;
    }

    public static ArrayList<HashMap<String, String>> getUsersByUserName(Context context, String username){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelUser model = new ModelUser(context, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
                ModelUser.tblName,
                null,
                ModelUser.username + "=?",
                new String[]{username+""},
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            HashMap<String, String> mapper = new HashMap<>();
            mapper.put(ModelUser.uid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelUser.type, cursor.getString(1));
            mapper.put(ModelUser.username, cursor.getString(2));
            mapper.put(ModelUser.password, cursor.getString(3));
            res.add(mapper);
        }
        cursor.close();
        db.close();

        return res;
    }

    public static int modifyByUid(Context context, int uid, String type, String username, String password) {
        int id = -1;
        ModelUser model = new ModelUser(context, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        // 只取一个

        ContentValues values = new ContentValues();
        values.put(ModelUser.username, username);
        // values.put(ModelUser.uid, uid);
        values.put(ModelUser.type, type);
        values.put(ModelUser.password, password);

        id = (int)db.update(
                ModelUser.tblName,
                values,
                ModelUser.uid + "=?",
                new String[]{uid+""}
        );
        // id 从1开始， 000 -> 获得全部表
        if (id <= 0){
            Log.e(ModelUser.tblName, "Failed to insert!");
        }
        db.close();
        return id;
    }

    // 删除了多少条
    public static int deleteByUid(Context context, int uid){
        int cnt = 0;
        ModelUser model = new ModelUser(context, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        cnt = db.delete(
                ModelUser.tblName,
                ModelUser.uid + "=?",
                new String[]{uid+""}
        );
        db.close();
        return cnt;
    }

    // 删除全部表
    public static void dropAll(Context context){
        ModelUser model = new ModelUser(context, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        db.execSQL("drop table " + ModelUser.tblName + ";");
        db.close();
    }

}
