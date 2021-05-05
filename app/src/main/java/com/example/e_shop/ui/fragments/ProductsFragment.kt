package com.example.e_shop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_shop.Modul.Product
import com.example.e_shop.R
import com.example.e_shop.ui.activity.AddProductActivity
import com.example.e_shop.ui.activity.FirestoreClass
import com.example.e_shop.ui.adapters.MyProductListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : Fragment() {

  //  private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      // if we want to use option menu in fragment we need to add it
      setHasOptionsMenu(true)
        }


     fun successProductListFromFirestore(productList:ArrayList<Product>){
         if (productList.size > 0) {
             rv_my_product_items.visibility = View.VISIBLE
             tv_no_products_found.visibility = View.GONE
             rv_my_product_items.layoutManager = LinearLayoutManager(activity)
             rv_my_product_items.setHasFixedSize(true)
             val adapterProducts = MyProductListAdapter(requireActivity(), productList)
             rv_my_product_items.adapter = adapterProducts
         } else {
             rv_my_product_items.visibility = View.GONE
             tv_no_products_found.visibility = View.VISIBLE
         }
     }

    override fun onResume() {
        super.onResume()
        FirestoreClass().getProductsList(this)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_products, container, false)


        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_product ->{
                val intent = Intent(activity, AddProductActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}