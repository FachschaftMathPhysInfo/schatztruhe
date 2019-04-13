package info.mathphys.schatztruhe.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProductsViewModel(application: Application, val theken_id:Long) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: ProductRepository
    val allProducts: LiveData<List<Product>>
    val theke:Theke
    init {
        val productDao = SchatzTruhenDatabase.getDatabase(application).productDao()
        val thekeDao = SchatzTruhenDatabase.getDatabase(application).thekeDao()
        val bietet_anDao = SchatzTruhenDatabase.getDatabase(application).bietet_anDao()
        repository = ProductRepository(productDao,bietet_anDao,theken_id)
        //val theke_repository = ThekeRepository(thekeDao)
        allProducts = repository.allProducts
        theke = thekeDao.getTheke(theken_id)
    }

    fun insert(product: Product) = scope.launch(Dispatchers.IO) {
        repository.insert(product)
    }
    fun insert(bietet_an: bietet_an) = scope.launch(Dispatchers.IO) {
        repository.insert(bietet_an)
    }
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}