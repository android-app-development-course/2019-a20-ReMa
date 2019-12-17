package com.iwktd.rema.ui.myComment;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.iwktd.rema.R;

public class CreateCommentPopWin extends PopupWindow {
    private Context mContext;
    private View view;
    private Button btn_save;
    private Button btn_exit;
    public EditText et_comment;

    public CreateCommentPopWin(Activity mContext, View.OnClickListener itemsOnClick) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.create_comment_dialog,null);
        et_comment = (EditText) view.findViewById(R.id.et_mycomment);
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        btn_save = (Button) view.findViewById(R.id.btn_save);

        //设置按钮监听
        btn_save.setOnClickListener(itemsOnClick);
        btn_exit.setOnClickListener(itemsOnClick);

        //设置外部可点击
        this.setOutsideTouchable(true);

        /*        设置弹出窗口特征     */
        //设置视图
        this.setContentView(this.view);

        //设置弹出窗体的宽和高
        Window commentDialog = mContext.getWindow();
        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); //获取屏幕宽高用
        WindowManager.LayoutParams p = commentDialog.getAttributes();//获取对话框当前参数值

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int)(d.getWidth()*0.8));

        //设置弹出窗体可点击
        this.setFocusable(true);
    }

    //自定义一个回调接口来实现click事件（longclick
    public interface OnCommentListener {
        void onItemClick(View v, int position);
        //可添加longclick
    }

    private CreateCommentPopWin.OnCommentListener mOnCommentListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnCommentListener(CreateCommentPopWin.OnCommentListener listener) {
        this.mOnCommentListener = listener;
    }
}
