package io.github.shvmsaini.moengage.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.github.shvmsaini.moengage.models.ArticleItem
import io.github.shvmsaini.moengage.repository.ArticleItemRepository

class MainActivityViewModel : ViewModel() {
    private var observer: Observer<ArrayList<ArticleItem>?>? = null
    var articleItemRepository: ArticleItemRepository = ArticleItemRepository()
    var list = MutableLiveData<ArrayList<ArticleItem>?>()

    fun getMyList() : MutableLiveData<ArrayList<ArticleItem>?> {
        val data = MutableLiveData<ArrayList<ArticleItem>?>()
        observer = Observer {
            data.postValue(it)
        }
        articleItemRepository.articleItems.observeForever(observer!!)
        return data
    }
}