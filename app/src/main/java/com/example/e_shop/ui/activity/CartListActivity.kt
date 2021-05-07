package com.example.e_shop.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_shop.Modul.CartItem
import com.example.e_shop.R
import com.example.e_shop.ui.adapters.CartItemListAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
    }

    @SuppressLint("SetTextI18n")
    fun successCartItemList(cartlist:ArrayList<CartItem>){
        if (cartlist.size >0){
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager =LinearLayoutManager(this)
            rv_cart_items_list.setHasFixedSize(true)
            val cardlistadapter = CartItemListAdapter(this,cartlist)
            rv_cart_items_list.adapter = cardlistadapter
            var subTotal:Double = 0.0
            for (item in cartlist){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)

            }
            tv_sub_total.text = "Rs $subTotal"
            tv_shipping_charge.text =" Rs 60"
            if (subTotal >0){
                ll_checkout.visibility = View.VISIBLE
                val total = subTotal + 60
                tv_total_amount.text ="Rs $total"

            }else{
                ll_checkout.visibility = View.GONE
            }
        }
        else{
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE

        }

    }

    override fun onResume() {
        super.onResume()
        FirestoreClass().getCartList(this)
    }

}