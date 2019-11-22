package com.example.signup;

import java.util.List;
import java.util.Vector;

public class Course{
    private Integer courseID;
    private String courseName;
    private Vector<Integer> cateList; // 这里用Integer表示一个标签

    public Course(Integer id, String name, List<Integer> catelis){
        this.courseID = id;
        this.courseName = name;
        this.cateList = new Vector<>();
        this.cateList.addAll(catelis);
    }
}