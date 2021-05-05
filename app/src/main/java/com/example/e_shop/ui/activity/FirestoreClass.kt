package com.example.e_shop.ui.activity

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.e_shop.Modul.Product
import com.example.e_shop.Modul.User
import com.example.e_shop.ui.fragments.DashboardFragment
import com.example.e_shop.ui.fragments.ProductsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private  val mfirestore = FirebaseFirestore.getInstance()
    val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
    fun registeruser(activity: RegisterActivity,userInfo: User){
        mfirestore.collection("users").document(userInfo.id).set(userInfo, SetOptions.merge())
    }
//    fun profileuser (activity: Activity, userInfo: User){
//        mfirestore.collection("users").document(currentuserid).set(userInfo).addOnSuccessListener {
//
//        }
//    }
    fun updateUserProfile(activity: Activity,userHashMap: HashMap<String, Any>){
        mfirestore.collection("users").document(currentuserid).update(userHashMap).addOnSuccessListener {

        }.addOnFailureListener { e ->
            Log.e(activity.javaClass.simpleName,"error",e)
        }

    }

    fun registerproduct(activity: AddProductActivity, userInfo: Product){
        mfirestore.collection("Products").document().set(userInfo, SetOptions.merge())
    }

//    fun getProductsList(fragment: Fragment) {
//
//        mfirestore.collection("products")
//                .whereEqualTo("user_id", currentuserid)
//                .get()
//                .addOnSuccessListener { document ->
//                    Log.e("Products List", document.documents.toString())
//                    val productsList: ArrayList<Product> = ArrayList()
//                    for (i in document.documents) {
//                        val product = i.toObject(Product::class.java)
//                        product!!.id_user = i.id
//
//                        productsList.add(product)
//                    }
//
//                    when (fragment) {
//                        is ProductsFragment -> {
//                            fragment.successProductsListFromFireStore(productsList)
//                        }
//                        else -> (null)
//                    }
//                }
//                .addOnFailureListener { e ->
//                    // Hide the progress dialog if there is any error based on the base class instance.
//                    when (fragment) {
//                        is ProductsFragment -> {
//                        }
//                    }
//                    Log.e("Get Product List", "Error while getting product list.", e)
//                }
//    }

      fun getProductsList(fragment: Fragment){
          mfirestore.collection("Products").whereEqualTo("user_id",currentuserid)
                  .get()
                  .addOnSuccessListener { document ->
                      Log.e("Products List", document.documents.toString())

                      // Here we have created a new instance for Products ArrayList.
                      val productsList: ArrayList<Product> = ArrayList()

                      // A for loop as per the list of documents to convert them into Products ArrayList.
                      for (i in document.documents) {

                          val product = i.toObject(Product::class.java)
                          product!!.id = i.id
                          productsList.add(product)
                      }
                      when (fragment) {
                          is ProductsFragment -> {
                              fragment.successProductListFromFirestore(productsList)
                          }
                      }
                  }
      }

    fun getdashboardlist(fragment: DashboardFragment){
        mfirestore.collection("Products")
                .get()
                .addOnSuccessListener { document ->
                    val DASHBOARDlists:ArrayList<Product> =ArrayList()
                    for (i in document.documents){
                        val product = i.toObject(Product::class.java)
                        product!!.id = i.id
                          DASHBOARDlists.add(product)
                    }
                      fragment.successDashboardListFromFirestore(DASHBOARDlists)
                }
    }


}