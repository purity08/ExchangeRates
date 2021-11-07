package com.exchangerates

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.exchangerates.databinding.MainActivityBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

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