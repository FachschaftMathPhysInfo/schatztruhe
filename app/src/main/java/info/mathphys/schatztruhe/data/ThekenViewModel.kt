package info.mathphys.schatztruhe.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import kotlin.coroutines.CoroutineContext

class ThekenViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: ThekeRepository
    private val product_repository: ProductRepository
    private val bietet_anDao:bietet_anDao
    private val verkauft_repository: verkauftRepository
    val allTheken: LiveData<List<Theke>>
    val anyProducts: List<Product>
    val allverkauft: List<verkauft>
    init {
        val thekenDao = SchatzTruhenDatabase.getDatabase(application).thekeDao()
        val verkauftDao = SchatzTruhenDatabase.getDatabase(application).verkauftDao()
        val productDao = SchatzTruhenDatabase.getDatabase(application).productDao()
        bietet_anDao = SchatzTruhenDatabase.getDatabase(application).bietet_anDao()
        repository = ThekeRepository(thekenDao)
        verkauft_repository= verkauftRepository(verkauftDao)
        product_repository = ProductRepository(productDao,bietet_anDao,-1)
        allTheken = repository.allTheken
        anyProducts = productDao.getAnyProducts()
        allverkauft= verkauft_repository.allverkauft
    }

    fun insert(theke: Theke) = scope.launch(Dispatchers.IO) {
        repository.insert(theke)
    }
    fun insert(product: Product) = scope.launch(Dispatchers.IO) {
        product_repository.insert(product)
    }
    fun insert(bietet_an: bietet_an){
        doAsync{

            bietet_anDao.insert(bietet_an)
        }
    }
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}