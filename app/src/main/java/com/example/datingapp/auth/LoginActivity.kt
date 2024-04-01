package com.example.datingapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val loginBtn = findViewById<Button>(R.id.loginOk)


        loginBtn.setOnClickListener {

            val email = findViewById<TextInputEditText>(R.id.emailVal).text.toString()
            val password = findViewById<TextInputEditText>(R.id.pwdVal).text.toString()

            Log.d(TAG, email)
            Log.d(TAG, password)

            Log.d("clickclickclickclickclick", "clickclickclickclick")

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Log.d("여기까지 옴?????11111", "signInWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }
}