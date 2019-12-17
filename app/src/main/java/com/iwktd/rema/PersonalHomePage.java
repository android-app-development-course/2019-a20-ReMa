package com.iwktd.rema;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalHomePage extends AppCompatActivity {

    Bundle data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


//        this.data = getIntent().getExtras();
//        String user_name = data.getString("user_name");
//        Integer user_id = data.getInt("user_id");
//        Integer user_stars = data.getInt("user_stars");
//        Integer user_course = data.getInt("user_course");
//        Integer user_comment = data.getInt("user_comment");
//
//        TextView textViewUserName = findViewById(R.id.textview_user_name);
//        TextView textViewUserStar = findViewById(R.id.textview_mystar);
//        TextView textViewUserComment = findViewById(R.id.textview_my_comment);
//
//        textViewUserName.setText(textViewUserName.getText() + user_name);
////        textViewUserStar.setText(textViewUserName.getText() + user_stars.toString());
//        textViewUserName.setText(textViewUserName.getText() + user_comment.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);



        // ---------------------------------------------------------------- buttons
        Button button_my_info = findViewById(R.id.button_personal_info);
        Button button_my_comment = findViewById(R.id.button_my_comment);
        Button button_my_collection = findViewById(R.id.button_my_collection);
        Button button_view_history = findViewById(R.id.button_view_history);

        setListenerForButtons(
                button_my_info,
                button_my_comment,
                button_my_collection,
                button_view_history
        );
        return true;
    }

    void setListenerForButtons(
            Button button_my_info ,
            Button button_my_comment,
            Button button_my_collection,
            Button button_view_history)
    {
        // 注意this.data状态更新
        final Bundle PersonalInfo = (Bundle)this.data.clone();
        button_my_info.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // startNewActivity;
                        Intent intent = new Intent(PersonalHomePage.this, PersonalInfo.class);
                        intent.putExtras(PersonalInfo);
                        startActivity(intent);
                    }
                }
        );
        button_my_comment.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // startNewActivity;
                        Intent intent = new Intent(PersonalHomePage.this, MyCommentActivity.class);
                        intent.putExtras(PersonalInfo);
                        startActivity(intent);
                    }
                }
        );
        button_my_collection.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // startNewActivity;
                        Intent intent = new Intent(PersonalHomePage.this, MyCourseActivity.class);
                        intent.putExtras(PersonalInfo);
                        startActivity(intent);

                    }
                }
        );
        button_view_history.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // startNewActivity;
                        Intent intent = new Intent(PersonalHomePage.this, PreviewHistory.class);
                        intent.putExtras(PersonalInfo);
                        startActivity(intent);
                    }
                }
        );
    }


}
