package com.exchangerates

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.exchangerates.databinding.MainActivityBinding
import com.exchangerates.model.Exchange
import com.exchangerates.service.AppServices
import com.exchangerates.viewmodel.MainViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var menuSettingsItem: MenuItem
    lateinit var menuApplyItem: MenuItem

    var isSuccessfulLoadingList: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        loadExchangeList("2020-11-7")
    }

    private fun loadExchangeList(date: String) {

        AppServices
            .exchangesService
            .getAllExchanges(date)
            .enqueue(object: Callback<List<Exchange>> {
                override fun onResponse(
                    call: Call<List<Exchange>>,
                    response: Response<List<Exchange>>
                ) {
                    val bodyData = response.body()
                    Timber.d("GET_URL: ${call.request().url}")
                    if (response.code() == 200) {
                        if (bodyData != null) {
                            binding.navHostFragment.visibility = View.VISIBLE
                            binding.errorMessageTextView.visibility = View.GONE
                            binding.firstDateTextView.visibility = View.VISIBLE
                            binding.secondDateTextView.visibility = View.VISIBLE
                            isSuccessfulLoadingList = true
                            menuSettingsItem.isVisible = true

                            mainViewModel.exchangeList.value = bodyData
                        }
                    }
                }
                override fun onFailure(call: Call<List<Exchange>>, t: Throwable) {
                    binding.navHostFragment.visibility = View.GONE
                    binding.errorMessageTextView.visibility = View.VISIBLE
                    binding.firstDateTextView.visibility = View.GONE
                    binding.secondDateTextView.visibility = View.GONE
                    isSuccessfulLoadingList = false
                    Timber.d("GET_URL: ${call.request().url}")
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menuSettingsItem = menu!!.findItem(R.id.action_settings)
        menuApplyItem = menu.findItem(R.id.action_apply)
        if (isSuccessfulLoadingList) {
            menuSettingsItem.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_settings -> {
                binding.firstDateTextView.visibility = View.GONE
                binding.secondDateTextView.visibility = View.GONE
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.firstDateTextView.visibility = View.VISIBLE
        binding.secondDateTextView.visibility = View.VISIBLE
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}