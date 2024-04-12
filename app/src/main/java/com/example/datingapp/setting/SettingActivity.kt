package com.example.datingapp.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.datingapp.IntroActivity
import com.example.datingapp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    private val TAG = "SettingActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val myPageBtn = findViewById<Button>(R.id.myPageBtn)
        val logout = findViewById<Button>(R.id.logout)

        myPageBtn.setOnClickListener{
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()//로그아웃
            Log.d(TAG, "logout ok")

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
    }
}