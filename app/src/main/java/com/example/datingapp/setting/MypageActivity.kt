package com.example.datingapp.setting

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.auth.UserDataModel
import com.example.datingapp.utils.FirebaseAuthUtils
import com.example.datingapp.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MypageActivity : AppCompatActivity() {

    private val TAG = "MypageActivityTAG"
    private val uid = FirebaseAuthUtils.getUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)



        getMyData()

    }

    private fun getMyData() {
        val myUid = findViewById<TextView>(R.id.myUid)
        val nickname = findViewById<TextView>(R.id.nickname)
        val age = findViewById<TextView>(R.id.age)
        val location = findViewById<TextView>(R.id.location)
        val gender = findViewById<TextView>(R.id.gender)
        val myImage = findViewById<ImageView>(R.id.myImage)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                //!!를 다는 이유는 data가 null이 아닐때
                //값이 무조건 들어있을때
                myUid.text = data!!.uid
                nickname.text = data!!.nickname
                age.text = data!!.age
                location.text = data!!.area
                gender.text = data!!.gender

                val storageRef = Firebase.storage.reference.child(data.uid + ".png")
                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(this@MypageActivity)
                            .load(task.result)
                            .into(myImage)
                    }
                })
            }



            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }
}