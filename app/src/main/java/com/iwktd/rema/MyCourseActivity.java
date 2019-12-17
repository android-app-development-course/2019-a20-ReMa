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

public class MyCourseActivity extends AppCompatActivity {

    private ListView ll_courses = null;
    private ArrayList<TextView> commentList;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        this.ll_courses = findViewById(R.id.ll_courses);
        // ================ prepare data.

        ArrayList<String> courseNames = new ArrayList<>();
        courseNames.add("高保真音响维修");
        courseNames.add("算法设计");
        courseNames.add("编译原理");
        courseNames.add("HTML前端开发");
        ArrayList<String> categories = new ArrayList<>();
        categories.add("分类: 电子;");
        categories.add("分类: 计算机科学;");
        categories.add("分类: 计算机科学;");
        categories.add("分类: 软件开发; 前端;");

        List<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("courseName", courseNames.get(i));
            map.put("categories", categories.get(i));
            listitem.add(map);
        }

        this.simpleAdapter = new SimpleAdapter(
                this,
                listitem,
                R.layout.layout_re_ma_item,
                new String[]{"courseName", "categories"},
                new int[]{R.id.textview_up, R.id.textview_down}
        );
        this.ll_courses.setAdapter(this.simpleAdapter);

        Log.d("MyCommentActivity", "Open my collections");

        //displayComments();
    }

}
