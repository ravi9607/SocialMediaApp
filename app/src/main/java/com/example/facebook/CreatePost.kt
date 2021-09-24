package com.example.facebook

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.facebook.Dao.PostDao
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.IOException

class CreatePost : AppCompatActivity()  {

    private var mSelectedImageFileUri: Uri?=null
    private lateinit var auth: FirebaseAuth
    private  var downloadUrl:String?=null

    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        auth= Firebase.auth

        postDao=PostDao()
        postButton.setOnClickListener {
            val input = postInput.text.toString()
            if(input.isNotEmpty() && downloadUrl?.isNotEmpty() == true){
                Toast.makeText(this, downloadUrl, Toast.LENGTH_SHORT).show()
                postDao.addPost(input, downloadUrl!!)
                finish()
            }
            else{
                Toast.makeText(this,"ENTER POST INPUT",Toast.LENGTH_LONG).show()
            }
        }

        select_image.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                val galleryIntent=Intent(
                    Intent.ACTION_PICK,

                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(galleryIntent,222)

            }else{
                ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                121)

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==121){
            val galleryIntent=Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,222)

        }else{
            Toast.makeText(this, "not give permission", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==222){
                if(data!==null){
                    try {
                        mSelectedImageFileUri = data.data!!
                        imageView.setImageURI(mSelectedImageFileUri)
                        uploadingImage(mSelectedImageFileUri!!)

                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this, "Image selection Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun uploadingImage(image: Uri) {
        val ref = FirebaseStorage.getInstance().reference.child("upload")
        val uploadTask = ref.putFile(image)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){ task ->
            if(!task.isSuccessful){
                Log.e("Error uploading " , task.exception.toString())
            }
            return@Continuation ref.downloadUrl

        }).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.e("Done uploading " , task.result.toString())

                downloadUrl=task.result.toString()
            }

        }
    }
}


