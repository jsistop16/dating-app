package com.example.datingapp.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.datingapp.IntroActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.utils.FirebaseRef
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class SignUpActivity : AppCompatActivity() {

    private val TAG = "SignUpActivity"

    private lateinit var auth : FirebaseAuth

    private var uid = ""
    private var gender = ""
    private var area = ""
    private var age = ""
    private var nickname = ""
    lateinit var profileImg : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        profileImg = findViewById(R.id.profileImg)
        //핸드폰기기에 있는 이미지를 가져오는거(갤러리 접근?)
        val getAction = registerForActivityResult(
            /*
            갤러리에 접근해서 이미지를 뷰에 가져오는거까지만!!
             */
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                profileImg.setImageURI(uri)
            }
        )


        profileImg.setOnClickListener{
            getAction.launch("image/*")
        }

        val signUpOk = findViewById<Button>(R.id.signUpOk)
        signUpOk.setOnClickListener{
            val email = findViewById<TextInputEditText>(R.id.emailVal)
            val pwd = findViewById<TextInputEditText>(R.id.pwdVal)

            gender = findViewById<TextInputEditText>(R.id.gender).text.toString()
            age = findViewById<TextInputEditText>(R.id.age).text.toString()
            area = findViewById<TextInputEditText>(R.id.area).text.toString()
            nickname = findViewById<TextInputEditText>(R.id.nickname).text.toString()


            //이메일 체크
            //비밀번호 2중 체크
            //회원가입시 입력값 검증 구현해야함


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
                        FirebaseRef.userInfoRef.child(uid).setValue(userModel)
                        //.child로 계속 가지치기 할수있음...
                        //개쩐다...
                        uploadImg(uid)//image upload
                        //img upload 수정해야함
                        //upload안됨

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

    private fun uploadImg(uid : String){
        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid+".png")//uid로 이미지 이름 설정
        Log.d("img uid in???", uid)
        // Get the data from an ImageView as bytes
        profileImg.isDrawingCacheEnabled = true
        profileImg.buildDrawingCache()
        val bitmap = (profileImg.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }
}