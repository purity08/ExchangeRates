package com.exchangerates

import android.annotation.SuppressLint
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

    private val mainViewModel: MainViewModel by viewModels()

    lateinit var binding: MainActivityBinding
    lateinit var menuSettingsItem: MenuItem
    lateinit var menuApplyItem: MenuItem

    var isYesterdayNeeded: Boolean = false
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

        Timber.d("Main_onCreate")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.firstDateTextView.visibility = View.VISIBLE
        binding.secondDateTextView.visibility = View.VISIBLE

        val sdf = SimpleDateFormat("yyyy-M-dd", Locale.ROOT)
        val currentDate = sdf.format(Date())
        loadExchangeList(currentDate)

        val sdfForDateText = SimpleDateFormat("dd.M.yy", Locale.ROOT)
        val currentDateForText = sdfForDateText.format(Date())
        binding.firstDateTextView.text = currentDateForText

        val tomorrowDate = LocalDate.now().plusDays(1)
        //val tomorrowDate = LocalDate.now().minusDays(1)
        val formattedTomorrowDate = tomorrowDate.format(DateTimeFormatter.ofPattern("yyyy-M-dd"))
        loadExchangeList(formattedTomorrowDate)

    }

    private fun loadExchangeList(date: String) {
        AppServices
            .exchangesService
            .getAllExchanges(date)
            .enqueue(object : Callback<List<Exchange>> {
                @SuppressLint("SetTextI18n")
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

                            //val tomorrowDate = LocalDate.now().minusDays(1)
                            val formattedTomorrowDate = LocalDate.now().plusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-M-dd"))

                            val formattedYesterdayDate = LocalDate.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-M-dd"))

                            if (date == currentDate) {
                                binding.navHostFragment.visibility = View.VISIBLE
                                binding.errorMessageTextView.visibility = View.GONE
                                binding.firstDateTextView.visibility = View.VISIBLE

                                isSuccessfulLoadingList = true
                                menuSettingsItem.isVisible = true

                                for(item in bodyData) {
                                    when(item.Cur_Abbreviation) {
                                        "EUR" -> item.isShowing = true
                                        "USD" -> item.isShowing = true
                                        "RUB" -> item.isShowing = true
                                    }
                                }
                                mainViewModel.todayExchangesList.value = bodyData
                            } else if (date == formattedTomorrowDate) {
                                if (bodyData.isNotEmpty()) {
                                    binding.secondDateTextView.visibility = View.VISIBLE

                                    val dateText = date.split("-")
                                    val yearText = "${dateText[2]}.${dateText[1]}.${dateText[0].substring(2)}"

                                    binding.secondDateTextView.text = yearText

                                    for(item in bodyData) {
                                        when(item.Cur_Abbreviation) {
                                            "EUR" -> item.isShowing = true
                                            "USD" -> item.isShowing = true
                                            "RUB" -> item.isShowing = true
                                        }
                                    }

                                    mainViewModel.tomorrowExchangesList.value = bodyData
                                    for (item in mainViewModel.tomorrowExchangesList.value!!) {
                                        if (item.Cur_Abbreviation == "USD"
                                            || item.Cur_Abbreviation == "EUR"
                                            || item.Cur_Abbreviation == "RUB"
                                        ) {
                                            item.isShowing = true
                                        }
                                    }
                                } else {
                                    //load yesterday

                                    loadExchangeList(formattedYesterdayDate)
                                    //binding.secondDateTextView.visibility = View.INVISIBLE
                                    //mainViewModel.tomorrowExchangesList.value = listOf()
                                }
                            } else if (date == formattedYesterdayDate) {
                                AppServices
                                    .exchangesService
                                    .getAllExchanges(formattedTomorrowDate)
                                    .enqueue(object : Callback<List<Exchange>> {
                                        override fun onResponse(
                                            call: Call<List<Exchange>>,
                                            response: Response<List<Exchange>>
                                        ) {
                                            val body = response.body()
                                            Timber.d("GET_URL: ${call.request().url}")
                                            if (response.code() == 200) {
                                                if (body != null) {
                                                    binding.secondDateTextView.visibility = View.VISIBLE

                                                    val dateText = date.split("-")
                                                    val yearText = "${dateText[2]}.${dateText[1]}.${dateText[0].substring(2)}"

                                                    binding.secondDateTextView.text = yearText

                                                    for(item in bodyData) {
                                                        when(item.Cur_Abbreviation) {
                                                            "EUR" -> item.isShowing = true
                                                            "USD" -> item.isShowing = true
                                                            "RUB" -> item.isShowing = true
                                                        }
                                                    }

                                                    mainViewModel.tomorrowExchangesList.value = bodyData
                                                    for (item in mainViewModel.tomorrowExchangesList.value!!) {
                                                        if (item.Cur_Abbreviation == "USD"
                                                            || item.Cur_Abbreviation == "EUR"
                                                            || item.Cur_Abbreviation == "RUB"
                                                        ) {
                                                            item.isShowing = true
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<List<Exchange>>,
                                            t: Throwable
                                        ) {

                                        }
                                    })
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
                binding.firstDateTextView.visibility = View.INVISIBLE
                binding.secondDateTextView.visibility = View.INVISIBLE
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}