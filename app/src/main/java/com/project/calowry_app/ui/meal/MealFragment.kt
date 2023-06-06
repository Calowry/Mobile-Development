package com.project.calowry_app.ui.meal

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.project.calowry_app.databinding.FragmentMealBinding

class MealFragment : Fragment() {

    private var _binding: FragmentMealBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val MealViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(MealViewModel::class.java)

        _binding = FragmentMealBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMeal
        MealViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}