
package com.iwktd.rema;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.iwktd.rema.ui.myComment.PersonCard;
import com.iwktd.rema.ui.myComment.WaterFallAdapter;

import java.util.ArrayList;
import java.util.List;

public class myCollectionActivity extends AppCompatActivity {
    public Context waterfallContext;
    public List<PersonCard> list;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_main_mycollection);
        init();
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);//recycleview_main.xml
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WaterFallAdapter(this, buildData());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //设置item及item中控件的点击事件
        //mAdapter.setOnItemClickListener(MyItemClickListener);
        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


/**
     * item + item里的控件点击监听事件
     *//*
*/
/*
    private WaterFallAdapter.OnItemClickListener MyItemClickListener = new WaterFallAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, WaterFallAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()) {
                case R.id.imgLike:
                    ImageView imgLike;
                    imgLike = findViewById(v.getId());
                    boolean isLike = list.get(position).isLike;
                    if(isLike) {
                        isLike = false;
                        imgLike.setImageResource(R.drawable.ic_heart_outline_grey);
                    }else {
                        isLike = true;
                        imgLike.setImageResource(R.drawable.ic_heart_red);
                    }
                    imgLike.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.my_comment_likes));
                    Toast.makeText(MainActivity.this,"点赞"+position+1,Toast.LENGTH_SHORT).show();
                    Log.i("click:","11111");
                    break;
                default:
                    Toast.makeText(MainActivity.this,"item按钮",Toast.LENGTH_SHORT).show();
                    Log.i("click:","22222");
                    break;
            }
        }
    };*/


    //生成6个明星数据，这些Url地址都来源于网络
    private List<PersonCard> buildData() {

        String[] names = {"啊啊啊111111111111111111111111111111111111111111111111111111","啊啊啊啊","哈哈哈哈","呵呵哈哈哈","哈哈哈哈","哈哈哈哈"};//对应不同课程名称
        int[] imgUrs = {R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2};//对应不同课程的图片
        int[] imgHds = {R.drawable.empty, R.drawable.empty, R.drawable.empty, R.drawable.empty, R.drawable.empty, R.drawable.empty};//头像
        String[] userNames = {"abcde","abcde","abcde","abcde","abcde","abcde"};
        int[] imgLikes = {R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey};//未点赞桃心
        String[] likesNum = {"0","0","0","0","0","0"};//点赞数

        list = new ArrayList<>();
        for(int i=0;i< names.length;i++) {
            PersonCard p = new PersonCard();
            p.avatarUrl = imgUrs[i];
            p.courseName = names[i];
            p.userName = userNames[i];
            p.head = imgHds[i];
            p.imgHeight = (i % 2)*100 + 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
            p.like = imgLikes[i];
            p.likeNum = likesNum[i];
            list.add(p);
        }
        return list;
    }


}
