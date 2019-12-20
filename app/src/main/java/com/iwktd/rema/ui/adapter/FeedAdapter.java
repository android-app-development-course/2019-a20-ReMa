package com.iwktd.rema.ui.adapter;

import android.content.Context;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.iwktd.rema.ModelCourse;
import com.iwktd.rema.ModelUser;
import com.iwktd.rema.ui.activity.MainActivity;
import com.iwktd.rema.ui.view.LoadingFeedItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.String;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.iwktd.rema.R;

import static java.lang.Integer.parseInt;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    private final List<FeedItem> feedItems = new ArrayList<>();

    private Context context;
    private OnFeedItemClickListener onFeedItemClickListener;

    private boolean showLoadingView = false;

    public FeedAdapter(Context context) {
        this.context = context;
    }

    // 2019-12 --> 给MainActivity
    public HashMap<Integer, Integer> pos2cid = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DEFAULT) {
            // 这个就是首页的 课程简介 的item
            View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
            CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
            setupClickableViews(view, cellFeedViewHolder);
            return cellFeedViewHolder;
        } else if (viewType == VIEW_TYPE_LOADER) {
            LoadingFeedItemView view = new LoadingFeedItemView(context);
            view.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            return new LoadingCellFeedViewHolder(view);
        }

        return null;
    }

    private void setupClickableViews(final View view, final CellFeedViewHolder cellFeedViewHolder) {
        cellFeedViewHolder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onCommentsClick(view, cellFeedViewHolder.getAdapterPosition());
            }
        });
        cellFeedViewHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onMoreClick(v, cellFeedViewHolder.getAdapterPosition());
            }
        });
//        cellFeedViewHolder.ivFeedCenter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int adapterPosition = cellFeedViewHolder.getAdapterPosition();
//                feedItems.get(adapterPosition).likesCount++;
//                notifyItemChanged(adapterPosition, ACTION_LIKE_IMAGE_CLICKED);
//                if (context instanceof MainActivity) {
//                    ((MainActivity) context).showLikedSnackbar();
//                }
//            }
//        });
        cellFeedViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = cellFeedViewHolder.getAdapterPosition();
                //int count = feedItems.get(adapterPosition).likesCount;
                boolean islike = feedItems.get(adapterPosition).isLiked;
                //Log.i("islike1:", ""+islike+" "+count);
                //点赞和取消
                if(feedItems.get(adapterPosition).isLiked) {
                    //count--;
                    feedItems.get(adapterPosition).likesCount--;
                    feedItems.get(adapterPosition).isLiked = false;
                } else {
                    //count++;
                    feedItems.get(adapterPosition).likesCount++;
                    feedItems.get(adapterPosition).isLiked = true;
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).showLikedSnackbar();
                    }
                }
                notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);
            }
        });
//        cellFeedViewHolder.ivUserProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onFeedItemClickListener.onProfileClick(view);
//            }
//        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((CellFeedViewHolder) viewHolder).bindView(feedItems.get(position));

        if (getItemViewType(position) == VIEW_TYPE_LOADER) {
            bindLoadingFeedItem((LoadingCellFeedViewHolder) viewHolder);
        }
    }

    private void bindLoadingFeedItem(final LoadingCellFeedViewHolder holder) {
        holder.loadingFeedItemView.setOnLoadingFinishedListener(new LoadingFeedItemView.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished() {
                showLoadingView = false;
                notifyItemChanged(0);
            }
        });
        holder.loadingFeedItemView.startLoading();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public void updateItems(boolean animated) {
        feedItems.clear();

        // db 读取数据
        ArrayList<HashMap<String, String>> courseInfo = ModelCourse.getAllCourse(this.context);
        this.pos2cid = new HashMap<>();

        for(int i = 0; i < courseInfo.size(); i++){
            HashMap<String, String> c = courseInfo.get(i);
            if (c != null){
                int likes = parseInt(c.getOrDefault(ModelCourse.likes, "0"));
                feedItems.add(
                        new FeedItem(likes,
                                false,
                                "教师: "+c.get(ModelCourse.tname),
                                "课程名: "+c.get(ModelCourse.cname),
                                "创建者: "+c.get(ModelCourse.uid),
                                "简介: "+c.get(ModelCourse.intro)
                                )
                );
                // 创建 pos -> cid, 也即是 i -> cid
                this.pos2cid.put(i, parseInt(c.get(ModelCourse.cid)));
            }
        }

        if (animated) {
            notifyItemRangeInserted(0, feedItems.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public void showLoadingView() {
        showLoadingView = true;
        notifyItemChanged(0);
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.ivFeedCenter)
//        ImageView ivFeedCenter;
        @BindView(R.id.ivFeedBottom)
        ImageView ivFeedBottom;
        @BindView(R.id.btnComments)
        ImageButton btnComments;
        @BindView(R.id.btnLike)
        ImageButton btnLike;
        @BindView(R.id.btnMore)
        ImageButton btnMore;
//        @BindView(R.id.vBgLike)
//        View vBgLike;
//        @BindView(R.id.ivLike)
//        ImageView ivLike;
        @BindView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;
//        @BindView(R.id.ivUserProfile)
//        ImageView ivUserProfile;
        @BindView(R.id.vImageRoot)
        LinearLayout vImageRoot;

        // 2019-12 -----------------------------------------
        @BindView(R.id.creator)
        TextView creator;
        @BindView(R.id.lesson_name)
        TextView lessonname;
        @BindView(R.id.teacher)
        TextView teachername;
        @BindView(R.id.intro)
        TextView intro;

        FeedItem feedItem;

        public CellFeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(FeedItem feedItem) {
            this.feedItem = feedItem;
 //           int adapterPosition = getAdapterPosition();
//            ivFeedCenter.setImageResource(adapterPosition % 2 == 0 ? R.drawable.img_feed_center_1 : R.drawable.img_feed_center_2);
//            ivFeedBottom.setImageResource(adapterPosition % 2 == 0 ? R.drawable.img_feed_bottom_1 : R.drawable.img_feed_bottom_2);
            btnLike.setImageResource(feedItem.isLiked ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
            tsLikesCounter.setCurrentText(vImageRoot.getResources().getQuantityString(
                    R.plurals.likes_count, feedItem.likesCount, feedItem.likesCount
            ));
//            tsLikesCounter.setText(feedItem.likesCount);

            // 2019-12
            teachername.setText(feedItem.teachername);
            //academyname.setText(feedItem.academyname);
            lessonname.setText(feedItem.coursename);
            intro.setText(feedItem.intro);
            creator.setText(feedItem.creator);

        }

        public FeedItem getFeedItem() {
            return feedItem;
        }
    }

    public static class LoadingCellFeedViewHolder extends CellFeedViewHolder {

        LoadingFeedItemView loadingFeedItemView;

        public LoadingCellFeedViewHolder(LoadingFeedItemView view) {
            super(view);
            this.loadingFeedItemView = view;
        }

        @Override
        public void bindView(FeedItem feedItem) {
            super.bindView(feedItem);
        }
    }

    public static class FeedItem {
        public int likesCount;
        public boolean isLiked;

        // 2019-12
        public String coursename;
        public String teachername; //.tname -> tname
        public String creator; // uid ->
        public String intro;

        public FeedItem(int likesCount, boolean isLiked, String teacherName, String coursename, String creater, String intro) {
            this.likesCount = likesCount;
            this.isLiked = isLiked;
            this.coursename = coursename;
            this.teachername = teacherName;
            this.intro = intro;
            this.creator = creater;
        }
    }

    public interface OnFeedItemClickListener {
        void onCommentsClick(View v, int position);

        void onMoreClick(View v, int position);

        void onProfileClick(View v);//点击图片即点赞?
    }
}
