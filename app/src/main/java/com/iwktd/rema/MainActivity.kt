package com.iwktd.rema

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.lesson_detail.*

class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView;
    lateinit var search_btn : Button;
    lateinit var to_signup_btn : Button
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
        val r1 = get_map(R.drawable.l1, "python语言程序设计                       ", "100", "Very good", 1)
        mutableList.add(r1)
        val r2 = get_map(R.drawable.l2, "区块链技术与应用                          ", "101", "Very goood", 2)
        mutableList.add( r2)
        val r3 = get_map(R.drawable.l3, "C语言                                     ", "102", "Very gooood", 3)
        mutableList.add( r3)
        val r4 = get_map(R.drawable.l4,"物联网技术概论                             ", "102", "Very gooood", 4)
        mutableList.add( r4)
        val r5 = get_map(R.drawable.l5,"C++                                       " , "102", "Very gooood", 5)
        mutableList.add( r5)
        val r6 = get_map(R.drawable.l5,"C++                                       " , "102", "Very gooood", 6)
        mutableList.add( r6)
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
        search_btn = findViewById(R.id.search)
        to_signup_btn = findViewById(R.id.person)
        search_btn.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, Search::class.java)
            startActivity(intent)
        }

        to_signup_btn.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, SignUpActivity::class.java)
            startActivity(intent)
        }


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

                val intent = Intent()
                intent.setClass(applicationContext, LessonDetail::class.java)
                intent.putExtra("lesson_id", id)
                startActivityForResult(intent, 123)
            }
        }
    }

}

