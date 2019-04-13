package info.mathphys.schatztruhe.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface verkauftDao {

    @Query("SELECT * from verkauft_table  ORDER BY id asc")
    fun getAllverkauft(): LiveData<List<verkauft>>

    @Insert
    fun insert(product: verkauft)

    @Query("DELETE FROM verkauft_table")
    fun deleteAll()
}