package io.github.shvmsaini.moengage.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.messaging.FirebaseMessaging
import io.github.shvmsaini.moengage.R
import io.github.shvmsaini.moengage.adapters.ArticleItemAdapter
import io.github.shvmsaini.moengage.databinding.ActivityMainBinding
import io.github.shvmsaini.moengage.models.ArticleItem
import io.github.shvmsaini.moengage.viewmodels.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel

    fun showAlertDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val uri = intent.data
        if (uri != null) {
            // Notification Entry
            showAlertDialog(this, "Got this data payload", intent.extras.toString())
        }

        setContentView(binding.root)

        // Permissions in Android 13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission: String = Manifest.permission.POST_NOTIFICATIONS
            if (ActivityCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // has permission
            } else if (shouldShowRequestPermissionRationale(permission)) {
                // Don't do anything
            } else {
                requestNotificationPermission.launch(permission)
            }
        }

        // getToken
        getFCMToken()

        viewModel = this.defaultViewModelProviderFactory.create(MainActivityViewModel::class.java)
        var articleItemAdapter : ArticleItemAdapter?= null
        viewModel.getMyList().observe(this) {
            articleItemAdapter = ArticleItemAdapter(this, it!!, viewModel)
            binding.rvArticlesView.layoutManager = LinearLayoutManager(this)
            binding.rvArticlesView.adapter = articleItemAdapter
        }

        val unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.baseline_sort_24)
        binding.topAppBar.overflowIcon = unwrappedDrawable

        ContextCompat.getDrawable(this, R.drawable.baseline_sort_24)
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sortAscending -> {
                    viewModel.getMyList().observe(this, Observer {
                        it?.let {
                            binding.rvArticlesView.visibility = View.GONE
                            it.sort()
                            binding.rvArticlesView.invalidate()
                            articleItemAdapter?.list = it
                            articleItemAdapter?.notifyDataSetChanged()
                            binding.rvArticlesView.visibility = View.VISIBLE
                        }
                    })
                    true
                }

                R.id.sortDescending -> {
                    viewModel.getMyList().observe(this, Observer {
                        it?.let {
                            binding.rvArticlesView.visibility = View.GONE
                            it.sortDescending()
                            articleItemAdapter?.list = it
                            articleItemAdapter?.notifyDataSetChanged()
                            binding.rvArticlesView.visibility = View.VISIBLE
                        }
                    })
                    true
                }

                else -> false
            }
        }

        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private val requestNotificationPermission = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, perform your action here
        } else {
        }
    }

    fun getFCMToken(): String? {
        var token: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                try {
                    token = task.result
                    Log.d(TAG, "getFCMToken: $token")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return token
    }

}