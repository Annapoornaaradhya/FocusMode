package com.example.focusmode.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val duration: Int, // in minutes
    val type: String // "Focus", "Short Break", "Long Break"
)
