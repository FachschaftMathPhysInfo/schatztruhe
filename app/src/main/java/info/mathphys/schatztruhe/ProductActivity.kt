package info.mathphys.schatztruhe

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import kotlinx.android.synthetic.main.activity_product.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.mathphys.schatztruhe.data.*
import kotlinx.android.synthetic.main.content_product.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ProductActivity : AppCompatActivity() {

    private lateinit var mProductsViewModel : ProductsViewModel
    var count: Long=1
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product)
        setSupportActionBar(toolbar)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

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
                recyclerView.layoutManager = GridLayoutManager(self, 3)
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
                    intent.putExtra("COUNT",count)
                    startActivity(intent)
                }
            }
        }
        resetColors()
        one.setBackgroundColor(ContextCompat.getColor(self,R.color.colorSelect))
        one.setOnClickListener { view->
            resetColors()
            view.setBackgroundColor(ContextCompat.getColor(self,R.color.colorSelect))
            self.count =1
        }
        two.setOnClickListener { view->
            resetColors()
            view.setBackgroundColor(ContextCompat.getColor(self,R.color.colorSelect))
            self.count =2
        }
        three.setOnClickListener { view->
            resetColors()
            view.setBackgroundColor(ContextCompat.getColor(self,R.color.colorSelect))
            self.count =3
        }
        four.setOnClickListener { view->
            resetColors()
            view.setBackgroundColor(ContextCompat.getColor(self,R.color.colorSelect))
            self.count =4
        }
        five.setOnClickListener { view->
            resetColors()
            view.setBackgroundColor(ContextCompat.getColor(self,R.color.colorSelect))
            self.count =5
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun resetColors() {
        one.setBackgroundColor(ContextCompat.getColor(this,R.color.button_material_light))
        two.setBackgroundColor(ContextCompat.getColor(this,R.color.button_material_light))
        three.setBackgroundColor(ContextCompat.getColor(this,R.color.button_material_light))
        four.setBackgroundColor(ContextCompat.getColor(this,R.color.button_material_light))
        five.setBackgroundColor(ContextCompat.getColor(this,R.color.button_material_light))
    }

}
