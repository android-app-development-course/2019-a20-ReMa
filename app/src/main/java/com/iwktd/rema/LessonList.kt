package com.iwktd.rema

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import org.jetbrains.anko.startActivityForResult
import kotlin.collections.*

class LessonList :Activity() {
    lateinit var listView: ListView;
    fun get_map(img : Int, lesson_name : String, lesson_score : String, hot_comment : String, id : Int) : Map<String, String>{
        val map = mutableMapOf<String, String>()
        map.put("img", img.toString())
        map.put("lesson_name", lesson_name)
        map.put("lesson_score", lesson_score)
        map.put("hot_comment", hot_comment)
        map.put("id", id.toString())
        return map
    }

    fun getData() : List<Map<String, String>>{
        var mutableList = mutableListOf<Map<String, String>>()

        //var map = Map<String, Any>()
        val r1 = get_map(R.drawable.number1, "CS", "100", "Very good", 1)
        mutableList.add(r1)
        val r2 = get_map(R.drawable.number2, "EE", "101", "Very goood", 2)
        mutableList.add( r2)
        val r3 = get_map(R.drawable.number3, "Art", "102", "Very gooood", 3)
        mutableList.add( r3)

        return mutableList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            123 -> {
                val is_changed = data?.getBooleanExtra("is_changed", false)
                if (is_changed == null){
                    return
                }
                val new_score = data?.getStringExtra("new_score")
                val id = data?.getStringExtra("lesson_id")

                // update the score of specific lesson_id
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lesson_list)

        listView = findViewById(R.id.lessons)

        val simpleAdapter = SimpleAdapter(this, getData(), R.layout.list_row, arrayOf("img", "lesson_name", "lesson_score", "hot_comment"), intArrayOf(R.id.class_img, R.id.lesson_name, R.id.lesson_score, R.id.hot_comment));

        listView.adapter = simpleAdapter

        listView.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item_position = listView.getItemAtPosition(position) as Map<String, String>

                val id  = item_position.get("id")

                //Toast.makeText(applicationContext, "${id} is clicked", Toast.LENGTH_LONG).show()

                //val intent = Intent()
                //intent.setClass(applicationContext, LessonDetail::class.java)
                //intent.putExtra("lesson_id", id)
                //startActivityForResult(intent, 123)
                startActivityForResult<LessonDetail>(123, "lesson_id" to id)
            }
        }
    }

}
