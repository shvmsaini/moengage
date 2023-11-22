package io.github.shvmsaini.moengage.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.shvmsaini.moengage.R
import io.github.shvmsaini.moengage.activities.DetailsActivity
import io.github.shvmsaini.moengage.models.ArticleItem
import io.github.shvmsaini.moengage.utils.MyBitmapUtils
import io.github.shvmsaini.moengage.viewmodels.MainActivityViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class ArticleItemAdapter(
    private val context: Context,
    var list: ArrayList<ArticleItem>,
    var viewModel: MainActivityViewModel,
) : RecyclerView.Adapter<ArticleItemAdapter.ItemViewHolder>() {
    companion object {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.description.text = item.description

        val date = item.publishedAt?.let { inputFormat.parse(it) }
        holder.publishTime.text = date?.let { outputFormat.format(it) } ?: ""

        holder.itemView.setOnClickListener {
            val i = Intent(context, DetailsActivity::class.java)
            i.putExtra("url", item.url)
            context.startActivity(i)
        }
        if (item.bitmap != null) {
            holder.image.setImageBitmap(item.bitmap)
        } else {
            Thread {
                item.urlToImage?.let {
                    MyBitmapUtils.downloadImageFromPath(item.urlToImage, holder.image)?.let {
                        item.bitmap = it
                    }
                }
            }.start()
        }
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
