package com.iwktd.rema;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

import com.iwktd.rema.R;

public class SearchDemo extends AppCompatActivity {

    // 1. 初始化搜索框变量
    private SearchView searchView;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentOperator.setGlobalContext(this);


        // 2. 绑定视图
        setContentView(R.layout.activity_search);

        // 3. 绑定组件
        searchView = findViewById(R.id.search_view);
        //searchButton = findViewById(R.id.butten_search);

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                // 2019-12
                // do search
                Log.d("SearchDemo", "我收到了 " + string);
            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });

        /*
        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );*/


        //索搜按钮（上次说到的索搜后面加个按钮，我也不知道有什么用）
        Bn_search();
    }
    void Bn_search(){


    }

    @Override
    protected void onStart(){
        super.onStart();
        ContentOperator.setGlobalContext(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ContentOperator.setGlobalContext(this);
    }


}