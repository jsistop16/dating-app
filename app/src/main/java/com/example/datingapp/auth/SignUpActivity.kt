package com.example.datingapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private val TAG = "SignUpActivity"

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth


        val signUpOk = findViewById<Button>(R.id.signUpOk)
        signUpOk.setOnClickListener{
            val email = findViewById<TextInputEditText>(R.id.emailVal)
            val pwd = findViewById<TextInputEditText>(R.id.pwdVal)


            Log.d(TAG, email.text.toString())
            Log.d(TAG, pwd.text.toString())

            //firebase공식문서 참조
            //https://firebase.google.com/docs/auth/android/start?hl=ko&_gl=1*14jb6ez*_up*MQ..*_ga*OTY3MDE4MjYuMTcxMTk3ODgxMA..*_ga_CW55HF8NVT*MTcxMTk3ODgwOS4xLjAuMTcxMTk3ODgwOS4wLjAuMA..
            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Log.d(TAG, user?.uid.toString())
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        /*Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)*/
                    }
                }
        }
    }
}