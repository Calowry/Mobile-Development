package com.project.calowry_app.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.project.calowry_app.R
import com.project.calowry_app.ui.base.CalowryApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChartFragment : Fragment() {

    private lateinit var lineChart: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        // Initialize lineChart
        lineChart = view.findViewById(R.id.linechart_calories)

        // Configure and set data to the chart
        configureLineChart(lineChart)
        setData(lineChart)

        return view
    }

    private fun configureLineChart(lineChart: LineChart) {
        lineChart.setDrawGridBackground(false)
        lineChart.description.isEnabled = false
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.position = XAxisPosition.BOTTOM

        // Konfigurasi label sumbu X
        val xAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f
        xAxis.labelCount = 7

        // Konfigurasi label sumbu Y
        val yAxis = lineChart.axisLeft
        yAxis.setDrawGridLines(true)

        // Konfigurasi legenda
        val legend = lineChart.legend
        legend.isEnabled = false
    }

    private fun setData(lineChart: LineChart) {
        val entries = ArrayList<Entry>()

        // Get the list of food daily entities
        GlobalScope.launch(Dispatchers.IO) {
            val foodDailyList = CalowryApplication.database.foodDailyDao().getAllFoodDaily()
            var cumulativeCalories = 0f

            // Iterate over the food daily entities and calculate the cumulative sum of caloriesValue
            for (foodDailyEntity in foodDailyList) {
                val caloriesValue = foodDailyEntity.caloriesValue.toFloat()
                cumulativeCalories += caloriesValue

                // Add the entry with the cumulative sum to the entries list
                entries.add(Entry(foodDailyEntity.id.toFloat(), cumulativeCalories))
            }

            // Create the line dataset
            val dataSet = LineDataSet(entries, "Jumlah Kalori Harian")
            dataSet.color = Color.BLUE
            dataSet.setCircleColor(Color.BLUE)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 4f
            dataSet.setDrawCircleHole(false)
            dataSet.setDrawValues(false)

            // Create the line data and set it to the line chart
            val lineData = LineData(dataSet)
            lineChart.data = lineData
            lineChart.invalidate()
        }
    }

}