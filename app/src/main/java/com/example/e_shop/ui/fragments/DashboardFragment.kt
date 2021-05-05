package com.example.e_shop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_shop.Modul.Product
import com.example.e_shop.R
import com.example.e_shop.ui.activity.FirestoreClass
import com.example.e_shop.ui.activity.SettingsActivity
import com.example.e_shop.ui.adapters.MyDashBoardListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // if we want to use option menu in fragment we need to add it
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    fun successDashboardListFromFirestore(productList:ArrayList<Product>){
        if (productList.size > 0) {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_found.visibility = View.GONE
            rv_dashboard_items.layoutManager = GridLayoutManager(activity,2)
            rv_dashboard_items.setHasFixedSize(true)
            val adapterDashboardProducts = MyDashBoardListAdapter(requireActivity(),productList)
            rv_dashboard_items.adapter = adapterDashboardProducts
        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_found.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        FirestoreClass().getdashboardlist(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings ->{
                 val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}