package com.exchangerates.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.exchangerates.MainActivity
import com.exchangerates.R
import com.exchangerates.adapter.ExchangesAdapter
import com.exchangerates.databinding.FragmentMainBinding
import com.exchangerates.model.Exchange
import com.exchangerates.viewmodel.MainViewModel
import timber.log.Timber
import kotlin.streams.toList

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var exchangesRecyclerViewAdapter: ExchangesAdapter
    private var todayExchangesList = arrayListOf<Exchange>()
    private var tomorrowExchangesList = arrayListOf<Exchange>()

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle()

        initializeExchangesRecyclerView()

        viewModel.todayExchangesList.observe(viewLifecycleOwner, {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                todayExchangesList = ArrayList(
                    it.parallelStream()
                        .filter { item ->
                            item.isShowing
                        }.toList()
                )
            }
            exchangesRecyclerViewAdapter.setExchangesLists(
                todayExchangesList,
                tomorrowExchangesList
            )
        })

        viewModel.tomorrowExchangesList.observe(viewLifecycleOwner, {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tomorrowExchangesList = ArrayList(
                    it.parallelStream()
                        .filter { item ->
                            item.isShowing
                        }.toList()
                )
            }
            exchangesRecyclerViewAdapter.setExchangesLists(
                todayExchangesList,
                tomorrowExchangesList
            )
        })


    }

    private fun initializeExchangesRecyclerView() {
        val recyclerView = binding.mainRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        exchangesRecyclerViewAdapter = ExchangesAdapter(todayExchangesList, tomorrowExchangesList)
        recyclerView.adapter = exchangesRecyclerViewAdapter
    }

    private fun setActionBarTitle() {
        mainActivity.setActionBarTitle("Курсы валют")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Timber.d("FRAGMENT_SETTINGS_CLICKED")
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}