package com.example.facebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login_phone_number.*

class loginPhoneNumber : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_phone_number)
        ccp.registerCarrierNumberEditText(phonenumber)

        btn.setOnClickListener {
            val intent = Intent(this,OTPverify::class.java)
            intent.putExtra("phoneNumber",ccp.fullNumberWithPlus.replace(" ",""))
            startActivity(intent)
        }

    }


}