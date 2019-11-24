package com.iwktd.rema

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    lateinit var search_class : EditText;
    lateinit var search_btn : Button;
    lateinit var to_signup_btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_class = findViewById(R.id.search_class);
        search_btn = findViewById(R.id.search_btn);
        to_signup_btn = findViewById(R.id.to_signup)
    }

    override fun onStart() {
        super.onStart()

        search_btn.setOnClickListener {
            val content = search_class.text.toString();
            val intent = Intent();
            intent.setClass(this, LessonList::class.java);
            intent.putExtra("Content", content);
            startActivity(intent);
        }

        to_signup_btn.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
