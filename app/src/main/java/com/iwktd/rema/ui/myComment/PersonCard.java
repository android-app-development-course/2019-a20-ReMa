package com.iwktd.rema.ui.myComment;

public class PersonCard  {
    public int avatarUrl; //课程图片
    public String courseName;  //课程评论或标题，超过两行用...
    public String userName; //用户名字
    public int head; //用户头像
    public int imgHeight;  //头像图片的高度
    public int like; //点赞图标
    public String likeNum ; //点赞数
    public boolean isLike = false;

    public int getAvatarUrl() { return avatarUrl;}
    public String getCourseName() { return courseName; }
    public String getUserName() { return userName; }
    public int getHead() { return head; }
    public int getImgHeight() { return imgHeight; }
    public int getLike() { return like; }
    public void setLike() { }
    public String getLikeNum() { return likeNum; }
}
