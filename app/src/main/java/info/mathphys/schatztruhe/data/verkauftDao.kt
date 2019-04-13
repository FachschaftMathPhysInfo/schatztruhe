package info.mathphys.schatztruhe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface verkauftDao {


    @Insert
    fun insert(product: verkauft)

    @Query("DELETE FROM verkauft_table")
    fun deleteAll()
}