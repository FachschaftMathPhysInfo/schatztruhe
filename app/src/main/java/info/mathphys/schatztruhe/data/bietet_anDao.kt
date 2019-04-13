package info.mathphys.schatztruhe.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface bietet_anDao {


    @Insert
    fun insert(product: bietet_an)

    @Query("DELETE FROM bietet_an_table")
    fun deleteAll()
}