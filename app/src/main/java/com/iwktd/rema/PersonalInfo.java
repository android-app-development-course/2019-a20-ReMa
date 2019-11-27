package com.iwktd.rema;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iwktd.rema.R;

public class PersonalInfo extends AppCompatActivity {

    TextView textview_user_id = null;
    TextView edittext_user_name = null;
    TextView edittext_user_sex= null;
    TextView edittext_school = null;
    TextView edittext_majority = null;
    Bundle personInfo = null;
    Boolean isEditable = false; // boolean 不可变
    //boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        personInfo = getIntent().getExtras();
        //Log.d("")
        Log.d("PersonalInfo", "miaomiaomiao");
        textview_user_id = findViewById(R.id.textview_user_id);
        edittext_user_name = findViewById(R.id.edittext_user_name);
        edittext_user_sex= findViewById(R.id.edittext_user_sex);
        edittext_school = findViewById(R.id.edittext_user_school);
        edittext_majority = findViewById(R.id.edittext_user_majority);
        final Button changeIsEditable = findViewById(R.id.button_edit);

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
                            changeIsEditable.setText("编辑");
                        }
                        Log.d("PersonalInfo", "editable" + isEditable);
                    }
                }
        );
        this.textview_user_id.setText(String.valueOf(personInfo.getInt("user_id")));
        this.edittext_user_name.setText(personInfo.getString("user_name"));
        this.edittext_user_sex.setText("男");
        this.edittext_school.setText("华南师范大学");
        this.edittext_majority.setText("搬砖");
    }

    void setEditable(boolean flag){
        this.edittext_user_name.setEnabled(flag);
        this.edittext_user_sex.setEnabled(flag);
        this.edittext_school.setEnabled(flag);
        this.edittext_majority.setEnabled(flag);
    }



}
