package com.oguzhancetin.pomodoro.data.local.dao

import androidx.room.*
import com.oguzhancetin.pomodoro.data.local.entity.PomodoroEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by ocetin00 on 12.02.2023
 */
@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoro")
    fun getAllPomodoro(): Flow<List<PomodoroEntity>>

    @Query("SELECT * FROM pomodoro WHERE created_date = :isFinish")
    fun pomodoroListByFinish(isFinish: Int = 0): Flow<List<PomodoroEntity>>

    @Query("SELECT * FROM pomodoro WHERE  created_date >= :current_week_millis")
    fun finishPomodoroListForCurrentWeek(current_week_millis: Long): Flow<List<PomodoroEntity>>

    @Delete
    suspend fun delete(pomodoro: PomodoroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskItem: PomodoroEntity)

    @Query("DELETE FROM TASK_ITEM")
    suspend fun deleteAllPomodoro()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePomodoro(pomodoro: PomodoroEntity)

    @Query("SELECT * FROM pomodoro WHERE id = :id ")
    fun getPomodoroById(id: Int): Flow<PomodoroEntity>





}