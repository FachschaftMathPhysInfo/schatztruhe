package info.mathphys.schatztruhe.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import info.mathphys.schatztruhe.R

class ProductsListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ProductsListAdapter.ProductViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var products = emptyList<Product>() // Cached copy of words
    var onItemClick: ((Product) -> Unit)? = null
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
        val wordItemBoxView: RelativeLayout = itemView.findViewById(R.id.textViewBox)
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(products[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_product_item, parent, false)
        return ProductViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = products[position]
        var is_red : Boolean = true

        holder.wordItemView.text = current.name
        if (products.size < 13){
            is_red = (position%2 == 0)
        } else {
            is_red = ((position+(position/4)%2)%2 == 0)
        }

        if (is_red){
            holder.wordItemView.setBackgroundColor(ContextCompat.getColor(holder.wordItemView.context, R.color.colorRED))
            holder.wordItemBoxView.setBackgroundColor(ContextCompat.getColor(holder.wordItemView.context, R.color.colorRED))
        }

    }

    internal fun setProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

    override fun getItemCount() = products.size
}