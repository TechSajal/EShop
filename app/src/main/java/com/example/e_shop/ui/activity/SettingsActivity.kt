package com.example.e_shop.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.e_shop.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentuserid).addSnapshotListener(this, object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (value!!.exists()) {
                    val firstusername = value.getString("firstname")
                    val lastusername = value.getString("lastname")
                    val saj = value.getString("image")
                    namesetting.text = ("$firstusername $lastusername")
                    Glide.with(this@SettingsActivity).load(saj).into(usersetting)
                }
            }
        })

        val logout = findViewById<AppCompatButton>(R.id.submitprofile)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        val edit = findViewById<AppCompatButton>(R.id.edit)
        edit.setOnClickListener {
            val intent = Intent(this,EditCompletedActivity::class.java)
            startActivity(intent)
        }
    }
}