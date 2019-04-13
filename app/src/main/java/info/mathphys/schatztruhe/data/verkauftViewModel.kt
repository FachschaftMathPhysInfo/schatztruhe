package info.mathphys.schatztruhe.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class verkauftViewModel(application: Application, val theken_id:Long, val product_id:Long) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: verkauftRepository
    val theke:Theke
    val product : Product
    init {
        val productDao = SchatzTruhenDatabase.getDatabase(application).productDao()
        val thekeDao = SchatzTruhenDatabase.getDatabase(application).thekeDao()
        val verkauftDao = SchatzTruhenDatabase.getDatabase(application).verkauftDao()
        repository = verkauftRepository(verkauftDao)
        //val theke_repository = ThekeRepository(thekeDao)
        theke = thekeDao.getTheke(theken_id)
        product = productDao.getProdukt(product_id)
    }

    fun insert(verkauft: verkauft) = scope.launch(Dispatchers.IO) {
        repository.insert(verkauft)
    }
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}