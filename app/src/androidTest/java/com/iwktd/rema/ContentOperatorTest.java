package com.iwktd.rema;

import android.os.Bundle;
import android.util.Log;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContentOperatorTest extends TestCase {
    @Test
    public void testGetAllTables(){
        assertEquals(2, 1+1);
    }

    @Test
    public void testAskForSessionID(){
        // 只有账号密码正确， 才能得到session_id
        String uid = "rema";
        String password = "admin";
        Bundle info = new Bundle();
        info.putString("username", uid);
        info.putString("password", password);
        info.putString("current_hash", "000");

        int id = ContentOperator.askForSessionID(info);
        if (id < 0){
            Log.e("testSignIn", "Failed to get");
        }else{
            Log.d("testSignIn", "OK!---------------------------------------");
        }

    }


}