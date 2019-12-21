package com.iwktd.rema.Objects;

import java.util.Vector;

public class ResponseDB {
    public Vector<TableObjects.user> userVector;
    public Vector<TableObjects.teaching> teachingVector;
    public Vector<TableObjects.teacher> teacherVector;
    public Vector<TableObjects.course> courseVector;
    public Vector<TableObjects.comments> commentsVector;

    public ResponseDB(){
        userVector = new Vector<>();
        teachingVector= new Vector<>();
        teacherVector= new Vector<>();
        courseVector= new Vector<>();
        commentsVector= new Vector<>();
    }

    public void printVec(){
        for (TableObjects.user u : this.userVector){
            u.print();
        }
    }
}
