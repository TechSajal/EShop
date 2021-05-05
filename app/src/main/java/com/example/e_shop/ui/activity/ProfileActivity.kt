package com.example.e_shop.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.e_shop.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    private var filepath :Uri?  = null
    private val READ_STORAGE_PERMISSION_CODE =2
    lateinit var auth: FirebaseAuth
    val MALE:String = "male"
    val FEMALE:String = "female"
    val MOBILE:String = "mobile"
    val FNAME:String = "firstname"
    val LNAME:String = "lastname"
    val GENDER:String = "gender"
    val UPDATEPROFILE:String ="profileCompleted"
    val IMAGEURL:String = "image"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val userphoto = findViewById<ImageView>(R.id.iv_user_photo)
        auth = Firebase.auth
        val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentuserid).addSnapshotListener(this, object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (value!!.exists()) {
                        firstnameSetting.isEnabled = false
                        firstnameSetting.editText!!.setText(value.getString("firstname"))

                        lastnameSetting.isEnabled = false
                        lastnameSetting.editText!!.setText(value.getString("lastname"))

                        emailsetting.isEnabled = false
                        emailsetting.editText!!.setText(value.getString("email"))
                }
            }
        })
        userphoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                val i = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
            }else{
                       ActivityCompat.requestPermissions(
                               this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                               READ_STORAGE_PERMISSION_CODE
                       )

            }
        }

//        appCompatButton.setOnClickListener {
//            val userHashMap = HashMap<String, Any>()
//                val gender = if (profile_male.isChecked) {
//                MALE-
//            } else {
//                FEMALE
//            }
//            val mobileNumbera = mobilenoprofile.editText!!.text.toString().trim{ it <= ' ' }
//            if (mobileNumbera.isNotEmpty()) {
//                userHashMap[MOBILE] = mobileNumbera
//            }
//            userHashMap[GENDER] = gender
//            FirestoreClass().updateUserProfile(this, userHashMap)
//
//            if (filepath != null) {
//                val pd = ProgressDialog(this)
//                pd.setTitle("Uploading Profile")
//                pd.show()
//                val imageref = FirebaseStorage.getInstance().reference.child("profile/profilepic.jpg")
//                imageref.putFile(filepath)
//                        .addOnSuccessListener { p0 ->
//                            pd.dismiss()
//                            val snackbar = Snackbar.make(it, "Profile Uploaded", Snackbar.LENGTH_LONG)
//                            val sbView: View = snackbar.view
//                            sbView.setBackgroundColor(resources.getColor(R.color.colorThemeOrange))
//                            snackbar.show()
//                            val image = imageref.downloadUrl.addOnSuccessListener { Task ->
//                                val url = Task.toString()
//                                userHashMap[IMAGEURL] = url
//                                FirestoreClass().updateUserProfile(this, userHashMap)
//                            }
//                            // val B:String = "b"
//                            userHashMap[UPDATEPROFILE] = 1
//                            FirestoreClass().updateUserProfile(this, userHashMap)
//                            val intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)
//                        }
//                        .addOnProgressListener { p0 ->
//                            val progress: Double = (100.0 * p0.bytesTransferred) / p0.totalByteCount
//                            pd.setMessage("Uploaded ${progress.toInt()}%")
//                        }
//
//
//            }
//
//        }
        submitprofile.setOnClickListener {
            val mobileNumbera = mobilenosetting.editText!!.text.toString().trim{ it <= ' ' }
            val drawble = iv_user_photo.drawable

            if (    mobileNumbera.isEmpty() || filepath == null){
                val snackbar = Snackbar.make(it, "Please Fill all the field", Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(resources.getColor(R.color.design_default_color_error))
                snackbar.show()
            }else{
                val userHashMap = HashMap<String, Any>()
                val gender = if (profile_malese.isChecked) {
                    MALE
                } else {
                    FEMALE
                }
                userHashMap[MOBILE] = mobileNumbera
                userHashMap[GENDER] = gender
                FirestoreClass().updateUserProfile(this, userHashMap)
                val pd = ProgressDialog(this)
                pd.setTitle("Uploading Profile")
                pd.show()
                val imageref = FirebaseStorage.getInstance().reference.child("profile/profilepic.jpg")
                imageref.putFile(filepath!!)
                        .addOnSuccessListener { p0 ->
                            pd.dismiss()
                            val snackbar = Snackbar.make(it, "Profile Uploaded", Snackbar.LENGTH_LONG)
                            val sbView: View = snackbar.view
                            sbView.setBackgroundColor(resources.getColor(R.color.colorThemeOrange))
                            snackbar.show()
                            val image = imageref.downloadUrl.addOnSuccessListener { Task ->
                                val url = Task.toString()
                                userHashMap[IMAGEURL] = url
                                FirestoreClass().updateUserProfile(this, userHashMap)
                            }

                            userHashMap[UPDATEPROFILE] = 1
                            FirestoreClass().updateUserProfile(this, userHashMap)
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnProgressListener { p0 ->
                            val progress: Double = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                            pd.setMessage("Uploaded ${progress.toInt()}%")
                        }

            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val i = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
            }else{
                Toast.makeText(applicationContext, "Storage Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==111 && resultCode == RESULT_OK && data != null){
            filepath =data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
           // iv_user_photosetting.setImageBitmap(bitmap)
              Glide.with(this).load(bitmap).into(iv_user_photo)
        }
    }




//     firstnameSetting.editText!!.setText(value.getString("firstname"))
//                        lastnameSetting.editText!!.setText(value.getString("lastname"))
//                        emailsetting.isEnabled = false
//                        emailsetting.editText!!.setText(value.getString("email"))
//                        phone.setText(value.getString("mobile"))
//                        val saj = value.getString("image")
//                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
//                        Glide.with(this@ProfileActivity).load(bitmap).into(iv_user_photosetting)
//                        val firstname = firstnameSetting.editText!!.text.toString().trim{ it <= ' ' }
//                        if (firstname != value.getString("firstname") && firstname.isNotEmpty()){
//                            val userHashMap = HashMap<String, Any>()
//                            userHashMap[FNAME] = firstname
//                            FirestoreClass().updateUserProfile(this@ProfileActivity, userHashMap)
//                        }
//                        val lastname = lastnameSetting.editText!!.text.toString().trim{ it <= ' ' }
//                        if (lastname != value.getString("lastname") && lastname.isNotEmpty()){
//                            val userHashMap = HashMap<String, Any>()
//                            userHashMap[LNAME] = lastname
//                            FirestoreClass().updateUserProfile(this@ProfileActivity, userHashMap)
//
//                        }
//                        val mobile = phone.text.toString().trim { it <=  ' ' }
//                        if (mobile != value.getString("mobile") && mobile.isNotEmpty()){
//                            val userHashMap = HashMap<String, Any>()
//                            userHashMap[MOBILE] = mobile
//                            FirestoreClass().updateUserProfile(this@ProfileActivity, userHashMap)
//                        }
//
//                        val gender = if (profile_malese.isChecked) {
//                            MALE
//                        } else {
//                            FEMALE
//                        }
//                        val userHashMap = HashMap<String, Any>()
//                        userHashMap[GENDER] = gender
//                        FirestoreClass().updateUserProfile(this@ProfileActivity, userHashMap)



}
