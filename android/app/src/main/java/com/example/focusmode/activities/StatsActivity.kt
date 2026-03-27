package com.example.focusmode.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.focusmode.database.AppDatabase
import com.example.focusmode.databinding.ActivityStatsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChart()
        loadStats()
    }

    private fun setupChart() {
        binding.barChart.description.isEnabled = false
        binding.barChart.setDrawGridBackground(false)
        binding.barChart.xAxis.textColor = Color.WHITE
        binding.barChart.axisLeft.textColor = Color.WHITE
        binding.barChart.axisRight.isEnabled = false
        binding.barChart.legend.textColor = Color.WHITE
    }

    private fun loadStats() {
        val database = AppDatabase.getDatabase(this)
        
        database.sessionDao().getTotalSessionsCount().observe(this) { count ->
            binding.tvTotalSessions.text = count.toString()
        }

        database.sessionDao().getTotalFocusTime().observe(this) { totalMinutes ->
            val minutes = totalMinutes ?: 0
            val h = minutes / 60
            val m = minutes % 60
            binding.tvTotalFocusTime.text = "${h}h ${m}m"
        }

        // Dummy data for chart (Weekly)
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 120f))
        entries.add(BarEntry(1f, 150f))
        entries.add(BarEntry(2f, 80f))
        entries.add(BarEntry(3f, 200f))
        entries.add(BarEntry(4f, 180f))
        entries.add(BarEntry(5f, 220f))
        entries.add(BarEntry(6f, 160f))

        val dataSet = BarDataSet(entries, "Focus Minutes")
        dataSet.color = Color.parseColor("#BB86FC")
        dataSet.valueTextColor = Color.WHITE
        
        val barData = BarData(dataSet)
        binding.barChart.data = barData
        binding.barChart.invalidate()
    }
}
