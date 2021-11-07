package com.exchangerates.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.exchangerates.MainActivity
import com.exchangerates.R
import timber.log.Timber


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity = activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle()

    }

    private fun setActionBarTitle() {
        mainActivity = activity as MainActivity
        mainActivity.setActionBarTitle("Настройка валют")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        mainActivity.menuSettingsItem.isVisible = false
        mainActivity.menuApplyItem.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_apply -> {
                Timber.d("CLICKED_SETTINGS")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        mainActivity.binding.firstDateTextView.visibility = View.VISIBLE
        mainActivity.binding.secondDateTextView.visibility = View.VISIBLE
        super.onDestroy()
    }
}