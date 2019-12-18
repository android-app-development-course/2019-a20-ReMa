package com.iwktd.rema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelTeacher extends SQLiteOpenHelper {
    public static final String tblName = "teacher";
    public static final String tid = "tid";
    public static final String tname = "tname";


    ModelTeacher(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,ModelTeacher.tblName, factory, version);
    }

    // 创建数据库， 添加初始数据.
    public void onCreate(SQLiteDatabase db) {
        //String drop = "drop table diary;";
        String createSQL = "create table course(\n" +
                "        tid integer primary key autoincrement,\n" +
                "        tname text not null);";
        //db.execSQL(drop)
        db.execSQL(createSQL);
        String insert = "insert into teacher (tid, tname) values\n" +
                "    (1, 'Bintou'), " +
                "    (2, '浴帘王子');";
        db.execSQL(insert);
        Log.d("ModelTeacher", "create table.");
    }

    // db版本升级时调用
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    // 每次成功打开db后首先执行
    public void onOpen(SQLiteDatabase db ){
        super.onOpen(db);
        Log.d(ModelTeacher.tblName, "Open table "+ModelTeacher.tblName + " ---------- ");
    }

    public static ArrayList<HashMap<String, String>> getAllTeachers(Context cnt){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelTeacher model = new ModelTeacher(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
                ModelTeacher.tblName,
                null,
                null,
                null,
                null,
                null,
                ModelTeacher.tid,
                null
        );

        while(cursor.moveToNext()){
            HashMap<String, String> mapper = new HashMap<>();
            mapper.put(ModelTeacher.tid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelTeacher.tname, cursor.getString(1));
            res.add(mapper);
        }
        cursor.close();
        db.close();

        return res;
    }


    public static int addNewTeacher(Context cnt,  String tname){
        int id = -1;
        ModelTeacher model = new ModelTeacher(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        // 只取一个

        ContentValues values = new ContentValues();
        values.put(ModelTeacher.tname, tname);

        id = (int)db.insert(
                ModelTeacher.tblName, null, values
        );
        if (id <= 0){
            Log.e(ModelTeacher.tblName, "Failed to insert!");
        }

        return id;
    }

}
