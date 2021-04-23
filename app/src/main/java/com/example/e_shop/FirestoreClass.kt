package com.example.e_shop

import com.example.e_shop.Modul.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private  val mfirestore = FirebaseFirestore.getInstance()
    fun registeruser(activity: RegisterActivity,userInfo: User){
        mfirestore.collection("users").document(userInfo.id).set(userInfo, SetOptions.merge())
    }

}