package com.project.calowry_app.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.project.calowry_app.R
import com.project.calowry_app.database.local.FoodDailyDao
import com.project.calowry_app.databinding.FragmentHomeBinding
import com.project.calowry_app.ui.base.CalowryApplication
import com.project.calowry_app.utils.ConstVal
import com.project.calowry_app.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var userNameTextView: TextView
    private lateinit var currentCaloriesTextView: TextView
    private lateinit var maxCaloriesTextView: TextView
    private lateinit var carbsTextView: TextView
    private lateinit var carbsReachedTextView: TextView
    private lateinit var sugarTextView: TextView
    private lateinit var sugarReachedTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var proteinReachedTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        userNameTextView = view.findViewById(R.id.user_name)
        currentCaloriesTextView = view.findViewById(R.id.calories_reached)
        maxCaloriesTextView = view.findViewById(R.id.maxCalories)
        carbsTextView = view.findViewById(R.id.carbs)
        carbsReachedTextView = view.findViewById(R.id.carbs_reached)
        sugarTextView = view.findViewById(R.id.sugar)
        sugarReachedTextView = view.findViewById(R.id.sugar_reached)
        proteinTextView = view.findViewById(R.id.protein)
        proteinReachedTextView = view.findViewById(R.id.protein_reached)

        // Set the user name

        var pref: SessionManager = SessionManager(requireContext())
        val userName = pref.getUserName // Get the user name from ConstVal.kt
        userNameTextView.text = getString(R.string.hello_name, userName)

        // Calculate and display the target calories
        val targetCalories = calculateTargetCalories()
        maxCaloriesTextView.text = "of $targetCalories Cal"

        // Calculate and display the current calories reached
        val currentDate = getCurrentDate()
        GlobalScope.launch(Dispatchers.IO) {
            val currentCalories =
                CalowryApplication.database.foodDailyDao().sumCaloriesValueByDate(currentDate)

            currentCaloriesTextView.text = currentCalories.toString()

            val caloriesProgress = (currentCalories / targetCalories.toFloat()) * 100
            requireActivity().runOnUiThread {
                val caloriesProgressBar =
                    view?.findViewById<CircularProgressIndicator>(R.id.calories_progress)

                caloriesProgressBar?.progress = caloriesProgress.toInt()
            }
        }

        // Display other nutrient information (carbs, sugar, protein)
        displayNutrientInfo()

        return view
    }

    // Function to calculate the target calories based on the given formula
    private fun calculateTargetCalories(): Int {
        val weight = 55
        val height = 150
        val age = 21

        return ((10 * weight) + (6.25 * height) - (5 * age) - 5).toInt()
    }

    // Function to get the current date in the format "yyyy-MM-dd"
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    // Function to display the nutrient information (carbs, sugar, protein)
    private fun displayNutrientInfo() {
        val currentDate = getCurrentDate()
        // Get the nutrient information from your data source or calculate it
        GlobalScope.launch(Dispatchers.IO) {
            val maxCarbs = 300
            val maxProtein = 60
            val maxSugar = 30
            val carbsConsumed =
                CalowryApplication.database.foodDailyDao().sumCarbsValueByDate(currentDate)
            val proteinConsumed =
                CalowryApplication.database.foodDailyDao().sumProteinValueByDate(currentDate)
            val sugarConsumed =
                CalowryApplication.database.foodDailyDao().sumSugarValueByDate(currentDate)

            carbsReachedTextView.text = "$carbsConsumed of $maxCarbs g"
            proteinReachedTextView.text = "$proteinConsumed of $maxProtein g"
            sugarReachedTextView.text = "$sugarConsumed of $maxSugar g"

            // Calculate the progress values for each nutrient
            val carbsProgress = (carbsConsumed / maxCarbs.toFloat()) * 100
            val proteinProgress = (proteinConsumed / maxProtein.toFloat()) * 100
            val sugarProgress = (sugarConsumed / maxSugar.toFloat()) * 100

            // Set the progress values in the progress bars
            requireActivity().runOnUiThread {
                val carbsProgressBar =
                    view?.findViewById<LinearProgressIndicator>(R.id.carbs_progress)
                val proteinProgressBar =
                    view?.findViewById<LinearProgressIndicator>(R.id.protein_progress)
                val sugarProgressBar =
                    view?.findViewById<LinearProgressIndicator>(R.id.sugar_progress)

                carbsProgressBar?.progress = carbsProgress.toInt()
                proteinProgressBar?.progress = proteinProgress.toInt()
                sugarProgressBar?.progress = sugarProgress.toInt()
            }
        }
    }
}
