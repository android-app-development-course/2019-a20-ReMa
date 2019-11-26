package com.iwktd.rema

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.go_back.*
import kotlinx.android.synthetic.main.lesson_detail.*
import java.nio.file.attribute.AclFileAttributeView

class LessonDetail :Activity() {
    data class comment(val cid : Int, val uid : Int)

    var id : Int = 0

    fun getData() : List<Map<String, String>>{
        val cid = 1
        val list = mutableListOf<Map<String, String>>()

        for (i in 0..20){
            // i is id
            val map = mutableMapOf<String, String>()
            map.put("uid", "User${i} says: ")
            map.put("comment", "G" + ("o".repeat (i +1)) + "d")

            list.add(map)
        }

        return list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lesson_detail)
        actionBar?.hide()

        val back_btn = findViewById<ImageButton>(R.id.back)

        back_btn.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        id = intent.getStringExtra("lesson_id").toInt()
        Log.v("LessonDetail", "id = ${id}")

        // use id to retrive specific lesson and comments

        //detailed_lesson_img.setImageResource()
        //detailed_teacher.setText()
        // ...

        // retrieve a list of comments
        val simpleAdapter = SimpleAdapter(this, getData(), R.layout.comment, arrayOf("uid", "comment"), intArrayOf(R.id.detail_comment_uid, R.id.detailed_comment));
        detailed_listView.adapter = simpleAdapter

    }
}

