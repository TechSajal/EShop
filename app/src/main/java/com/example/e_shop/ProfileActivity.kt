package com.example.e_shop

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    lateinit var filepath :Uri
    private val READ_STORAGE_PERMISSION_CODE =2
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val userphoto = findViewById<ImageView>(R.id.iv_user_photo)
        auth = Firebase.auth
        val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentuserid).addSnapshotListener(this, object : EventListener<DocumentSnapshot> {
                override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                    if (value!!.exists()){
                        firstnameprofile.isEnabled = false
                        firstnameprofile.editText!!.setText(value.getString("firstname"))

                        lastnameprofile.isEnabled=false
                        lastnameprofile.editText!!.setText(value.getString("lastname"))

                         emailprofile.isEnabled=false
                        emailprofile.editText!!.setText(value.getString("email"))
                    }
                }
            })
        userphoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                val i = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
            }else{
                       ActivityCompat.requestPermissions(
                               this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                               READ_STORAGE_PERMISSION_CODE
                       )

            }
        }



        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val i = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
            }else{
                Toast.makeText(applicationContext,"Storage Permission Denied",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==111 && resultCode == RESULT_OK && data != null){
            filepath =data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            iv_user_photo.setImageBitmap(bitmap)
        }
    }
}
