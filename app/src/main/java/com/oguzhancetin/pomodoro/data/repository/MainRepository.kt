package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import com.oguzhancetin.pomodoro.core.database.entity.TaskItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MainRepository  {
    fun getFavoriteTaskItems(): Flow<List<TaskItemEntity>>
    suspend fun updateTask(taskItem: TaskItemEntity)


}