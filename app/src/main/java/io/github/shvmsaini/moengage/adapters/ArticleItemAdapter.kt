package io.github.shvmsaini.moengage.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.shvmsaini.moengage.R
import io.github.shvmsaini.moengage.models.ArticleItem
import io.github.shvmsaini.moengage.viewmodels.MainActivityViewModel
import org.w3c.dom.Text

class ArticleItemAdapter(
    private val context : Context,
    var list : ArrayList<ArticleItem>,
    var viewModel: MainActivityViewModel,
) : RecyclerView.Adapter<ArticleItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.publishTime.text = item.publishedAt
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var publishTime: TextView
        var title: TextView
        var description: TextView
        var image: ImageView

        init {
            publishTime = itemView.findViewById(R.id.tvPublishedTime)
            title = itemView.findViewById(R.id.tvTitle)
            description = itemView.findViewById(R.id.tvDescription)
            image = itemView.findViewById(R.id.ivThumbnail)
        }
    }
}