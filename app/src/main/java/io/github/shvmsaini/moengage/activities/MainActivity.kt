package io.github.shvmsaini.moengage.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.shvmsaini.moengage.R
import io.github.shvmsaini.moengage.adapters.ArticleItemAdapter
import io.github.shvmsaini.moengage.databinding.ActivityMainBinding
import io.github.shvmsaini.moengage.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort -> {
                    // TODO:
                    true
                }

                else -> {
                    false
                }
            }
        }
        viewModel = this.defaultViewModelProviderFactory.create(MainActivityViewModel::class.java)
//        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getMyList().observe(this, Observer {
            val articleItemAdapter = ArticleItemAdapter(this, it!!, viewModel)
            binding.rvArticlesView.layoutManager = LinearLayoutManager(this)
            binding.rvArticlesView.adapter = articleItemAdapter
        })

        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}