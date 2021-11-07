package com.exchangerates.ui

import android.os.Bundle
import android.view.*
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.exchangerates.MainActivity
import com.exchangerates.R
import com.exchangerates.adapter.ExchangesAdapter
import com.exchangerates.databinding.FragmentMainBinding
import com.exchangerates.model.Exchange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext

class MainFragment: Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var exchangesRecyclerViewAdapter: ExchangesAdapter
    private val todayExchangesList = arrayListOf<Exchange>()
    private val tomorrowExchangesList = arrayListOf<Exchange>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle()

        initializeExchangesRecyclerView()
        createTodayExchangesList()
    }

    private fun createTodayExchangesList() {

        for(i in 0..20) {
            todayExchangesList.add(
                Exchange(
                    "170",
                    "2020-11-06T00:00:00",
                    "AUD",
                    "1",
                    "Австралийский доллар",
                    "1.8859"))
        }
        exchangesRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun initializeExchangesRecyclerView() {
        val recyclerView = binding.mainRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        exchangesRecyclerViewAdapter = ExchangesAdapter(todayExchangesList, tomorrowExchangesList)
        recyclerView.adapter = exchangesRecyclerViewAdapter
    }

    private fun setActionBarTitle() {
        val activity: MainActivity = activity as MainActivity
        activity.setActionBarTitle("Курсы валют")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}