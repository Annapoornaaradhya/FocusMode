package com.example.focusmode.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.focusmode.R
import com.example.focusmode.database.AppDatabase
import com.example.focusmode.databinding.ActivityTimerBinding
import com.example.focusmode.models.StudySession
import com.example.focusmode.services.TimerService
import com.example.focusmode.utils.Constants
import com.example.focusmode.utils.PreferenceManager
import kotlinx.coroutines.launch

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private lateinit var prefManager: PreferenceManager
    private var timerService: TimerService? = null
    private var isBound = false

    private var sessionCount = 0
    private var currentType = "Focus"

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            isBound = true
            observeTimer()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PreferenceManager(this)
        setupUI()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    private fun setupUI() {
        binding.btnStartPause.setOnClickListener {
            if (timerService?.isRunningLiveData?.value == true) {
                timerService?.pauseTimer()
                binding.btnStartPause.text = "Resume"
                binding.btnStartPause.setIconResource(android.R.drawable.ic_media_play)
            } else {
                if (timerService?.timeLeftLiveData?.value == null || timerService?.timeLeftLiveData?.value == 0L) {
                    startNewSession()
                } else {
                    timerService?.resumeTimer()
                }
                binding.btnStartPause.text = "Pause"
                binding.btnStartPause.setIconResource(android.R.drawable.ic_media_pause)
            }
        }

        binding.btnStop.setOnClickListener {
            timerService?.stopTimer()
            finish()
        }
    }

    private fun startNewSession() {
        val durationMin = when (currentType) {
            "Focus" -> prefManager.getFocusDuration()
            "Short Break" -> prefManager.getShortBreakDuration()
            else -> 15 // Long break
        }
        
        val intent = Intent(this, TimerService::class.java).apply {
            action = Constants.ACTION_START
            putExtra("duration", durationMin * 60 * 1000L)
        }
        startService(intent)
        
        binding.circularProgressBar.progressMax = (durationMin * 60).toFloat()
        binding.tvTimerType.text = currentType
        
        val color = if (currentType == "Focus") R.color.neon_purple else R.color.neon_cyan
        binding.circularProgressBar.progressBarColor = getColor(color)
        binding.tvTimerType.setTextColor(getColor(color))
    }

    private fun observeTimer() {
        timerService?.timeLeftLiveData?.observe(this) { millis ->
            val secondsLeft = (millis / 1000).toInt()
            binding.tvTimeLeft.text = formatTime(millis)
            binding.circularProgressBar.setProgressWithAnimation(binding.circularProgressBar.progressMax - secondsLeft)
        }

        timerService?.timerFinishedLiveData?.observe(this) { finished ->
            if (finished) {
                handleSessionFinished()
            }
        }
    }

    private fun handleSessionFinished() {
        lifecycleScope.launch {
            if (currentType == "Focus") {
                val session = StudySession(
                    date = System.currentTimeMillis(),
                    duration = prefManager.getFocusDuration(),
                    type = "Focus"
                )
                AppDatabase.getDatabase(this@TimerActivity).sessionDao().insertSession(session)
                prefManager.incrementStreak()
                sessionCount++
                
                if (sessionCount % 4 == 0) {
                    currentType = "Long Break"
                } else {
                    currentType = "Short Break"
                }
            } else {
                currentType = "Focus"
            }
            
            Toast.makeText(this@TimerActivity, "Session Finished! Next: $currentType", Toast.LENGTH_SHORT).show()
            startNewSession()
        }
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
