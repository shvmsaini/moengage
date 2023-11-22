package io.github.shvmsaini.moengage.models

import android.graphics.Bitmap
import io.github.shvmsaini.moengage.adapters.ArticleItemAdapter

data class ArticleItem(
    var source: Source? = Source(),
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null,
    var content: String? = null,
    var bitmap: Bitmap? = null
) : Comparable<ArticleItem> {
    override fun compareTo(other: ArticleItem): Int {
        val thisDate = this.publishedAt?.let { ArticleItemAdapter.inputFormat.parse(it) }
        val otherDate = other.publishedAt?.let { ArticleItemAdapter.inputFormat.parse(it) }
        if (otherDate != null && thisDate != null)
            return thisDate!!.compareTo(otherDate!!)
        return 0
    }
}

data class Source(
    var id: String? = null,
    var name: String? = null
)