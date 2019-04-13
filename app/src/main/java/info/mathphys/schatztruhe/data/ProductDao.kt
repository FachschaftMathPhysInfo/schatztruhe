package info.mathphys.schatztruhe.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * from product_table JOIN bietet_an_table ON product_id=product_table.id WHERE theke_id=:theke_id ORDER BY name ASC")
    fun getAllProducts( theke_id:Long): LiveData<List<Product> >

    @Insert
    fun insert(product: Product)

    @Query("DELETE FROM product_table")
    fun deleteAll()

    @Query("SELECT * from product_table WHERE id=:product_id ORDER BY name ASC LIMIT 1")
    fun getProdukt(product_id: Long): Product
}