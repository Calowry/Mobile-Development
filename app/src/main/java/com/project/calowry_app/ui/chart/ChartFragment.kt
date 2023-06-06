package com.project.calowry_app.ui.chart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.project.calowry_app.databinding.FragmentChartBinding

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ChartViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(ChartViewModel::class.java)

        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textChart
        ChartViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}