package com.example.facebook

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebook.Dao.PostDao
import com.example.facebook.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=Firebase.auth

        floatbtn.setOnClickListener{
            val createpost = Intent(this, CreatePost::class.java)
            startActivity(createpost)
        }

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun deletePost(postId: String) {
        Toast.makeText(this, "Deleted Clicked ... ", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.logout -> {
                //Toast.makeText(this,"LOGOUT....", Toast.LENGTH_LONG).show()
                val builder = AlertDialog.Builder(this)
                builder.setTitle("LOGOUT")
                builder.setMessage("Are You Sure ")
                //builder.setIcon(R.drawable.logout)
                builder.setPositiveButton("Yes") { dialogInterface, which ->
//                    if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//                        (getContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager)
//                                .clearApplicationUserData()
//                    }
                    //Toast.makeText(applicationContext,"clicked yes", Toast.LENGTH_LONG).show()

                    
                    Firebase.auth.signOut();
                    val mainActivityIntent = Intent(this, SignInActivity::class.java)
                    startActivity(mainActivityIntent)
                    finish()

//                    auth.signOut();
//                    val intent = Intent(this, SignInActivity::class.java)
//                    mainActivityIntent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
                }
                builder.setNegativeButton("No") { dialogInterface, which ->
                    //Toast.makeText(applicationContext,"clicked No", Toast.LENGTH_LONG).show()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}