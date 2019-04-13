package info.mathphys.schatztruhe

import java.io.File
import java.io.FileWriter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var mThekenViewModel: ThekenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        //recyclerView.addItemDecoration(GridItemDecoration(10,2))
        val self = this
        doAsync {
            mThekenViewModel = ViewModelProviders.of(self).get(ThekenViewModel::class.java)
            val adapter = ThekenListAdapter(self)
            uiThread {
                mThekenViewModel.allTheken.observe(self, Observer { words ->
                    // Update the cached copy of the words in the adapter.
                    words?.let { adapter.setTheken(it) }
                })
                recyclerView.adapter = adapter
            }
            adapter.onItemClick = { item ->

                // do something with your item
                Log.d("CLICKED", item.name)
                val intent = Intent(self, ProductActivity::class.java).apply {

                }
                intent.putExtra("THEKE_ID", item.id)
                startActivity(intent)
            }
            fab.setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                mThekenViewModel.insert(Theke("Shotbar"))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     *
     */

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

            R.id.action_export -> {

                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    val sell_data = mThekenViewModel.allverkauft

                    val now = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                    val datetimeStr = now.format(Date())

                    val CSV_HEADER = "anzahl,product_id,theke_id,verschenkt,zeitpunkt,tablet_imei,"

                    val verkauftFile = File(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS
                        ), datetimeStr + "_verkauft.csv"
                    )

                    //val verkauftFile = File.createTempFile("verkauft", "csv")
                    var fileWriter: FileWriter? = null

                    try {
                        fileWriter = FileWriter(verkauftFile)


                        fileWriter.append(CSV_HEADER)
                        fileWriter.append('\n')

                        for (item in sell_data!!) {
                            fileWriter.append(item.anzahl.toString())
                            fileWriter.append(',')
                            fileWriter.append(item.product_id.toString())
                            fileWriter.append(',')
                            fileWriter.append(item.theke_id.toString())
                            fileWriter.append(',')
                            fileWriter.append(item.verschenkt.toString())
                            fileWriter.append(',')
                            fileWriter.append(SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(item.zeitpunkt))
                            fileWriter.append(',')
                            fileWriter.append(item.tablet_imei.toString())
                            fileWriter.append(",\n")
                        }
                        fileWriter.close()
                        toast("Successfully saved data to file ;-)")

                    } catch (e: Exception) {
                        println("Writing CSV error!")
                        e.printStackTrace()
                        Log.e("E/WRITE-TO-FILE", e.stackTrace.toString())
                    }
                } else {
                    toast("No access to external storage")
                }


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
