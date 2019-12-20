package com.iwktd.rema;

import android.util.Log;

import java.util.ArrayList;

// 2019-12
// 这个类维护一个数据结构，里面存着recordID
// 特点：
// 1. 每一个recordID只最多之多一次(去重)
// 2. 按照访问时间降序排序
// 3. 当队列满， pop出最旧那个recordID
// 4. 插入新
public class LruCache {
    public final static int MAX_SIZE = 16; // 最多16个
    public final static String TAG = "ProViewHistory";

    private ArrayList<Integer> list;

    LruCache(){
        this.list = new ArrayList<>();
        Log.d(TAG, "Create LruCache");
    }

    public void insertNewRecord(int recordID){
        int index = this.list.indexOf(recordID);
        if (index > -1){ // 已存在
            this.list.remove(index); // 0是最旧的
        }else if (this.list.size() >= MAX_SIZE){
            this.list.remove(0);
        }
        this.list.add(recordID);
    }



    public ArrayList<Integer> getAllRecord(){
        return this.list;
    }
}
