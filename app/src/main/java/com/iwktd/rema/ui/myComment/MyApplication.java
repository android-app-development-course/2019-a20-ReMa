/**
 * 没有用------------------------------------------
 */
package com.iwktd.rema.ui.myComment;

import android.app.Application;
import android.content.Context;

//import com.facebook.drawee.backends.pipeline.Fresco;


public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //context = getBaseContext();
//        Fresco.initialize(this);
    }
    //获取全局上下文
    public static Context getContext() {
        return context;
    }
}
