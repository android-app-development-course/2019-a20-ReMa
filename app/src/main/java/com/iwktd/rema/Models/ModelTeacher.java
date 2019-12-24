package com.iwktd.rema.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.iwktd.rema.ContentOperator;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelTeacher extends SQLiteOpenHelper {
    public static final String tblName = "teacher";
    public static final String tid = "tid";
    public static final String tname = "tname";


    public ModelTeacher(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,ModelTeacher.tblName, factory, version);
    }

    // 创建数据库， 添加初始数据.
    public void onCreate(SQLiteDatabase db) {
        //String drop = "drop table diary;";
        String createSQL = "create table teacher(\n" +
                "        tid integer primary key autoincrement,\n" +
                "        tname text not null);";
        //db.execSQL(drop)
        db.execSQL(createSQL);
        //String insert = "insert into teacher (tid, tname) values\n" +
        //        "    (1, 'Bintou'), " +
        //        "    (2, '浴帘王子');";
        //db.execSQL(insert);
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

    public static HashMap<Integer, String> getMapTid2Tname(Context context){
        HashMap<Integer, String> mapper = new HashMap<>();
        ModelTeacher model = new ModelTeacher(context, null, 1);
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
            mapper.put(cursor.getInt(0), cursor.getString(1));
        }
        cursor.close();
        //db.close();

        return mapper;
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
        //db.close();

        return res;
    }

    public static int addNewTeacher(Context cnt,  int tid, String tname){
        int id = -1;
        ModelTeacher model = new ModelTeacher(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        // 只取一个

        ContentValues values = new ContentValues();
        values.put(ModelTeacher.tname, tname);
        values.put(ModelTeacher.tid, tid);

        id = (int)db.insert(
                ModelTeacher.tblName, null, values
        );
        if (id <= 0){
            Log.e(ModelTeacher.tblName, "Failed to insert!");
        }

        return id;
    }

    public static HashMap<String, String> getTeacherByTid(Context context, int tid){
        HashMap<String, String> mapper = new HashMap<>();

        ModelTeacher table = new ModelTeacher(context, null, 1);
        SQLiteDatabase db = table.getReadableDatabase();

        Cursor cursor = db.query(
                ModelTeacher.tblName,
                null,
                ModelTeacher.tid + "=?",
                new String[]{tid+""},
                null,
                null,
                null,
                null
        );

        if(cursor.moveToNext()){
            mapper.put(ModelTeacher.tid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelTeacher.tname, cursor.getString(1));
        }
        cursor.close();
        return mapper;
    }

    public static int modifyByTid(Context context, int tid, String tname){
        int id = -1;
        ModelTeacher model = new ModelTeacher(context, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ModelTeacher.tid, tid);
        values.put(ModelTeacher.tname, tname);

        id = (int)db.update(
                ModelTeacher.tblName,
                values,
                ModelTeacher.tid + "=?",
                new String[]{tid+""}
        );
        if (id <= 0){
            Log.e(ModelTeacher.tblName, "Failed to insert!");
        }
        return id;
    }

    public static int deleteByTid(Context context, int tid){
        int cnt = 0;
        ModelTeacher model = new ModelTeacher(context, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        cnt = db.delete(
                ModelTeacher.tblName,
                ModelTeacher.tid + "=?",
                new String[]{tid+""}
        );
        //db.close();
        return cnt;
    }

    public static void dropAll(Context context){
        ModelTeacher model = new ModelTeacher(context, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        db.execSQL("delete from " + ModelTeacher.tblName + ";");
        //db.close();
    }


}
