package com.iwktd.rema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalInfo extends AppCompatActivity {

    TextView textview_user_id = null;
    TextView edittext_user_name = null;
    TextView edittext_user_nicheng = null;
    TextView edittext_user_sex= null;
    TextView edittext_school = null;
    TextView edittext_majority = null;
    Bundle personInfo = null;
//    private Button back;
    Boolean isEditable = false; // boolean 不可变
    //boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        personInfo = getIntent().getExtras();
        //Log.d("")
        Log.d("PersonalInfo", "miaomiaomiao");

//        textview_user_id = findViewById(R.id.textview_user_id);
        edittext_user_nicheng = findViewById(R.id.edittext_user_nicheng);
        edittext_user_name = findViewById(R.id.edittext_user_name);
        edittext_user_sex= findViewById(R.id.edittext_user_sex);
        edittext_school = findViewById(R.id.edittext_user_school);
        edittext_majority = findViewById(R.id.edittext_user_majority);
        final Button changeIsEditable = findViewById(R.id.button_edit);
        final Button back = findViewById(R.id.back_personal_info);
        setEditable(isEditable);

        changeIsEditable.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isEditable){
                            isEditable = true;
                            setEditable(true);
                            changeIsEditable.setText("完成");
                        }else{
                            isEditable = false;
                            setEditable(false);
                            // 更改完成， 持久化
                            persistEdition();
                            changeIsEditable.setText("编辑");
                        }
                        Log.d("PersonalInfo", "editable" + isEditable);
                    }
                }
        );
//        back=findViewById(R.id.button_personal_info);
        back.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // startNewActivity;
                        Intent intent = new Intent(PersonalInfo.this, com.iwktd.rema.ui.activity.MainActivity.class);
                        startActivity(intent);
                    }
                }
        );


        // load user's info
        updateInfo();



    }

    void updateInfo(){
//        if (this.textview_user_id != null){
//            this.textview_user_id.setText(String.valueOf(personInfo.getInt("user_id")));
//        }
//        if (this.edittext_user_name != null){
//            this.edittext_user_name.setText(personInfo.getString("user_name"));
//        }

//        if (this.textview_user_id != null){
//            this.textview_user_id.setText("1111");
//        }
        if (this.edittext_user_nicheng != null){
            this.edittext_user_nicheng.setText("小明同学");
        }
        if (this.edittext_user_name != null){
            this.edittext_user_name.setText("admin");
        }
        if (this.edittext_user_sex != null){
            this.edittext_user_sex.setText("男");
        }
        if (this.edittext_school != null){
            this.edittext_school.setText("华南师范大学");
        }
        if (this.edittext_majority != null){
            this.edittext_majority.setText("搬砖");
        }
    }

    void setEditable(boolean flag){
        if (this.edittext_user_nicheng != null){
            this.edittext_user_nicheng.setEnabled(flag);
        }
        if (this.edittext_user_name != null){
            this.edittext_user_name.setEnabled(flag);
        }
        if (this.edittext_user_sex != null){
            this.edittext_user_sex.setEnabled(flag);
        }
        if (this.edittext_school != null){
            this.edittext_school.setEnabled(flag);
        }
        if (this.edittext_majority != null){
            this.edittext_majority.setEnabled(flag);
        }
    }

    void persistEdition(){
        // 将个人信息的更改持久化
    }


}
