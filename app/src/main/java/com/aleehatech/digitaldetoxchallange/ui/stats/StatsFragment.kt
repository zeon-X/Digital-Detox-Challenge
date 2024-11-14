package com.aleehatech.digitaldetoxchallange.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleehatech.digitaldetoxchallange.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var statsAdapter: StatsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val statsViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerViewForStats
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe the app usage data
        statsViewModel.apps.observe(viewLifecycleOwner) { appList ->
            statsAdapter = StatsAdapter(requireContext(), appList)
            recyclerView.adapter = statsAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
