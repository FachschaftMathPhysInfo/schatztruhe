package info.mathphys.schatztruhe.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ThekeRepository(private val thekeDao: ThekeDao) {

    val allTheken: LiveData<List<Theke>> = thekeDao.getAllTheken()

    @WorkerThread
    suspend fun insert(theke: Theke) {
        thekeDao.insert(theke)
    }
}