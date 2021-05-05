package com.example.e_shop.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_shop.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.activity_edit_completed.*
import kotlinx.android.synthetic.main.activity_profile.*

class EditCompletedActivity : AppCompatActivity() {
    val MALE:String = "male"
    val FEMALE:String = "female"
    val MOBILE:String = "mobile"
    val FNAME:String = "firstname"
    val LNAME:String = "lastname"
    val GENDER:String = "gender"
    val IMAGEURL:String = "image"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_completed)

        val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentuserid).addSnapshotListener(this, object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (value!!.exists()) {
                    emailedit.isEnabled = false
                    val saj = value.getString("image")
                    firstnameeditf.setText(value.getString("firstname"))
                    lastnameeditf.setText(value.getString("lastname"))
                    emailedit.setText(value.getString("email"))
                    phoneedf.setText(value.getString("mobile"))
                    Glide.with(this@EditCompletedActivity).load(saj).into(iv_user_photoedit)
                    submitedit.setOnClickListener {
                        if (TextUtils.isEmpty(firstnameeditf.toString()) || TextUtils.isEmpty(lastnameeditf.toString()) || TextUtils.isEmpty(phoneedf.toString())){
                            val snackbar = Snackbar.make(it, "Please Fill empty field", Snackbar.LENGTH_LONG)
                            val sbView: View = snackbar.view
                            sbView.setBackgroundColor(resources.getColor(R.color.design_default_color_error))
                            snackbar.show()
                        }else{

                            val firstname = firstnameeditf.text.toString().trim{ it <= ' ' }
                            if (firstname != value.getString("firstname") && firstname.isNotEmpty()){
                                val userHashMap = HashMap<String, Any>()
                                userHashMap[FNAME] = firstname
                                FirestoreClass().updateUserProfile(this@EditCompletedActivity, userHashMap)
                            }
                            val lastname = lastnameeditf.text.toString().trim { it <= ' '  }
                            if (lastname != value.getString("lastname") && lastname.isNotEmpty()){
                                val userHashMap = HashMap<String, Any>()
                                userHashMap[LNAME] = lastname
                                FirestoreClass().updateUserProfile(this@EditCompletedActivity, userHashMap)
                            }
                            val mobile = phoneedf.text.toString().trim{ it <= ' ' }
                            if (mobile != value.getString("mobile") && mobile.isNotEmpty()){
                                val userHashMap = HashMap<String, Any>()
                                userHashMap[MOBILE] = mobile
                                FirestoreClass().updateUserProfile(this@EditCompletedActivity, userHashMap)
                            }
                            val gender = if (profile_maleedit.isChecked) {
                                MALE
                            } else {
                                FEMALE
                            }
                            val userHashMap = HashMap<String, Any>()
                            userHashMap[GENDER] = gender
                            FirestoreClass().updateUserProfile(this@EditCompletedActivity, userHashMap)

                            Toast.makeText(this@EditCompletedActivity,"Your Profile is updated successfully",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@EditCompletedActivity,DashboardActivity::class.java)
                            startActivity(intent)
                        }

                    }

                     }
            }
        })
    }
}