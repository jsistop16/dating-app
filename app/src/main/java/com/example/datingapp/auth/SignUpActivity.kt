package com.example.datingapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.datingapp.IntroActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.utils.FirebaseRef
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private val TAG = "SignUpActivity"

    private lateinit var auth : FirebaseAuth

    private var uid = ""
    private var gender = ""
    private var area = ""
    private var age = ""
    private var nickname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth


        val signUpOk = findViewById<Button>(R.id.signUpOk)
        signUpOk.setOnClickListener{
            val email = findViewById<TextInputEditText>(R.id.emailVal)
            val pwd = findViewById<TextInputEditText>(R.id.pwdVal)

            gender = findViewById<TextInputEditText>(R.id.gender).text.toString()
            age = findViewById<TextInputEditText>(R.id.age).text.toString()
            area = findViewById<TextInputEditText>(R.id.area).text.toString()
            nickname = findViewById<TextInputEditText>(R.id.nickname).text.toString()



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

                        uid = user?.uid.toString()

                        //write rtdb
                        //write message to the database
                        //val database = Firebase.database
                        //val myRef = database.getReference("message")
                        //myRef.setValue("Hello, World!")
                        //해당경로에 setValue로 데이터 삽입

                        val userModel = UserDataModel(
                            nickname,
                            age,
                            gender,
                            area
                        )
                        FirebaseRef.userInfoRef.child(uid).child("111").setValue(userModel)
                        //.child로 계속 가지치기 할수있음...
                        //개쩐다...

                        val intent = Intent(this, IntroActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            task.exception.toString(),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}