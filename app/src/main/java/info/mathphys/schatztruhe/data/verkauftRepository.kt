package info.mathphys.schatztruhe.data

import androidx.annotation.WorkerThread

class verkauftRepository(private val verkauftDao: verkauftDao) {


    @WorkerThread
    suspend fun insert(verkauft: verkauft) {
        verkauftDao.insert(verkauft)
    }
}