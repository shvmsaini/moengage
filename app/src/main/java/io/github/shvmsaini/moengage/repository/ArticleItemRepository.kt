package io.github.shvmsaini.moengage.repository

import androidx.lifecycle.MutableLiveData
import io.github.shvmsaini.moengage.models.ArticleItem
import io.github.shvmsaini.moengage.services.ArticleItemFetchingService

class ArticleItemRepository {
    private var articleItemList: MutableLiveData<ArrayList<ArticleItem>?>? = null

    init {
        articleItems
    }
    val articleItems : MutableLiveData<ArrayList<ArticleItem>?>
        get() {
            if (articleItemList == null) {
                articleItemList = MutableLiveData()
                ArticleItemFetchingService().list.observeForever {
                    articleItemList!!.postValue(it)
                }
            }

            return articleItemList!!
        }

    /**
     * Clears Articles
     */
    fun clearItems() {
        articleItemList = null
    }
}