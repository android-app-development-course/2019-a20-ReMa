package com.iwktd.rema.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

// mycollection(cid, )
public class ModelMyCollection extends SQLiteOpenHelper {
    public final static String tblName = "mycollection";
    public final static String cid = "cid";
    public final static String date = "date";

    public ModelMyCollection(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, ModelMyCollection.tblName, factory, version);
    }

    // 创建数据库， 添加初始数据.
    public void onCreate(SQLiteDatabase db) {
        //String drop = "drop table diary;";
        String createSQL = "create table mycollection(\n" +
                "        cid integer primary key, \n" +
                "        date integer not null);";
        //db.execSQL(drop)
        db.execSQL(createSQL);
    }

    // db版本升级时调用
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    // 每次成功打开db后首先执行
    public void onOpen(SQLiteDatabase db ){
        super.onOpen(db);
    }


    public static int addNewRecord(Context context,int cid){
        ModelMyCollection table = new ModelMyCollection(context, null, 1);
        SQLiteDatabase db = table.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ModelMyCollection.cid, cid);
        values.put(ModelMyCollection.date, new Date(System.currentTimeMillis()).getTime());
        int id = (int)db.insert(
                ModelMyCollection.tblName,
                null,
                values
        );
        if (id < 0){
            Log.e(ModelMyCollection.tblName, "get id < 0");
        }
        //db.close();
        return id;
    }

    // 删了多少条
    public static int deleteRecord(Context context, int cid){
        ModelMyCollection table = new ModelMyCollection(context, null, 1);
        SQLiteDatabase db = table.getWritableDatabase();
        int cnt = (int)db.delete(
                ModelMyCollection.tblName,
                ModelMyCollection.cid + "=?",
                new String[]{cid+""}
        );

        //db.close();
        return cnt;
    }

    // 按时间降序排列
    public static ArrayList<Integer> getAllCollection(Context context){
        ArrayList<Integer> res = new ArrayList<>();
        ModelMyCollection table = new ModelMyCollection(context, null, 1);
        SQLiteDatabase db = table.getReadableDatabase();
        Cursor cursor = db.query(
                ModelMyCollection.tblName,
                null,
                null,
                null,
                null,
                null,
                ModelMyCollection.date + " desc",
                null
        );
        //db.close();
        while(cursor.moveToNext()){
            res.add(cursor.getInt(0));
        }
        cursor.close();
        return res;
    }
}
