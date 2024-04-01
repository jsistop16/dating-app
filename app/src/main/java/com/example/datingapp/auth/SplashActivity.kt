package com.example.datingapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.datingapp.IntroActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    //firebase객체 받아오기
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "SplashActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //val uid = auth.currentUser?.uid.toString()
        val uid = FirebaseAuthUtils.getUid()
        Log.d("this value is what?", auth.currentUser?.uid.toString())

        if(uid == "null"){//로그인이 안되어있는 상황
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, 3000)
        } else {//로그인이 되어있는 상황
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }
}