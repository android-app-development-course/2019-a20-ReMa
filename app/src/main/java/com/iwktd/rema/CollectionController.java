package com.iwktd.rema;

import android.content.ContentValues;
import android.content.Context;

import com.iwktd.rema.Models.ModelMyCollection;

import java.util.ArrayList;
import java.util.HashSet;

//
public class CollectionController {
    private static ArrayList<Integer> collection = null;

    public static void init(Context context){
        collection = ModelMyCollection.getAllCollection(context);
    }

    public static void addNewRecord(Context context, int cid){
        if (collection == null){
            init(context);
        }
        if (!collection.contains(cid)){
            ModelMyCollection.addNewRecord(context, cid);
            collection.add(cid);
        }
    }

    public static void deleteRecord(Context context, int cid){
        if (collection == null){
            init(context);
        }
        if (collection.contains(cid)){
            collection.remove(cid);
            ModelMyCollection.deleteRecord(context, cid);
        }
    }

    public static ArrayList<Integer> getCollection(Context context){
        return ModelMyCollection.getAllCollection(context);
    }

}
