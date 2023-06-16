package com.project.calowry_app.ui.meal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.project.calowry_app.R
import com.project.calowry_app.database.local.FoodDailyEntity
import com.project.calowry_app.ui.MainActivity
import com.project.calowry_app.ui.base.CalowryApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MealFragment : Fragment() {

    private lateinit var foodName: TextView
    private lateinit var caloriesValue: TextView
    private lateinit var carbsValue: TextView
    private lateinit var sugarValue: TextView
    private lateinit var proteinValue: TextView
    private lateinit var foodListSpinner: Spinner
    private lateinit var foodNamePreview: TextView
    private lateinit var caloriesPreview: TextView
    private lateinit var carbsPreview: TextView
    private lateinit var sugarPreview: TextView
    private lateinit var proteinPreview: TextView
    private lateinit var saveBtn: ImageButton

    private var lastSavedFoodLabel: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_meal, container, false)

        // Initialize views
        foodName = view.findViewById(R.id.food_name)
        caloriesValue = view.findViewById(R.id.calories_value)
        carbsValue = view.findViewById(R.id.carbs_value)
        sugarValue = view.findViewById(R.id.sugar_value)
        proteinValue = view.findViewById(R.id.protein_value)
        foodListSpinner = view.findViewById(R.id.food_list_spinner)
        foodNamePreview = view.findViewById(R.id.food_name_preview)
        caloriesPreview = view.findViewById(R.id.calories_preview)
        carbsPreview = view.findViewById(R.id.carbs_preview)
        sugarPreview = view.findViewById(R.id.sugar_preview)
        proteinPreview = view.findViewById(R.id.protein_preview)
        saveBtn = view.findViewById(R.id.save_btn)

        // Set up spinner with food items
        val foodItems = arrayOf(
            "Anggur", "Apel", "Ayam Goreng", "Brokoli", "Cap Cay",
            "Ikan Bakar", "Jamur Crispy", "Kentang", "Mie Goreng", "Nasi Putih",
            "Nasi Goreng", "Pempek Kapal Selam", "Pisang Goreng", "Rawon",
            "Rendang", "Roti", "Sate Ayam", "Sate Usus", "Sosis", "Soto Ayam",
            "Telur Mata Sapi", "Wortel"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, foodItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        foodListSpinner.adapter = adapter

        // Set up spinner item selection listener
        foodListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedFood = parent.getItemAtPosition(position).toString()
                val nutritionData = readNutritionData(selectedFood) // Implement the function to read nutrition data
                if (nutritionData != null) {
                    // Update the preview TextView elements with the selected food's nutrition data
                    foodNamePreview.text = selectedFood
                    caloriesPreview.text = nutritionData.calories
                    carbsPreview.text = nutritionData.carbohydrates
                    sugarPreview.text = nutritionData.sugar
                    proteinPreview.text = nutritionData.protein
                } else {
                    // Handle case where nutrition data is not available
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no food item is selected
            }
        }

        // Set up save button click listener
        saveBtn.setOnClickListener {
            val selectedFood = foodListSpinner.selectedItem.toString()
            val nutritionData = readNutritionData(selectedFood) // Implement the function to read nutrition data
            if (nutritionData != null) {
                // Save the food or perform other actions
                saveFood(selectedFood, nutritionData)
                Toast.makeText(requireContext(), "Food has been saved", Toast.LENGTH_SHORT).show()
            } else {
                // Handle case where nutrition data is not available
            }
        }

        // Load and display the last saved food data from Room database
        loadLastSavedFoodData()

        return view
    }

    private fun loadLastSavedFoodData() {
        // Load the last saved food data from Room database or your desired data source
        // and display it in the UI

        // Example implementation using Room database:
        GlobalScope.launch(Dispatchers.IO) {
            val lastSavedFood = CalowryApplication.database.foodDailyDao().getLastFoodDaily()
            lastSavedFoodLabel = lastSavedFood?.foodLabel

            withContext(Dispatchers.Main) {
                if (lastSavedFood != null) {
                    foodName.text = lastSavedFood.foodLabel
                    caloriesValue.text = lastSavedFood.caloriesValue
                    carbsValue.text = lastSavedFood.carbsValue
                    sugarValue.text = lastSavedFood.sugarValue
                    proteinValue.text = lastSavedFood.proteinValue
                }
            }
        }
    }

    private fun readNutritionData(foodItem: String): NutritionData? {
        // Implement the logic to read the nutrition data for the given food item
        // from the CSV file or your desired data source

        // Example implementation using the provided CSV file:
        val csvFile = requireContext().assets.open("Data.csv")
        val csvReader = BufferedReader(InputStreamReader(csvFile))
        var line: String?
        var headers: List<String>? = null
        var nutritionData: NutritionData? = null

        while (csvReader.readLine().also { line = it } != null) {
            val row = line!!.split(", ")
            if (headers == null) {
                headers = row
            } else {
                val label = row[0]
                if (label == foodItem) {
                    val calories = row[1]
                    val carbs = row[2]
                    val sugar = row[3]
                    val protein = row[4]
                    nutritionData = NutritionData(calories, carbs, sugar, protein)
                    break
                }
            }
        }
        csvReader.close()

        return nutritionData
    }

    private fun saveFood(foodItem: String, nutritionData: NutritionData) {
        // Implement the logic to save the selected food and its nutrition data
        // to your desired destination (e.g., database, API)

        // Example implementation:
        val consumedValue = "100" // Get the consumed value from user input
        val caloriesValueFinal = (consumedValue.toFloat() / 100) * nutritionData.calories.toFloat()
        val carbsValueFinal = (consumedValue.toFloat() / 100) * nutritionData.carbohydrates.toFloat()
        val sugarValueFinal = (consumedValue.toFloat() / 100) * nutritionData.sugar.toFloat()
        val proteinValueFinal = (consumedValue.toFloat() / 100) * nutritionData.protein.toFloat()

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val foodDailyEntity = FoodDailyEntity(
            scanDate = currentDate.toString(),
            foodLabel = foodItem,
            caloriesValue = caloriesValueFinal.toString(),
            carbsValue = carbsValueFinal.toString(),
            sugarValue = sugarValueFinal.toString(),
            proteinValue = proteinValueFinal.toString()
        )

        // Save the FoodDailyEntity object to the database or perform other actions
        GlobalScope.launch(Dispatchers.IO) {
            CalowryApplication.database.foodDailyDao().insertFood(foodDailyEntity)
        }

        // Redirect to desired screen or show a success message
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    data class NutritionData(
        val calories: String,
        val carbohydrates: String,
        val sugar: String,
        val protein: String
    )
}