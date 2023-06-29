package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.model.Pomodoro
import kotlinx.coroutines.flow.Flow


/**
 * Created by ocetin00 on 12.02.2023
 */
interface PomodoroRepository {
    fun getAllPomodoro(): Flow<List<Pomodoro>>
    fun getCurrentWeekPomodoroList(currentWeekMillis:Long): Flow<List<Pomodoro>>
    fun getPomodoroItemsByFinish(isFinish:Int): Flow<List<Pomodoro>>
    suspend fun deletePomodoro(pomodoro: Pomodoro)
    suspend fun insertPomodoro(pomodoro: Pomodoro)
    fun getPomodoroById(id: Int): Flow<Pomodoro>
    suspend fun updatePomodoro(pomodoro: Pomodoro)
}