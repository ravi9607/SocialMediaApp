package com.example.facebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.facebook.Dao.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePost : AppCompatActivity() {
    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postDao=PostDao()
        postButton.setOnClickListener {
            val input = postInput.text.toString()
            if(input.isNotEmpty()){
                postDao.addPost(input)
                finish()
            }
            if(input.isEmpty()){
                Toast.makeText(this,"ENTER POST INPUT",Toast.LENGTH_LONG).show()
            }
        }


    }
}