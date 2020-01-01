package com.iwktd.rema.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;

import com.iwktd.rema.CollectionController;
import com.iwktd.rema.R;
import com.iwktd.rema.Utils;

// 对应一个课程的收藏按钮
public class FeedContextMenu extends LinearLayout {
    private static final int CONTEXT_MENU_WIDTH = Utils.dpToPx(240);
    private int feedItem = -1;
    // 2019-12
    private int cid = -1;
    private Context context = null;
    private OnFeedContextMenuItemClickListener onItemClickListener;
    public FeedContextMenu(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    // 这东西应该是把 pos 绑定起来
    public void bindToItem(int feedItem, int cid) {
        this.feedItem = feedItem;
        this.cid = cid;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(FeedContextMenu.this);
    }

    // 这个， 就是点击事件
    @OnClick(R.id.btnCollect)
    public void onReportClick() {
        if (onItemClickListener != null) {
            // ???
            onItemClickListener.onReportClick(feedItem);
        }
        CollectionController.addNewRecord(this.context, this.cid);


    }

//    @OnClick(R.id.btnSharePhoto)
//    public void onSharePhotoClick() {
//        if (onItemClickListener != null) {
//            onItemClickListener.onSharePhotoClick(feedItem);
//        }
//    }
//
//    @OnClick(R.id.btnCopyShareUrl)
//    public void onCopyShareUrlClick() {
//        if (onItemClickListener != null) {
//            onItemClickListener.onCopyShareUrlClick(feedItem);
//        }
//    }
//
//    @OnClick(R.id.btnCancel)
//    public void onCancelClick() {
//        if (onItemClickListener != null) {
//            onItemClickListener.onCancelClick(feedItem);
//        }
//    }

    public void setOnFeedMenuItemClickListener(OnFeedContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnFeedContextMenuItemClickListener {
        public void onReportClick(int feedItem);

        public void onSharePhotoClick(int feedItem);

        public void onCopyShareUrlClick(int feedItem);

        public void onCancelClick(int feedItem);
    }
}