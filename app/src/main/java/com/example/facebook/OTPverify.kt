package com.example.facebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_otpverify.*
import java.util.concurrent.TimeUnit

class OTPverify : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    lateinit var phoneNum :String
    lateinit var otp: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverify)

        phoneNum = intent.getStringExtra("phoneNumber").toString()

        textView2.text = "Phone No " + phoneNum

        verification()

       button2.setOnClickListener{

           var otp_t=otp_view.text.toString().trim()
           if(otp_view.text.toString().isEmpty() ){
               Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
           }
//           else if(otp_view.text.toString().length!=6){
//               Toast.makeText(this, "Please Enter 6 Digit", Toast.LENGTH_SHORT).show()
//           }
           else{
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(otp,otp_t)
               signInWithPhoneAuthCredential(credential)

           }
       }

    }

    private fun verification() {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNum)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    otp = verificationId
                    resendToken = forceResendingToken
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SUCCESSFUL", "signInWithCredential:success")
                    Toast.makeText(this, "signInWithCredential:success", Toast.LENGTH_SHORT).show()
                    val user = task.result?.user
                    val intent = Intent(this ,Dashboad::class.java)
                    startActivity(intent)
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("FAIL", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show()

                }
            }
    }
}


