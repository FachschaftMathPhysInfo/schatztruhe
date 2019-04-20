package info.mathphys.schatztruhe.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ProductRepository(private val productDao: ProductDao,private val bietet_anDao: bietet_anDao,private val theke_id:Long) {

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts(theke_id)


    @WorkerThread
    suspend fun insert(product: Product) {
        productDao.insert(product)
    }
    @WorkerThread
    suspend fun insert(bietet_an: bietet_an){
        bietet_anDao.insert(bietet_an)
    }
}
