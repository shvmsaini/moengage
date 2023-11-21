package io.github.shvmsaini.moengage.models

data class ArticleItem(
    var source: Source? = Source(),
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null,
    var content: String? = null
)

data class Source(
    var id: String? = null,
    var name: String? = null
)