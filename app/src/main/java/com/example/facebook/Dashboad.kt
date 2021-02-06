package com.example.facebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dashboad.*
import kotlinx.android.synthetic.main.activity_otpverify.*

class Dashboad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboad)

        button.setOnClickListener {
            Firebase.auth.signOut();
            val mainActivityIntent = Intent(this, loginPhoneNumber::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
    }
}