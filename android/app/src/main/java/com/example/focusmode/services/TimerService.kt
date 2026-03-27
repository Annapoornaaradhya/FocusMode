package com.example.focusmode.services

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.focusmode.R
import com.example.focusmode.activities.TimerActivity
import com.example.focusmode.utils.Constants

class TimerService : Service() {

    private val binder = TimerBinder()
    
    val timeLeftLiveData = MutableLiveData<Long>()
    val isRunningLiveData = MutableLiveData<Boolean>()
    val timerFinishedLiveData = MutableLiveData<Boolean>()
    
    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 0

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START -> {
                val duration = intent.getLongExtra("duration", 25 * 60 * 1000L)
                startTimer(duration)
            }
            Constants.ACTION_STOP -> stopTimer()
        }
        return START_STICKY
    }

    fun startTimer(duration: Long) {
        remainingTime = duration
        countDownTimer?.cancel()
        
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                timeLeftLiveData.postValue(millisUntilFinished)
                updateNotification(formatTime(millisUntilFinished))
            }

            override fun onFinish() {
                isRunningLiveData.postValue(false)
                timerFinishedLiveData.postValue(true)
                stopForeground(true)
                stopSelf()
            }
        }
        
        countDownTimer?.start()
        isRunningLiveData.postValue(true)
        startForeground(Constants.NOTIFICATION_ID, createNotification("Session Started"))
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        isRunningLiveData.postValue(false)
    }

    fun resumeTimer() {
        startTimer(remainingTime)
    }

    fun stopTimer() {
        countDownTimer?.cancel()
        isRunningLiveData.postValue(false)
        stopForeground(true)
        stopSelf()
    }

    private fun createNotification(content: String): Notification {
        val channel = NotificationChannel(
            Constants.CHANNEL_ID,
            "Focus Timer",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val intent = Intent(this, TimerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setContentTitle("Focus Mode Active")
            .setContentText(content)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(content: String) {
        val notification = createNotification(content)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(Constants.NOTIFICATION_ID, notification)
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
