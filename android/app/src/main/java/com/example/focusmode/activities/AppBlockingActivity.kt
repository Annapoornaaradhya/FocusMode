package com.example.focusmode.activities

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusmode.adapters.AppBlockAdapter
import com.example.focusmode.databinding.ActivityBlockingBinding
import com.example.focusmode.models.AppBlockInfo
import com.example.focusmode.utils.PreferenceManager

class AppBlockingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlockingBinding
    private lateinit var prefManager: PreferenceManager
    private lateinit var adapter: AppBlockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlockingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PreferenceManager(this)
        setupRecyclerView()
        loadApps()

        binding.btnSaveBlocking.setOnClickListener {
            Toast.makeText(this, "Configuration Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = AppBlockAdapter { app, isBlocked ->
            prefManager.setAppBlocked(app.packageName, isBlocked)
        }
        binding.rvApps.layoutManager = LinearLayoutManager(this)
        binding.rvApps.adapter = adapter
    }

    private fun loadApps() {
        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val blockList = mutableListOf<AppBlockInfo>()

        for (app in apps) {
            if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                val name = pm.getApplicationLabel(app).toString()
                val packageName = app.packageName
                blockList.add(AppBlockInfo(name, packageName, prefManager.isAppBlocked(packageName)))
            }
        }
        
        adapter.submitList(blockList.sortedBy { it.name })
    }
}
