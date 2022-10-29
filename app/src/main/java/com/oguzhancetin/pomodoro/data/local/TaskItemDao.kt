package com.oguzhancetin.pomodoro.data.local

import androidx.room.*
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskItemDao {

    @Query("SELECT * FROM task_item WHERE is_finished = 0")
    fun tasKItems(): Flow<List<TaskItem>>

    @Query("SELECT * FROM task_item WHERE is_finished = 1 AND done_date >= :current_week_millis")
    fun doneTaskItems(current_week_millis:Long): Flow<List<TaskItem>>

    @Query("SELECT * FROM task_item WHERE is_favorite = 1 AND is_finished = 0 ORDER BY create_date DESC")
    fun favoriteTaskItems(): Flow<List<TaskItem>>

    @Delete
    suspend fun delete(taskItem: TaskItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskItem: TaskItem)

    @Query("DELETE FROM TASK_ITEM")
    suspend fun deleteAllTaskItem()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTaskItem(taskItem: TaskItem)

    @Query("SELECT * FROM task_item WHERE id = :id ")
    fun taskItem(id:Int): Flow<TaskItem>



}