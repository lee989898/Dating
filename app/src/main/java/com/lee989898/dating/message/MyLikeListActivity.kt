package com.lee989898.dating.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lee989898.dating.R
import com.lee989898.dating.auth.UserDataModel
import com.lee989898.dating.utils.FirebaseAuthUtils
import com.lee989898.dating.utils.FirebaseRef

class MyLikeListActivity : AppCompatActivity() {

    private val uid = FirebaseAuthUtils.getUid()
    private val likeUserList = mutableListOf<UserDataModel>()
    private val likeUserListUid = mutableListOf<String>()

    lateinit var listViewAdapter: ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)

        val userListView = findViewById<ListView>(R.id.userListView)

        listViewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listViewAdapter

        getMyLikeList()

        userListView.setOnItemClickListener { adapterView, view, i, l ->
            checkMating(likeUserList[i].uid.toString())
        }
    }

    private fun checkMating(otherUid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.children.count() == 0) {
                    Toast.makeText(this@MyLikeListActivity, "매칭이 되었습니다.", Toast.LENGTH_LONG).show()

                } else {


                    for (dataModel in snapshot.children) {

                        val likeUserKey = dataModel.key.toString()
                        if (likeUserKey.equals(uid)) {
                        } else {
                            Toast.makeText(
                                this@MyLikeListActivity,
                                "매칭이 되지 않았습니다.",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FirebaseRef.userInfoRef.child(otherUid).addValueEventListener(postListener)
    }

    private fun getMyLikeList() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataModel in snapshot.children) {
                    likeUserListUid.add(dataModel.key.toString())
                }
                getUserDataList()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataModel in snapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)

                    if (likeUserListUid.contains(user?.uid)) {
                        likeUserList.add(user!!)
                    }
                }
                listViewAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)


    }
}