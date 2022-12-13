package com.lee.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.lee.mytodolist.Utils.Constants.TAG
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // title text 변경
        supportActionBar?.title = "to-do List"

        my_edit_text.setOnClickListener {
            Log.d(TAG, "edit_text 클릭")
            val intent = Intent(this, EditTextActivity::class.java)
            startActivity(intent)
        }

        revise_text_btn.setOnClickListener {
            Log.d(TAG, "revise_text 클릭")
            val intent = Intent(this, ReviseTextActivity::class.java)
            startActivity(intent)
        }

        todo_list_btn.setOnClickListener {
            Log.d(TAG, "text_list 클릭")
            val intent = Intent(this, TodoListActivity::class.java)
            startActivity(intent)
        }

        deleted_text_btn.setOnClickListener {
            Log.d(TAG, "deleted_text 클릭")
            val intent = Intent(this, DeletedTextActivity::class.java)
            startActivity(intent)
        }
    }
}