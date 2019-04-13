package info.mathphys.schatztruhe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.mathphys.schatztruhe.data.Theke
import info.mathphys.schatztruhe.data.ThekenListAdapter
import info.mathphys.schatztruhe.data.ThekenViewModel

import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.GridLayoutManager



class MainActivity : AppCompatActivity() {
    private lateinit var mThekenViewModel: ThekenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        //recyclerView.addItemDecoration(GridItemDecoration(10,2))
        mThekenViewModel = ViewModelProviders.of(this).get(ThekenViewModel::class.java)
        val adapter = ThekenListAdapter(this)
        mThekenViewModel.allTheken.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setTheken(it) }
        })
        recyclerView.adapter = adapter
        adapter.onItemClick = { item ->

            // do something with your item
            Log.d("CLICKED", item.name)
            val intent = Intent(this, ProductActivity::class.java).apply {

            }
            intent.putExtra("THEKE_ID",item.id)
            startActivity(intent)
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            mThekenViewModel.insert(Theke("Shotbar"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java).apply {

                }
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
