package com.iwktd.rema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelComments extends SQLiteOpenHelper {
    public final static String tblName = "comments";
    public final static String coid = "coid";
    public final static String uid = "uid";
    public final static String content = "content";
    public final static String cid = "cid";

   ModelComments(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,ModelComments.tblName, factory, version);
    }

    // 创建数据库， 添加初始数据.
    public void onCreate(SQLiteDatabase db) {
        //String drop = "drop table diary;";
        String createSQL = "create table comments(\n" +
                "        coid integer primary key autoincrement,\n" +
                "        uid integer not null,\n" +
                "        content text not null ,\n" +
                "        cid integer not null);";
        db.execSQL(createSQL);
        String insert = "insert into comments (uid, content, cid) values\n" +
                "    (1, '这门课真的不错', 1),\n" +
                "    (2, '这门课真的不错', 2);";
        db.execSQL(insert);
        Log.d("ModelComments", "create table.");
    }

    // db版本升级时调用
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    // 每次成功打开db后首先执行
    public void onOpen(SQLiteDatabase db ){
        super.onOpen(db);
        Log.d(ModelComments.tblName, "Open table "+ModelComments.tblName + " ---------- ");
    }

    public static ArrayList<HashMap<String, String>> getCommentsByCid(Context cnt, int cid){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelComments model = new ModelComments(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
               ModelComments.tblName,
                null,
                ModelComments.cid + "=?",
                new String[]{cid+""},
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            HashMap<String, String> mapper = new HashMap<>();
            mapper.put(ModelComments.coid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelComments.uid, String.valueOf(cursor.getInt(1)));
            mapper.put(ModelComments.content, cursor.getString(2));
            mapper.put(ModelComments.cid, cursor.getString(3));
            res.add(mapper);
        }
        cursor.close();
        db.close();

        return res;
    }


    public static int addNewComment(Context cnt,  int uid, String content, int cid){
        int id = -1;
       ModelComments model = new ModelComments(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        // 只取一个

        ContentValues values = new ContentValues();
        values.put(ModelComments.uid, uid);
        values.put(ModelComments.content, content);
        values.put(ModelComments.cid, cid);

        id = (int)db.insert(
               ModelComments.tblName, null, values
        );
        if (id <= 0){
            Log.e(ModelComments.tblName, "Failed to insert!");
        }

        return id;
    }

    public static ArrayList<HashMap<String, String>> getCommentsByUid(Context cnt, int uid){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelComments model = new ModelComments(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
                ModelComments.tblName,
                null,
                ModelComments.uid + "=?",
                new String[]{uid+""},
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            HashMap<String, String> mapper = new HashMap<>();
            mapper.put(ModelComments.coid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelComments.uid, String.valueOf(cursor.getInt(1)));
            mapper.put(ModelComments.content,cursor.getString(2));
            mapper.put(ModelComments.cid, String.valueOf(cursor.getInt(3)));
            res.add(mapper);
        }
        cursor.close();
        db.close();

        return res;
    }


}
