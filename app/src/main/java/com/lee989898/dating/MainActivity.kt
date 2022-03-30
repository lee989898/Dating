package com.lee989898.dating

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lee989898.dating.auth.IntroActivity
import com.lee989898.dating.auth.UserDataModel
import com.lee989898.dating.setting.SettingActivity
import com.lee989898.dating.slider.CardStackAdapter
import com.lee989898.dating.utils.FirebaseAuthUtils
import com.lee989898.dating.utils.FirebaseRef
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

    private val usersDataList = mutableListOf<UserDataModel>()

    private var userCount = 0

    private lateinit var currnetUserGender: String

    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val setting = findViewById<ImageView>(R.id.settingIcon)
        setting.setOnClickListener {

//            val auth = Firebase.auth
//            auth.signOut()
//
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {

                if (direction == Direction.Right) {

                    userLikeOtherUser(uid, usersDataList[userCount].uid.toString())
                }

                if (direction == Direction.Left) {

                }

                userCount += 1

                if (userCount == usersDataList.count()) {
                    getUserDataList(currnetUserGender)
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

        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

//        getUserDataList()
        getMyUserData()


    }

    private fun getMyUserData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val data = snapshot.getValue(UserDataModel::class.java)

                currnetUserGender = data?.gender.toString()

                getUserDataList(currnetUserGender)


            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)


    }


    private fun getUserDataList(currentUserGender: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataModel in snapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)

                    if (user!!.gender.toString().equals(currentUserGender)) {

                    } else {
                        usersDataList.add(user!!)
                    }


                }

                cardStackAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    private fun userLikeOtherUser(myUid: String, otherUid: String) {

        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")

        getOtherUserLikeList(otherUid)
    }

    private fun getOtherUserLikeList(otherUid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataModel in snapshot.children) {

                    val likeUserKey = dataModel.key.toString()
                    if (likeUserKey == uid) {
                        createNotificationChannel()
                        sendNotification()
                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FirebaseRef.userInfoRef.child(otherUid).addValueEventListener(postListener)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Chanell", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this, "Test channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("매칭완료")
            .setContentText("매칭이 완료되었습니다 저사람도 나를 좋아해요")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123, builder.build())
        }
    }

}