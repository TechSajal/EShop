package com.example.e_shop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_shop.Modul.Product
import com.example.e_shop.R
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

open  class MyDashBoardListAdapter(private val context: Context, private val list:ArrayList<Product>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_dashboard_layout,parent,false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model =list[position]
        Glide.with(context).load(model.image).centerCrop().into(holder.itemView.iv_dashboard_item_image)
        holder.itemView.tv_dashboard_item_title.text = model.title
        holder.itemView.tv_dashboard_item_price.text= "Rs ${model.price}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}