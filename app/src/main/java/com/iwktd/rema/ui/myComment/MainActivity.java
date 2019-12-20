package com.iwktd.rema.ui.myComment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.iwktd.rema.ContentOperator;
import com.iwktd.rema.ModelComments;
import com.iwktd.rema.ModelCourse;
import com.iwktd.rema.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Context waterfallContext;
    public List<PersonCard> list;
    //public boolean isLike=false;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter mAdapter;

    // 2019-12
    // cid list, for intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_main);
        init();
    }
    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);//recycleview_main.xml

        int uid = ContentOperator.getUid(this);
        if (uid < 0){
            Log.e("Comments MainActivity", "Error, can't find uid");
            return;
        }

        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WaterFallAdapter(this, buildDataFromUid(uid));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //设置item及item中控件的点击事件
        mAdapter.setOnItemClickListener(MyItemClickListener);

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
     */
    private WaterFallAdapter.OnItemClickListener MyItemClickListener = new WaterFallAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, WaterFallAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()) {
                /*case R.id.imgLike:
                    ImageView imgLike; //如何设置不同item的imgresource？？？？
                    imgLike = findViewById(v.getId());
                    //boolean isLike = list.get(position).isLike;
                    if(list.get(position).isLike) {
                        list.get(position).isLike = false;
                        imgLike.setImageResource(R.drawable.ic_heart_outline_grey);
                    }else {
                        list.get(position).isLike = true;
                        imgLike.setImageResource(R.drawable.ic_heart_red);
                    }
                    imgLike.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.my_comment_likes));
                    Toast.makeText(MainActivity.this,"点赞"+(position+1),Toast.LENGTH_SHORT).show();
                    //Log.i("click:","11111");
                    break;*/
                default:
                    Toast.makeText(MainActivity.this,"item按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    //Log.i("click:","22222");
                    //弹出修改框修改评论
                    showCommentPopWin(v,position);
                    break;
            }
        }
    };

    public CreateCommentPopWin createCommentPopWin;

    public void showCommentPopWin(View view, int position) {

        createCommentPopWin = new CreateCommentPopWin(this,onClickListener);
        createCommentPopWin.showAtLocation(findViewById(R.id.recyclerview), Gravity.CENTER,0,0);
        createCommentPopWin.et_comment.setText(list.get(position).courseName);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_exit:
                    createCommentPopWin.dismiss();
                    break;
                case R.id.btn_save:
                    String com = createCommentPopWin.et_comment.getText().toString();
                    //list.get(position)
                    createCommentPopWin.dismiss();
                    break;
            }
        }
    };

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

    private List<PersonCard> buildDataFromUid(int uid) {

        int[] imgUrs = {R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2, R.drawable.img_feed_center_2};//对应不同课程的图片
        int[] imgHds = {R.drawable.empty, R.drawable.empty, R.drawable.empty, R.drawable.empty, R.drawable.empty, R.drawable.empty};//头像
        int[] imgLikes = {R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey, R.drawable.ic_heart_outline_grey};//未点赞桃心

        ArrayList<HashMap<String, String>> comments = ModelComments.getCommentsByUid(this, uid);
        HashMap<Integer, String> mapper = ModelCourse.getMapCid2Cname(this);

        list = new ArrayList<>();
        for(int i = 0 ; i< comments.size(); i++) {
            PersonCard p = new PersonCard();
            Integer cid = Integer.parseInt(comments.get(i).get(ModelComments.cid));
            p.avatarUrl = imgUrs[i];
            p.courseName = mapper.get(cid);
            p.userName = comments.get(i).get(ModelComments.content); // 显示评论
            p.head = imgHds[i];
            p.imgHeight = 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
            p.like = imgLikes[i];
            p.likeNum = "";
            list.add(p);
        }
        return list;
    }


}
