package com.iwktd.rema.ui.myComment;

import android.content.Context;
import android.util.Log;

import com.iwktd.rema.ContentOperator;
import com.iwktd.rema.Models.ModelComments;
import com.iwktd.rema.Models.ModelCourse;
import com.iwktd.rema.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class PersonCard  {
    public int avatarUrl; //课程图片
    public String courseName;  //课程评论或标题，超过两行用...
    public String userName; //用户名字
    public int head; //用户头像
    public int imgHeight;  //头像图片的高度
    public int like; //点赞图标
    public String likeNum ; //点赞数
    public boolean isLike = false;

    // 2019-12
    public String comment; // 评论内容
    public String intro; // 课程简介
    public int coid;


    public int getAvatarUrl() { return avatarUrl;}
    public String getCourseName() { return courseName; }
    public String getUserName() { return userName; }
    public int getHead() { return head; }
    public int getImgHeight() { return imgHeight; }
    public int getLike() { return like; }
    public void setLike() { }
    public String getLikeNum() { return likeNum; }

    public static List<PersonCard> getPersonCardForMyComments(Context context, int uid){
        ArrayList<HashMap<String, String>> comments = ModelComments.getCommentsByUid(context, uid);
        HashMap<Integer, String> mapper = ModelCourse.getMapCid2Cname(context);

        List<PersonCard> res = new ArrayList<>();
        for(int i = 0 ; i< comments.size(); i++) {
            PersonCard p = new PersonCard();
            Integer cid = parseInt(comments.get(i).get(ModelComments.cid));
            // 2019-12
            p.coid = parseInt(comments.get(i).get(ModelComments.coid));

            p.avatarUrl = R.drawable.img_feed_center_2;
            p.courseName = mapper.get(cid);
            p.userName = comments.get(i).get(ModelComments.content); // 显示评论
            p.head = R.drawable.empty;
            p.imgHeight = 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
            p.like = R.drawable.ic_heart_outline_grey;
            p.likeNum = "";
            res.add(p);
        }
        return res;
    }

    public static List<PersonCard> getPersonCardForMyCollection(Context context){
        int uid = ContentOperator.getUid(context);
        List<PersonCard> res = new ArrayList<>();

        if (uid < 0){
            Log.e("PersonCard", "Error: got uid < 0");
            return res;
        }
        ArrayList<HashMap<String, String>> comments = ModelComments.getCommentsByUid(context, uid);
        HashMap<Integer, String> mapper = ModelCourse.getMapCid2Cname(context);

        for(int i = 0 ; i< comments.size(); i++) {
            PersonCard p = new PersonCard();
            Integer cid = parseInt(comments.get(i).get(ModelComments.cid));
            p.avatarUrl = R.drawable.img_feed_center_2;
            p.courseName = mapper.get(cid);
            p.userName = comments.get(i).get(ModelComments.content); // 显示评论
            p.head = R.drawable.empty;
            p.imgHeight = 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
            p.like = R.drawable.ic_heart_outline_grey;
            p.likeNum = "";
            res.add(p);
        }
        return res;
    }

    public static List<PersonCard> getPersonCardForMyIssues(Context context, int uid){
        ArrayList<HashMap<String, String>> courses = ModelCourse.getMyIssues(context, uid);
        List<PersonCard> res = new ArrayList<>();
        for(int i = 0; i< courses.size(); i++) {
            PersonCard p = new PersonCard();
            p.avatarUrl = R.drawable.img_feed_center_2;
            p.courseName = courses.get(i).getOrDefault(ModelCourse.cname, "Error"); // 课程名
            p.userName = "";
            p.head = R.drawable.empty;
            p.imgHeight = 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
            p.like = R.drawable.ic_heart_outline_grey;
            p.likeNum = "0";
            res.add(p);
        }
        return res;
    }

    public static List<PersonCard> getPersonCardForMyViewHistory(Context context, ArrayList<Integer> cids){
        ArrayList<String> names = new ArrayList<>();
        for (int cid: cids) {
            HashMap<String, String> course = ModelCourse.getCoursesByCid(context, cid);
            names.add(course.getOrDefault(ModelCourse.cname, "Error")); // 不存在的课程 -> Error.
        }

        List<PersonCard> res = new ArrayList<>();
        for(int i = 0; i< cids.size(); i++) {
            PersonCard p = new PersonCard();
            p.avatarUrl = R.drawable.img_feed_center_2;
            p.courseName = names.get(i); // 课程名
            p.userName = "";
            p.head = R.drawable.empty;
            p.imgHeight = 400;
            p.like = R.drawable.ic_heart_outline_grey;
            p.likeNum = "0";
            res.add(p);
        }
        return res;
    }
}
