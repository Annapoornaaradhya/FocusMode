package com.example.focusmode.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.focusmode.databinding.ActivitySettingsBinding
import com.example.focusmode.utils.PreferenceManager

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PreferenceManager(this)
        setupUI()
    }

    private fun setupUI() {
        binding.switchDarkMode.isChecked = prefManager.isDarkMode()
        binding.switchSound.isChecked = prefManager.isSoundEnabled()
        binding.switchVibration.isChecked = prefManager.isVibrationEnabled()
        
        binding.etFocusDuration.setText(prefManager.getFocusDuration().toString())
        binding.etShortBreak.setText(prefManager.getShortBreakDuration().toString())

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefManager.setDarkMode(isChecked)
        }

        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefManager.setSoundEnabled(isChecked)
        }

        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            prefManager.setVibrationEnabled(isChecked)
        }

        binding.etFocusDuration.addTextChangedListener {
            val text = it.toString()
            if (text.isNotEmpty()) {
                prefManager.setFocusDuration(text.toInt())
            }
        }

        binding.etShortBreak.addTextChangedListener {
            val text = it.toString()
            if (text.isNotEmpty()) {
                prefManager.setShortBreakDuration(text.toInt())
            }
        }
    }
}
