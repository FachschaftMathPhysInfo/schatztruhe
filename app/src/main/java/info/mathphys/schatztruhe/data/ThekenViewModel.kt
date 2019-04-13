package info.mathphys.schatztruhe.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ThekenViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: ThekeRepository
    private val verkauft_repository: verkauftRepository
    val allTheken: LiveData<List<Theke>>
    val allverkauft: List<verkauft>
    init {
        val thekenDao = SchatzTruhenDatabase.getDatabase(application).thekeDao()
        val verkauftDao = SchatzTruhenDatabase.getDatabase(application).verkauftDao()
        repository = ThekeRepository(thekenDao)
        verkauft_repository= verkauftRepository(verkauftDao)
        allTheken = repository.allTheken
        allverkauft= verkauft_repository.allverkauft
    }

    fun insert(theke: Theke) = scope.launch(Dispatchers.IO) {
        repository.insert(theke)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}