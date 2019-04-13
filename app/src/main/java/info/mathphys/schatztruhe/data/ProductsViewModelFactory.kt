package info.mathphys.schatztruhe.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.NonNull


 class ProductsViewModelFactory(private val application: Application, private val id: Long) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductsViewModel(application, id) as T

    }
}