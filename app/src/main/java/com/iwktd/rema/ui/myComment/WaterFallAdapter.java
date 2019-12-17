package com.iwktd.rema.ui.myComment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iwktd.rema.R;

import java.util.List;

//import android.net.Uri;
//import com.facebook.drawee.view.SimpleDraweeView;

public class WaterFallAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private Context mContext; //上下文
    private List<PersonCard> mData; //定义数据源
    //public boolean isLike = false; //初始未点赞状态
    //private WaterFallAdapter.OnItem

    //定义构造方法，默认传入上下文和数据源
    public WaterFallAdapter(Context context, List<PersonCard> data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override  //将ItemView渲染进来，创建ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, null);
        return new MyViewHolder(view);
    }

    @Override  //将数据源的数据绑定到相应控件上
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder2 = (MyViewHolder) holder;
        PersonCard personCard = mData.get(position);
//        Uri uri = Uri.parse(personCard.avatarUrl);
        holder2.userAvatar.setImageResource(personCard.getAvatarUrl());
        holder2.userAvatar.getLayoutParams().height = personCard.getImgHeight();
        holder2.courseName.setText(personCard.getCourseName());
        holder2.userName.setText(personCard.getUserName());
        holder2.head.setImageResource(personCard.getHead());
        //holder2.imgLike.setImageResource(personCard.getLike());
        //holder2.likeNum.setText(personCard.getLikeNum());

        holder2.itemView.setTag(position); //点击item
        //holder2.imgLike.setTag(position); //点击点赞
        /*holder2.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLike) {
                    isLike = false;
                    imgLike.setImageResource(R.drawable.ic_heart_red);
                }else {
                    isLike = true;
                    imgLike.setImageResource(R.drawable.ic_heart_outline_grey);
                }
                //imgLike.startAnimation(AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.my_comment_likes));
                imgLike.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.my_comment_likes));
            }
        });*/

        /*public void onClick(View v) {
            if(v.getId() == R.id.imgLike) {
                if(isLike) {
                    isLike = false;
                    imgLike.setImageResource(R.drawable.ic_heart_red);
                }else {
                    isLike = true;
                    imgLike.setImageResource(R.drawable.ic_heart_outline_grey);
                }
                //imgLike.startAnimation(AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.my_comment_likes));
                imgLike.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.my_comment_likes));
            }
        }*/
    }

    @Override //item数量
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    //定义自己的ViewHolder，将View的控件引用在成员变量上
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView userAvatar;
        private TextView courseName;
        private TextView userName;
        private ImageView head;
        //private ImageView imgLike;
        //private TextView likeNum;

        private MyViewHolder(View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            userName = (TextView) itemView.findViewById(R.id.userName);
            head = (ImageView) itemView.findViewById(R.id.head);
            /*imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            likeNum = (TextView) itemView.findViewById(R.id.tv_likeNum);*/

            //为ItemView添加点击事件
            itemView.setOnClickListener(WaterFallAdapter.this);
            //imgLike.setOnClickListener(WaterFallAdapter.this);

        }
    }

    //----------------为item中的控件点击事件处理--------------//
    //item里有多个控件可以点击（item+item内控件）
    public enum ViewName {
        ITEM,
        IMGLIKE
    }

    //自定义一个回调接口来实现click事件（longclick
    public interface OnItemClickListener {
        void onItemClick(View v, ViewName viewName, int position);
        //可添加longclick
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(); //getTag（）获取数据
        if(mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.recyclerview:
                    mOnItemClickListener.onItemClick(v, ViewName.IMGLIKE,position);
                    Log.i("posImg:",position+"");
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM,position);
                    Log.i("posItem:",position+"");
                    break;
            }
        }
    }

}
