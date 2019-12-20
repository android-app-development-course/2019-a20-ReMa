package com.iwktd.rema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelCourse extends SQLiteOpenHelper {
    public final static String tblName = "course";
    public final static String cid = "cid";
    public final static String cname = "cname"; // text
    public final static String tname = "tname";     // int
    public final static String intro = "intro"; // text
    public final static String likes = "likes"; // int , default 0
    public final static String uid = "uid";  // id of creator.

    public ModelCourse(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, ModelCourse.tblName, factory, version);
    }

    // 创建数据库， 添加初始数据.
    public void onCreate(SQLiteDatabase db) {
        //String drop = "drop table diary;";
        String createSQL = "create table course(\n" +
                "        cid integer primary key autoincrement,\n" +
                "        cname text,\n" +
                "        tname text not null,\n" +
                "        intro text,\n" +
                "        likes integer not null default 0,\n" +
                "        uid integer not null);";
        //db.execSQL(drop)
        db.execSQL(createSQL);
        String insert = "insert into course (cid, cname, tname, intro, uid) values \n" +
                "    (1, '计算机安全学', '斌头', '学习与密码学相关知识，了解密码学历史', 1),\n" +
                "    (2, '编译原理', '黄煜廉', '学习如何将代码转换成机器可执行代码的整个过程', 2);";
        db.execSQL(insert);
        Log.d("ModelCourse", "create table.");
    }

    // db版本升级时调用
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    // 每次成功打开db后首先执行
    public void onOpen(SQLiteDatabase db ){
        super.onOpen(db);
        Log.d(ModelCourse.tblName, "Open table "+ ModelCourse.tblName + " ---------- ");
    }

    public static ArrayList<HashMap<String, String>> getAllCourse(Context cnt){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelCourse model = new ModelCourse(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
                ModelCourse.tblName, null,null,null,null,null,ModelCourse.cid, null
        );

        while(cursor.moveToNext()){
            HashMap<String, String> mapper = new HashMap<>();
            mapper.put(ModelCourse.cid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelCourse.cname, cursor.getString(1));
            mapper.put(ModelCourse.tname, cursor.getString(2));
            mapper.put(ModelCourse.intro, cursor.getString(3));
            mapper.put(ModelCourse.likes, String.valueOf(cursor.getInt(4)));
            mapper.put(ModelCourse.uid, cursor.getString(5));
            res.add(mapper);
        }
        cursor.close();
        db.close();

        return res;
    }

    // 匹配
    public static ArrayList<HashMap<String, String>> getCoursesByCname(Context cnt, String cname){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelCourse model = new ModelCourse(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
                ModelCourse.tblName,
                null,
                ModelCourse.cname + " like ?",
                new String[]{"%"+cname+"%"},
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            HashMap<String, String> mapper = new HashMap<>();
            mapper.put(ModelCourse.cid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelCourse.cname, cursor.getString(1));
            mapper.put(ModelCourse.tname, cursor.getString(2));
            mapper.put(ModelCourse.intro, cursor.getString(3));
            mapper.put(ModelCourse.likes, String.valueOf(cursor.getInt(4)));
            mapper.put(ModelCourse.uid, cursor.getString(5));
            res.add(mapper);
        }
        cursor.close();
        db.close();

        return res;
    }


    public static ArrayList<HashMap<String, String>> getMyIssues(Context cnt, int uid){
        ArrayList<HashMap<String, String>> res = new ArrayList<>();
        ModelCourse model = new ModelCourse(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
                ModelCourse.tblName,
                null,
                ModelCourse.uid + "=?",
                new String[]{uid+""},
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            HashMap<String, String> mapper = new HashMap<>();
            mapper.put(ModelCourse.cid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelCourse.cname, cursor.getString(1));
            mapper.put(ModelCourse.tname, cursor.getString(2));
            mapper.put(ModelCourse.intro, cursor.getString(3));
            mapper.put(ModelCourse.likes, String.valueOf(cursor.getInt(4)));
            mapper.put(ModelCourse.uid, cursor.getString(5));
            res.add(mapper);
        }
        cursor.close();
        db.close();
        return res;
    }

    // 2019-12
    public static HashMap<String, String> getCoursesByCid(Context cnt, int cid){
        ModelCourse model = new ModelCourse(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();

        Cursor cursor = db.query(
                ModelCourse.tblName,
                null,
                ModelCourse.cid + "=?",
                new String[]{cid+""},
                null,
                null,
                null,
                null
        );
        HashMap<String, String> mapper = new HashMap<>();

        if(cursor.moveToNext()){
            mapper.put(ModelCourse.cid, String.valueOf(cursor.getInt(0)));
            mapper.put(ModelCourse.cname, cursor.getString(1));
            mapper.put(ModelCourse.tname, cursor.getString(2));
            mapper.put(ModelCourse.intro, cursor.getString(3));
            mapper.put(ModelCourse.likes, String.valueOf(cursor.getInt(4)));
            mapper.put(ModelCourse.uid, cursor.getString(5));
        }
        cursor.close();
        db.close();

        return mapper;
    }


    public static int addNewCourse(Context cnt, String cname, String tname, String intro, int likes, int uid){
        int id = -1;
        ModelCourse model = new ModelCourse(cnt, null, 1);
        SQLiteDatabase db = model.getReadableDatabase();
        // 只取一个

        ContentValues values = new ContentValues();
        values.put(ModelCourse.cname, cname);
        values.put(ModelCourse.tname, tname);
        values.put(ModelCourse.intro, intro);
        values.put(ModelCourse.likes, likes);
        values.put(ModelCourse.uid, uid);

        id = (int)db.insert(
                ModelCourse.tblName, null, values
        );
        if (id <= 0){
            Log.e(ModelCourse.tblName, "Failed to insert!");
        }

        return id;
    }

        public static HashMap<Integer, String> getMapCid2Cname(Context context){
            HashMap<Integer, String> res = new HashMap<>();
            ArrayList<HashMap<String, String>> courses = ModelCourse.getAllCourse(context);
            for(int i = 0; i < courses.size(); i++){
                res.put(
                        Integer.parseInt(courses.get(i).get(ModelCourse.cid)),
                        courses.get(i).get(ModelCourse.cname)
                );
            }
            return res;
        }


}
