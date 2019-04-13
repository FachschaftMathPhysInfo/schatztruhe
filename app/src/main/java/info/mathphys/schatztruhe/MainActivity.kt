package info.mathphys.schatztruhe

import android.content.Intent
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

import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.GridLayoutManager
import info.mathphys.schatztruhe.data.*
import java.io.*


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
            R.id.action_import_theken -> {

                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

                startActivityForResult(Intent.createChooser(intent, "Theken Import file"), 111)
                true
            }
            R.id.action_import_products -> {

                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

                startActivityForResult(Intent.createChooser(intent, " Import product file"), 112)
                true
            }
            R.id.action_import_bietet_an -> {

                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

                startActivityForResult(Intent.createChooser(intent, " Import bietet file"), 113)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            //theken
            val selectedFile = data?.data //The uri with the location of the file
            Log.d("DATA",selectedFile.toString())
            var fileReader: BufferedReader? = null

            try {
                var line: String?

                var fileInputStream: FileInputStream? = null
                fileInputStream =  FileInputStream ( File(selectedFile?.path));
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val fileReader: BufferedReader = BufferedReader(inputStreamReader)
                // Read CSV header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size > 0) {
                        val theke = Theke(tokens[1],tokens[0].toLong())
                        mThekenViewModel.insert(theke)
                    }

                    line = fileReader.readLine()
                }
                fileReader.close()
            } catch (e: Exception) {
                println("Reading CSV Error!")
                e.printStackTrace()
            } finally {
                try {

                } catch (e: IOException) {
                    println("Closing fileReader Error!")
                    e.printStackTrace()
                }
            }
        }
        if (requestCode == 112 && resultCode == RESULT_OK) {
            //theken
            val selectedFile = data?.data //The uri with the location of the file
            Log.d("DATA",selectedFile.toString())
            var fileReader: BufferedReader? = null

            try {
                var line: String?

                var fileInputStream: FileInputStream? = null
                fileInputStream =  FileInputStream ( File(selectedFile?.path));
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val fileReader: BufferedReader = BufferedReader(inputStreamReader)
                // Read CSV header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size > 0) {
                        val product = Product(tokens[1],tokens[0].toLong())
                        mThekenViewModel.insert(product)
                    }

                    line = fileReader.readLine()
                }
                fileReader.close()
            } catch (e: Exception) {
                println("Reading CSV Error!")
                e.printStackTrace()
            } finally {
                try {

                } catch (e: IOException) {
                    println("Closing fileReader Error!")
                    e.printStackTrace()
                }
            }
        }
        if (requestCode == 113 && resultCode == RESULT_OK) {
            //bietet_an
            val selectedFile = data?.data //The uri with the location of the file
            Log.d("DATA",selectedFile.toString())
            var fileReader: BufferedReader? = null

            try {
                var line: String?

                var fileInputStream: FileInputStream? = null
                fileInputStream =  FileInputStream ( File(selectedFile?.path));
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val fileReader: BufferedReader = BufferedReader(inputStreamReader)
                // Read CSV header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size > 0) {
                        val bietet_an = bietet_an(theke_id = tokens[0].toLong(),product_id = tokens[1].toLong())
                        mThekenViewModel.insert(bietet_an)
                    }

                    line = fileReader.readLine()
                }
                fileReader.close()
            } catch (e: Exception) {
                println("Reading CSV Error!")
                e.printStackTrace()
            } finally {
                try {

                } catch (e: IOException) {
                    println("Closing fileReader Error!")
                    e.printStackTrace()
                }
            }
        }

    }
}
