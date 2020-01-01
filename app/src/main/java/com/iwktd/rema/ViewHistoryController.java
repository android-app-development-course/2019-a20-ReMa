package com.iwktd.rema;

import java.util.ArrayList;

public class ViewHistoryController {
    private static LruCache viewList = null;

    private ViewHistoryController(){
    }

    public synchronized static void addNewViewRecord(int cid){
        if (viewList == null){
            viewList = new LruCache();
        }
        viewList.insertNewRecord(cid);
    }

    public synchronized static ArrayList<Integer> getHistory(){
        if (viewList == null){
            viewList = new LruCache();
        }

        return viewList.getAllRecord();
    }
}
