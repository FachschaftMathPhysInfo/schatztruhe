package info.mathphys.schatztruhe.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.NonNull


 class verkauftViewModelFactory(private val application: Application, private val theken_id: Long,private val product_id:Long) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return verkauftViewModel(application, theken_id,product_id) as T

    }
}