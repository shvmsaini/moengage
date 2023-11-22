package io.github.shvmsaini.moengage.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import io.github.shvmsaini.moengage.databinding.ActivityDetailsBinding
import io.github.shvmsaini.moengage.databinding.ActivityMainBinding

class DetailsActivity : AppCompatActivity(){
    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.extras!!.getString("url") ?: return
        val webView = binding.wvArticle
        webView.webViewClient = WebViewClient()
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.wvArticle.loadUrl(url)

        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}