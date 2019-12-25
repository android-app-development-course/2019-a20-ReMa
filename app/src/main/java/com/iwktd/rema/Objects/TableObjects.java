package com.iwktd.rema.Objects;

import android.content.Context;
import android.util.Log;

import com.iwktd.rema.ContentOperator;
import com.iwktd.rema.Models.ModelComments;
import com.iwktd.rema.Models.ModelCourse;

public class TableObjects {
    public static class user {
        public int uid;
        public String username;
        public void print(){
            System.out.println("[\n\tuid = " + uid+ ",\n\tusername = " + username + "\n]");
        }
    }

    public static class teaching {
        public int cid;
        public int tid;
    }

    public static class teacher {
        public int tid;
        public String tname;
    }

    public static class course {
        public int cid;
        public String cname;
        public String intro;
        public int likes;
        public int tid;
        public int uid;
    }

    public static class comments {
        public int cid;
        public int coid;
        public String content;
        public int uid;
    }

    // it goes error if field is not public
    public static class update_db{
        public String content;
        public int opcode;

        public void print(){
            System.out.println("{\n\topcode = " + opcode + ",\n\tcontent = " + content + "\n}");
        }
        public void execute(){
            String arg = this.content.substring(1, this.content.length()-1);
            System.out.println(arg);
            String []args = arg.split(",");
            Context context = ContentOperator.getGlobalContext();
            switch (this.opcode){
                case 100:{
                    // insert new comment
                    //(coid, uid, comment, cid)
                    int coid = Integer.parseInt(args[0]);
                    int uid = Integer.parseInt(args[1]);
                    String comment = args[2].substring(1, args[2].length() -1);
                    int cid = Integer.parseInt(args[3]);
                    ModelComments.addNewComment(context, coid, uid, comment, cid);
                    break;
                }
                case 200:{
                    // delete comment
                    // para(coid)
                    int coid = Integer.parseInt(args[0]);
                    if (args.length == 1){
                        ModelComments.deleteByCoid(context, coid);
                    }
                    else{
                        assert args.length == 2;
                        String comment = args[1];
                        ModelComments.updateContentByCoid(context, coid, comment);
                    }
                    break;
                }
                case 300:{
                    // create course
                    // (cid, cname, tid, intro, uid)
                    int cid = Integer.parseInt(args[0]);
                    String cname = args[1];
                    int tid = Integer.parseInt(args[2]);
                    String intro = args[3];
                    int uid = Integer.parseInt(args[4]);
                    // UNIMPLEMETED
                    //ModelCourse.addNewCourse(context, cid, cname, tid, intro, uid);
                    break;
                }
                case 400:{
                    // delete course
                    // para(cid)
                    int cid = Integer.parseInt(args[0]);
                    if (args.length == 1){
                        ModelCourse.deleteByCid(context, cid);
                    }
                    else {
                        assert args.length == 3;
                        // para(cid, cname, tid, intro)
                        String cname = args[1];
                        int tid = Integer.parseInt(args[2]);
                        String intro = args[3];
                        // UNIMPLEMENTED
                        //ModelCourse.modifyByCid(context, cid, cname, tname, likes, intro, uid);
                    }
                    break;
                }
                case 500:{
                    // like course
                    // para(likes +1, cid)
                    int likes = Integer.parseInt(args[0]);
                    int cid = Integer.parseInt(args[1]);
                    // UNIMPLEMENTED
                    //ModelCourse.
                    break;
                }
                default:{
                    // ERROR
                    this.print();
                }
            }
        }
    }
}
