package info.mathphys.schatztruhe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.mathphys.schatztruhe.data.*

import kotlinx.android.synthetic.main.activity_buy.*
import kotlinx.android.synthetic.main.content_buy.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import android.hardware.usb.UsbDevice.getDeviceId
import android.content.Context.TELEPHONY_SERVICE
import android.provider.Settings
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager

import android.provider.Settings.System
import android.view.View
import android.widget.Button
import org.jetbrains.anko.find

class BuyActivity : AppCompatActivity() {
    private lateinit var mverkauftViewModel: verkauftViewModel
    var count:Long=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        setSupportActionBar(toolbar)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        supportActionBar?.displayOptions= ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setCustomView(R.layout.actionbar_buy)

        count= intent.getLongExtra("COUNT",1)
        val self = this
        doAsync {

            mverkauftViewModel = ViewModelProviders.of(
                self,
                verkauftViewModelFactory(application, intent.getLongExtra("THEKE_ID",0),intent.getLongExtra("PRODUCT_ID",0))
            )
                .get(verkauftViewModel::class.java)
            val text = mverkauftViewModel.theke.name+": "+count.toString()+"*"+mverkauftViewModel.product.name
            uiThread {
                supportActionBar?.customView?.findViewById<TextView>(R.id.action_bar_title)?.text=text
                buy.setOnClickListener { view ->
                    mverkauftViewModel.insert(verkauft(anzahl = count,verschenkt = false,
                        product_id =mverkauftViewModel.product_id,theke_id = mverkauftViewModel.theken_id,zeitpunkt = Date(),
                        tablet_imei=System.getString(self.getContentResolver(), Settings.Secure.ANDROID_ID)))
                    val intent = Intent(self, ProductActivity::class.java).apply {

                    }
                    intent.putExtra("THEKE_ID",mverkauftViewModel.theken_id)
                    startActivity(intent)
                }
                gift.setOnClickListener { view ->
                    mverkauftViewModel.insert(verkauft(anzahl = count,verschenkt = true,
                        product_id =mverkauftViewModel.product_id,theke_id = mverkauftViewModel.theken_id,zeitpunkt = Date(),
                        tablet_imei=System.getString(self.getContentResolver(), Settings.Secure.ANDROID_ID)))
                    val intent = Intent(self, ProductActivity::class.java).apply {

                    }
                    intent.putExtra("THEKE_ID",mverkauftViewModel.theken_id)
                    startActivity(intent)
                }
                supportActionBar?.customView?.find<Button>(R.id.action_bar_abort)?.setOnClickListener { view->
                    val intent = Intent(self, ProductActivity::class.java).apply {

                    }
                    intent.putExtra("THEKE_ID",mverkauftViewModel.theken_id)
                    startActivity(intent)
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

}
