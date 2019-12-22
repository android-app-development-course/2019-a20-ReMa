package com.iwktd.rema.Objects;

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
    }
}
