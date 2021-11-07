package com.exchangerates

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.exchangerates.databinding.MainActivityBinding
import com.exchangerates.model.Exchange
import com.exchangerates.service.AppServices
import com.exchangerates.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var menuSettingsItem: MenuItem
    lateinit var menuApplyItem: MenuItem

    var isSuccessfulLoadingList: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
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

        val sdf = SimpleDateFormat("yyyy-M-dd", Locale.ROOT)
        val currentDate = sdf.format(Date())
        loadExchangeList(currentDate)

        val sdfForDateText = SimpleDateFormat("dd.M.yy", Locale.ROOT)
        val currentDateForText = sdfForDateText.format(Date())
        binding.firstDateTextView.text = currentDateForText

        val tomorrowDate = LocalDate.now().plusDays(1)
        val formattedTomorrowDate = tomorrowDate.format(DateTimeFormatter.ofPattern("yyyy-M-dd"))
        loadExchangeList(formattedTomorrowDate)

        val formattedTomorrowDateForText =
            tomorrowDate.format(DateTimeFormatter.ofPattern("dd.M.yy"))
        binding.secondDateTextView.text = formattedTomorrowDateForText

    }

    private fun loadExchangeList(date: String) {
        AppServices
            .exchangesService
            .getAllExchanges(date)
            .enqueue(object : Callback<List<Exchange>> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<List<Exchange>>,
                    response: Response<List<Exchange>>
                ) {
                    val bodyData = response.body()
                    Timber.d("GET_URL: ${call.request().url}")
                    if (response.code() == 200) {
                        if (bodyData != null) {
                            val sdf = SimpleDateFormat("yyyy-M-dd", Locale.ROOT)
                            val currentDate = sdf.format(Date())

                            val tomorrowDate = LocalDate.now().plusDays(1)
                            val formattedTomorrowDate = tomorrowDate.format(DateTimeFormatter.ofPattern("yyyy-M-dd"))
                            if (date == currentDate) {
                                binding.navHostFragment.visibility = View.VISIBLE
                                binding.errorMessageTextView.visibility = View.GONE
                                binding.firstDateTextView.visibility = View.VISIBLE

                                isSuccessfulLoadingList = true
                                menuSettingsItem.isVisible = true

                                mainViewModel.todayExchangesList.value = bodyData
                            } else if (date == formattedTomorrowDate){
                                if (bodyData.isNotEmpty()) {
                                    binding.secondDateTextView.visibility = View.VISIBLE
                                    mainViewModel.tomorrowExchangesList.value = bodyData
                                } else {
                                    //load yesterday
                                    binding.secondDateTextView.visibility = View.INVISIBLE
                                    mainViewModel.tomorrowExchangesList.value = listOf()
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Exchange>>, t: Throwable) {
                    binding.navHostFragment.visibility = View.GONE
                    binding.errorMessageTextView.visibility = View.VISIBLE
                    binding.firstDateTextView.visibility = View.INVISIBLE
                    binding.secondDateTextView.visibility = View.INVISIBLE
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
        when (item.itemId) {
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