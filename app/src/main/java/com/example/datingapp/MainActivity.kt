package com.example.datingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.datingapp.auth.UserDataModel
import com.example.datingapp.setting.SettingActivity
import com.example.datingapp.slider.CardStackAdapter
import com.example.datingapp.utils.FirebaseAuthUtils
import com.example.datingapp.utils.FirebaseRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity :AppCompatActivity() {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager
    private val TAG = "MainActivity"
    private var cnt = 0
    private val uid = FirebaseAuthUtils.getUid()
    private val userInfoList = mutableListOf<UserDataModel>()

    private lateinit var nowUserGender : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val setting = findViewById<ImageView>(R.id.settingIcon)
        setting.setOnClickListener {


            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object  : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                if (direction == Direction.Right) {
                    Toast.makeText(this@MainActivity, "right", Toast.LENGTH_SHORT).show()
                    Log.d("like press user uid", userInfoList[cnt].uid.toString())
                    userLikeOtherUser(uid, userInfoList[cnt].uid.toString())
                }else if(direction == Direction.Left){
                    Toast.makeText(this@MainActivity, "left", Toast.LENGTH_SHORT).show()
                }
                cnt+=1

                if (cnt == userInfoList.size) {
                    getUserCardInfo(nowUserGender)
                    Toast.makeText(this@MainActivity, "new load", Toast.LENGTH_LONG).show()
                }

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }
        })

        //CardStackAdapter에 두번째매개변수 list<String>이 들어가야하므로
        //임의로 testList만들어줌

        getMyData()


        cardStackAdapter = CardStackAdapter(baseContext, userInfoList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter



    }
    private fun getUserCardInfo(gender : String){//data 불러오는부분
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val post = dataSnapshot.getValue<Post>()
                Log.d("what's in snapShot", dataSnapshot.toString())
                for(dataModel in dataSnapshot.children){
                    Log.d("dataSnapShot's children", dataModel.toString())
                    val userInfo = dataModel.getValue(UserDataModel::class.java)
                    //userInfoList.add(userInfo!!)

                    if (userInfo!!.gender.toString().equals(gender)) {
                        //현재 사용자 성별과 같은 성별은 userInfoList에 추가하지 않음
                    }else{
                        userInfoList.add(userInfo!!)
                    }
                }
                //adapter의 데이터가 변화가 생겼을때 자동으로 감지후 adapter를 갱신시킴
                cardStackAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    private fun getMyData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                Log.d("my gender ???" , data!!.gender.toString())
                nowUserGender = data!!.gender.toString()
                getUserCardInfo(nowUserGender)
            }



            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }

    //user의 좋아요를 표시하는 부분
    //db에 저장
    private fun userLikeOtherUser(myUid: String, otherUid: String) {
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("like")
        getOtherUserLikeList(otherUid)
    }

    private fun getOtherUserLikeList(otherUid : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val likeUserKey = dataModel.key.toString()
                    if (likeUserKey.equals(uid)) {
                        Toast.makeText(this@MainActivity, "매칭 완료", Toast.LENGTH_SHORT).show()
                        createNotificationChannel()
                        sendNotification()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    //Notification
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("test_channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        var builder = NotificationCompat.Builder(this, "test_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("매칭완료")
            .setContentText("매칭이 성사되었습니다")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(123, builder.build())
        }
    }

}