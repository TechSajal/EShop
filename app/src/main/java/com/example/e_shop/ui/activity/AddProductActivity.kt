package com.example.e_shop.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.e_shop.Modul.Product
import com.example.e_shop.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity() {
    private val READ_STORAGE_PERMISSION_CODE =2
    private var filepath : Uri?  = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        backaddproduct.setOnClickListener {
            super.onBackPressed()
           // return@setOnClickListener
        }

        iv_add_updateProduct.setOnClickListener {
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

        submitproduct.setOnClickListener {
            if (filepath == null || TextUtils.isEmpty(producttytle.editText!!.text.toString()) || TextUtils.isEmpty(productdiscription.editText!!.text.toString()) || TextUtils.isEmpty(productprice.editText!!.text.toString()) ){
                val snackbar = Snackbar.make(it, "Please Fill all the field", Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(resources.getColor(R.color.design_default_color_error))
                snackbar.show()
            }else{
                val pd = ProgressDialog(this)
                pd.setTitle("Uploading Product")
                pd.show()
                val imageref = FirebaseStorage.getInstance().reference.child("product/" +System.currentTimeMillis() + "productpic.jpg" )
                imageref.putFile(filepath!!)
                    .addOnSuccessListener { p0 ->
                        pd.dismiss()
                        val snackbar = Snackbar.make(it, "Profile Uploaded", Snackbar.LENGTH_LONG)
                        val sbView: View = snackbar.view
                        sbView.setBackgroundColor(resources.getColor(R.color.colorThemeOrange))
                        snackbar.show()
                        val image = imageref.downloadUrl.addOnSuccessListener { Task ->
                            val url = Task.toString()
                            val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(currentuserid).addSnapshotListener(this, object : EventListener<DocumentSnapshot> {
                                override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                                    if (value!!.exists()) {
                                        val firstusername = value.getString("firstname")
                                        val lastusername = value.getString("lastname")
                                        val username = ("$firstusername $lastusername")
                                        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
                                        val product = Product(
                                                currentuser,
                                                username,
                                                producttytle.editText!!.text.toString().trim { it <= ' ' },
                                                productprice.editText!!.text.toString().trim { it <= ' ' },
                                                productdiscription.editText!!.text.toString().trim { it <=  ' ' },
                                                url,
                                        )
                                        FirestoreClass().registerproduct(this@AddProductActivity,product)
                                    }
                                }
                            })
                        }

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
            Glide.with(this).load(bitmap).into(iv_product_image)
        }
    }
}