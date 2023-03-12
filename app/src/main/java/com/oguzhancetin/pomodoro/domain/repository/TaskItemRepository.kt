package com.oguzhancetin.pomodoro.domain.repository

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.common.util.FilterType
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

/**
 * Created by ocetin00 on 15.01.2023
 */
interface TaskItemRepository {
    fun getTaskItems(): Flow<Resource<List<TaskItem>>>
    fun getFavoriteTaskItems() : Flow<Resource<List<TaskItem>>>
    suspend fun deleteTaskItem(taskItem: TaskItem)
    suspend fun insertTaskItem(taskItem: TaskItem)
    fun getTaskItemById(id: Int) : TaskItem

    fun getDoneTaskItems(filterType: FilterType) : List<TaskItem>
    suspend fun updateTask(taskItem: TaskItem)

}