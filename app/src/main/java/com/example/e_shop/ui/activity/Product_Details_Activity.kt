package com.example.e_shop.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_shop.Modul.CartItem
import com.example.e_shop.Modul.Product
import com.example.e_shop.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_product__details_.*

class Product_Details_Activity : AppCompatActivity() {
    private var mproductid:String = ""
     var productownerID:String = ""
    val DEFAULT_CART_QUANTITY:String = "1"
    val CART_ITEMS = "cart_items"

    private lateinit var mproductDetails:Product
    val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product__details_)

           if(intent.hasExtra("extra_product_id")){
              mproductid = intent.getStringExtra("extra_product_id")!!
              FirestoreClass().getproductdetails(this,mproductid)
          }
          if(intent.hasExtra("extra_product_owner_id")){
              productownerID = intent.getStringExtra("extra_product_owner_id")!!
          }

          if (currentuserid == productownerID){
              add_to_cart.visibility = View.GONE
              go_to_cart.visibility = View.GONE
          }else{
              add_to_cart.visibility = View.VISIBLE
          }

          add_to_cart.setOnClickListener {
              val cartItem = CartItem(
                  currentuserid,
                  mproductid,
                  mproductDetails.title,
                  mproductDetails.price,
                  mproductDetails.image,
                  DEFAULT_CART_QUANTITY
              )
              FirestoreClass().addCartitems(this,cartItem)
          }

           go_to_cart.setOnClickListener {
               val intent = Intent(this,CartListActivity::class.java)
               startActivity(intent)
           }

    }



    fun addtoCartSuccess(){
        Toast.makeText(this,"Product added to cart",Toast.LENGTH_LONG).show()
        add_to_cart.visibility =View.GONE
        go_to_cart.visibility =View.VISIBLE

    }
    fun productDetailSuccess(product:Product){
        mproductDetails = product
        Glide.with(this).load(product.image).into(iv_product_detail_image)
        tv_product_details_available_quantity.text =product.quantity
        tv_product_details_price.text = "Rs${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_title.text =product.title

        if (currentuserid == product.user_id){

        }else{
            FirestoreClass().ifItemExistInCart(this,mproductid)
        }
    }

    fun productexistincart() {
        add_to_cart.visibility = View.GONE
        go_to_cart.visibility = View.VISIBLE
    }

//    override fun onResume() {
//        super.onResume()
//        FirestoreClass().getCartList(this)
//    }
}