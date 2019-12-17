package com.iwktd.rema;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCommentActivity extends AppCompatActivity {

    private ListView ll_comments = null;
    private ArrayList<TextView> commentList;
    private SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        this.ll_comments= findViewById(R.id.ll_courses);
        // ================ prepare data.
        ArrayList<String> comments = new ArrayList<>();

        comments.add("老师666!");
        comments.add("老师233!");
        comments.add("老师666!");
        comments.add("老师666!");
        List<String> courseNames = new ArrayList<>();
        courseNames.add("高保真音响维修");
        courseNames.add("算法设计");
        courseNames.add("编译原理");
        courseNames.add("HTML前端开发");

        List<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i < 4; i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("comment",comments.get(i));
            map.put("courseName", courseNames.get(i));
            listitem.add(map);
        }

        this.simpleAdapter = new SimpleAdapter(
            this,
                listitem,
                R.layout.layout_re_ma_item,
                new String[]{"comment", "courseName"},
                new int[]{R.id.textview_up, R.id.textview_down}
        );
        this.ll_comments.setAdapter(this.simpleAdapter);

        Log.d("MyCommentActivity", "Open my comments");

        //displayComments();
    }




    public void displayComments(){



    }

}
