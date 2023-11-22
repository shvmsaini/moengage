package io.github.shvmsaini.moengage.services

import androidx.lifecycle.MutableLiveData
import io.github.shvmsaini.moengage.models.ArticleItem
import io.github.shvmsaini.moengage.models.Source
import io.github.shvmsaini.moengage.utils.MyHttpHandler
import org.json.JSONObject

class ArticleItemFetchingService {
    private val myHttpHandler = MyHttpHandler()
    val list : MutableLiveData<ArrayList<ArticleItem>?>
        get() {
            val itemList = MutableLiveData<ArrayList<ArticleItem>?>()

            Thread {
                val tempList = ArrayList<ArticleItem>()
                val response = myHttpHandler.makeServiceCall(BASE_URL)?.let { JSONObject(it) }
                if (response?.has("articles") == true) {
                    val arr = response.getJSONArray("articles")
                    for (i in 0 until arr.length()) {
                        val jsonObject = arr.getJSONObject(i)
                        val articleItem = ArticleItem()
                        articleItem.author = jsonObject.getString("author")
                        articleItem.title = jsonObject.getString("title")
                        articleItem.description = jsonObject.getString("description")
                        articleItem.url = jsonObject.getString("url")
                        articleItem.urlToImage = jsonObject.getString("urlToImage")
                        val source = Source()
                        source.id = jsonObject.getJSONObject("source").getString("id")
                        source.name = jsonObject.getJSONObject("source").getString("name")
                        articleItem.source = source
                        articleItem.publishedAt = jsonObject.getString("publishedAt")
                        tempList.add(articleItem)
                    }
                    itemList.postValue(tempList)
                }
            }.start()

            return itemList
        }


    companion object {
        private val TAG = ArticleItemFetchingService::class.java.simpleName
        private const val BASE_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
    }
}