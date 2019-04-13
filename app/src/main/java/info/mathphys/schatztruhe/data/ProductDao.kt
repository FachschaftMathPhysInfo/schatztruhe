package info.mathphys.schatztruhe.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * from product_table  ORDER BY name ASC")
    fun getAllProducts(): LiveData<List<Product> >

    @Insert
    fun insert(product: Product)

    @Query("DELETE FROM product_table")
    fun deleteAll()
}