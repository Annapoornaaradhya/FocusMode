package com.example.focusmode.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusmode.adapters.HistoryAdapter
import com.example.focusmode.database.AppDatabase
import com.example.focusmode.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeHistory()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter
    }

    private fun observeHistory() {
        AppDatabase.getDatabase(this).sessionDao().getAllSessions().observe(this) { sessions ->
            adapter.submitList(sessions)
        }
    }
}
