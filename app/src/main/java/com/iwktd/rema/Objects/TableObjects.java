package com.iwktd.rema.Objects;

import com.iwktd.rema.ContentOperator;
import com.iwktd.rema.Models.ModelComments;

public class TableObjects {
    public class user {
        public int uid;
        public String username;
        public void print(){
            System.out.println("[\n\tuid = " + uid+ ",\n\tusername = " + username + "\n]");
        }
    }

    public class teaching {
        public int cid;
        public int tid;
    }

    public class teacher {
        public int tid;
        public String tname;
    }

    public class course {
        public int cid;
        public String cname;
        public String intro;
        public int likes;
        public int tid;
        public int uid;
    }

    public class comments {
        public int cid;
        public int coid;
        public String content;
        public int uid;
    }

    // it goes error if field is not public
    public class update_db{
        public String content;
        public int opcode;

        public void print(){
            System.out.println("{\n\topcode = " + opcode + ",\n\tcontent = " + content + "\n}");
        }
        public void execute(){
            String arg = this.content.substring(1, this.content.length()-2);
            System.out.println(arg);
            String []args = arg.split(",");
            System.out.println(args);
            switch (this.opcode){
                case 100:{
                    // insert new comment
                    ModelComments.addNewComment(ContentOperator.getGlobalContext(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2],Integer.parseInt(args[3]) );
                    break;
                }
                case 200:{
                    // delete comment
                    break;
                }
                case 300:{
                    // create course
                    break;
                }
                case 400:{
                    // delete course
                    break;
                }
                case 500:{
                    // like course
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
