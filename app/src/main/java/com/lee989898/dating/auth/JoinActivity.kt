package com.lee989898.dating.auth

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lee989898.dating.MainActivity
import com.lee989898.dating.R
import com.lee989898.dating.utils.FirebaseRef

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        val profileImage = findViewById<ImageView>(R.id.imageArea)
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                profileImage.setImageURI(uri)

            }

        )

        profileImage.setOnClickListener {
            getAction.launch("image/*")

        }

        val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pwd = findViewById<TextInputEditText>(R.id.pwdArea)

            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            nickname = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()

            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        uid = user?.uid.toString()

                        val userModel = UserDataModel(
                            uid,
                            nickname,
                            age,
                            gender,
                            city
                        )

                        FirebaseRef.userInfoRef.child(uid).setValue(userModel)



//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)

                    } else {

                    }
                }



        }


    }
}