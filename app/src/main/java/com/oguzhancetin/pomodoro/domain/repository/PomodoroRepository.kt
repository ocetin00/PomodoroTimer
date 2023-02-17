package com.oguzhancetin.pomodoro.domain.repository

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import kotlinx.coroutines.flow.Flow


/**
 * Created by ocetin00 on 12.02.2023
 */
interface PomodoroRepository {
    fun getAllPomodoro(): Flow<Resource<List<Pomodoro>>>
    fun getCurrentWeekPomodoroList(currentWeekMillis:Long): Flow<Resource<List<Pomodoro>>>
    fun getPomodoroItemsByFinish(isFinish:Int): Flow<Resource<List<Pomodoro>>>
    suspend fun deletePomodoro(pomodoro: Pomodoro)
    suspend fun insertPomodoro(pomodoro: Pomodoro)
    fun getPomodoroById(id: Int): Flow<Resource<Pomodoro>>
    suspend fun updatePomodoro(pomodoro: Pomodoro)
}