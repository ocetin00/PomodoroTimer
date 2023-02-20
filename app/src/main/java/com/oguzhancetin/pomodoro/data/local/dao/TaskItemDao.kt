package com.oguzhancetin.pomodoro.data.local.dao

import androidx.room.*
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskItemDao {

    @Query("SELECT * FROM task_item WHERE is_finished = 0")
    fun tasKItems(): Flow<List<TaskItemEntity>>

    @Query("SELECT * FROM task_item WHERE is_finished = 1 AND done_date >= :current_week_millis")
    fun doneTaskItems(current_week_millis:Long): Flow<List<TaskItemEntity>>

    @Query("SELECT * FROM task_item WHERE is_favorite = 1 AND is_finished = 0 ORDER BY create_date DESC")
    fun favoriteTaskItems(): Flow<List<TaskItemEntity>>

    @Delete
    suspend fun delete(taskItem: TaskItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskItem: TaskItemEntity)

    @Query("DELETE FROM TASK_ITEM")
    suspend fun deleteAllTaskItem()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTaskItem(taskItem: TaskItemEntity)

    @Query("SELECT * FROM task_item WHERE id = :id ")
    fun taskItem(id:Int): TaskItemEntity



}