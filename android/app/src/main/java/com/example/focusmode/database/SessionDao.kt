package com.example.focusmode.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.focusmode.models.StudySession

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: StudySession)

    @Query("SELECT * FROM study_sessions ORDER BY date DESC")
    fun getAllSessions(): LiveData<List<StudySession>>

    @Query("SELECT SUM(duration) FROM study_sessions WHERE type = 'Focus'")
    fun getTotalFocusTime(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM study_sessions WHERE type = 'Focus'")
    fun getTotalSessionsCount(): LiveData<Int>
}
