package com.example.focusmode.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.focusmode.database.AppDatabase
import com.example.focusmode.databinding.ActivityMainBinding
import com.example.focusmode.utils.Constants
import com.example.focusmode.utils.PreferenceManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PreferenceManager
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PreferenceManager(this)
        database = AppDatabase.getDatabase(this)

        setupUI()
        observeData()
    }

    private fun setupUI() {
        binding.tvQuote.text = Constants.QUOTES.random()
        binding.tvStreak.text = "${prefManager.getStreak()} Days"

        binding.btnStartFocus.setOnClickListener {
            startActivity(Intent(this, TimerActivity::class.java))
        }

        binding.cardAppBlocking.setOnClickListener {
            startActivity(Intent(this, AppBlockingActivity::class.java))
        }

        binding.cardHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.cardStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        binding.cardSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun observeData() {
        database.sessionDao().getTotalSessionsCount().observe(this) { count ->
            binding.tvSessionsCount.text = count.toString()
            updateRank(count)
        }

        database.sessionDao().getTotalFocusTime().observe(this) { totalMinutes ->
            val minutes = totalMinutes ?: 0
            val h = minutes / 60
            val m = minutes % 60
            binding.tvTodayTime.text = "${h}h ${m}m"
        }
    }

    private fun updateRank(sessions: Int) {
        val rank = when {
            sessions > 100 -> "Master"
            sessions > 50 -> "Expert"
            sessions > 20 -> "Advanced"
            sessions > 5 -> "Apprentice"
            else -> "Novice"
        }
        binding.tvRank.text = rank
    }
}
