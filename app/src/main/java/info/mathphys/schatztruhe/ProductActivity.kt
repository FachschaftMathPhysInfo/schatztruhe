package info.mathphys.schatztruhe

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer

import kotlinx.android.synthetic.main.activity_product.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.mathphys.schatztruhe.data.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ProductActivity : AppCompatActivity() {

    private lateinit var mProductsViewModel : ProductsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions= ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setCustomView(R.layout.actionbar_theke)
        val self = this
        doAsync {

            mProductsViewModel = ViewModelProviders.of(
                self,
                ProductsViewModelFactory(application, intent.getLongExtra("THEKE_ID",0))
            )
                .get(ProductsViewModel::class.java)
            val text = mProductsViewModel.theke.name
            uiThread {
                supportActionBar?.customView?.findViewById<TextView>(R.id.action_bar_title)?.text=text
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                recyclerView.layoutManager = GridLayoutManager(self, 2)
                val adapter = ProductsListAdapter(self)
                mProductsViewModel.allProducts.observe(self, Observer { words ->
                    // Update the cached copy of the words in the adapter.
                    words?.let { adapter.setProducts(it) }
                })
                recyclerView.adapter = adapter
                adapter.onItemClick = { item ->

                    // do something with your item
                    Log.d("CLICKED", item.name)
                    val intent = Intent(self, BuyActivity::class.java).apply {

                    }
                    intent.putExtra("THEKE_ID",mProductsViewModel.theken_id)
                    intent.putExtra("PRODUCT_ID",item.id)
                    startActivity(intent)
                }
            }
        }
        fab.setOnClickListener { view ->
            var product = Product("Bier")
            Log.d("ID",product.id.toString())
            mProductsViewModel.insert(product)
            mProductsViewModel.insert(bietet_an(product_id = 1,theke_id = mProductsViewModel.theken_id))
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
