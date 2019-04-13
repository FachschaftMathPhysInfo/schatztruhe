package info.mathphys.schatztruhe.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class verkauftRepository(private val verkauftDao: verkauftDao) {

    val allverkauft: List<verkauft> = verkauftDao.getAllverkauft()
    @WorkerThread
    suspend fun insert(verkauft: verkauft) {
        verkauftDao.insert(verkauft)
    }
}