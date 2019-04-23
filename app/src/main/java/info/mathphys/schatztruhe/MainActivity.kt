package info.mathphys.schatztruhe

import android.Manifest
import android.app.AlertDialog
import java.io.File
import java.io.FileWriter

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.GridLayoutManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*
import info.mathphys.schatztruhe.data.*
import java.io.*


class MainActivity : AppCompatActivity() {
    private lateinit var mThekenViewModel: ThekenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        }
        setupPermissions()
    }

    private val READ_EXTERNAL_REQUEST_CODE = 101
    private val WRITE_EXTERNAL_REQUEST_CODE = 102
    private val READ_PHONE_REQUEST_CODE = 103

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_REQUEST_CODE)
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_REQUEST_CODE)
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            READ_PHONE_REQUEST_CODE)
    }

    private fun setupPermissions() {
        val permissions = listOf(
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE),
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        )

        for (permission in permissions) {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Permission to access external storage required")
                        .setTitle("Permission required")

                    builder.setPositiveButton(
                        "OK"
                    ) { dialog, id ->
                        makeRequest()
                    }

                    val dialog = builder.create()
                    dialog.show()
                } else {
                    makeRequest()
                }
            }
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

            R.id.action_export -> {

                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    val sell_data = mThekenViewModel.allverkauft

                    val now = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                    val datetimeStr = now.format(Date())

                    val CSV_HEADER = "id,anzahl,product_id,theke_id,thekenName,produktName,verschenkt,zeitpunkt,tablet_imei,"

                    val verkauftFile = File(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS
                        ), datetimeStr + "_verkauft.csv"
                    )

                    var fileWriter: FileWriter? = null

                    try {
                        fileWriter = FileWriter(verkauftFile)


                        fileWriter.append(CSV_HEADER)
                        fileWriter.append('\n')

                        for (item in sell_data!!) {
                            fileWriter.append(item.ID.toString())
                            fileWriter.append(',')
                            fileWriter.append(item.anzahl.toString())
                            fileWriter.append(',')
                            fileWriter.append(item.product_id.toString())
                            fileWriter.append(',')
                            fileWriter.append(item.theke_id.toString())
                            fileWriter.append(',')
                            fileWriter.append(mThekenViewModel.allTheken.value?.find { it-> it.id!!.equals(item.theke_id) }?.name)
                            fileWriter.append(',')
                            fileWriter.append(mThekenViewModel.anyProducts.find { it-> it.id!!.equals(item.product_id) }?.name)
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

                var fileInputStream: InputStream? = null
                fileInputStream =  contentResolver.openInputStream(selectedFile)
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

                var fileInputStream: InputStream? = null
                fileInputStream =  contentResolver.openInputStream(selectedFile)
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

                var fileInputStream: InputStream? = null
                fileInputStream =  contentResolver.openInputStream(selectedFile)
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
