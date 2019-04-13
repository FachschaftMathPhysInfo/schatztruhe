package info.mathphys.schatztruhe.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import info.mathphys.schatztruhe.R

class ThekenListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ThekenListAdapter.ThekeViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var theken = emptyList<Theke>() // Cached copy of words
    var onItemClick: ((Theke) -> Unit)? = null
    inner class ThekeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(theken[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThekeViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_theke_item, parent, false)
        return ThekeViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ThekeViewHolder, position: Int) {
        val current = theken[position]
        holder.wordItemView.text = current.name
    }

    internal fun setTheken(theken: List<Theke>) {
        this.theken = theken
        notifyDataSetChanged()
    }

    override fun getItemCount() = theken.size
}