package com.oguzhancetin.pomodoro.domain.repository

import com.oguzhancetin.pomodoro.core.Resource
import com.oguzhancetin.pomodoro.core.util.FilterType
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Created by ocetin00 on 15.01.2023
 */
interface TaskItemRepository {
    fun getTaskItems(): Flow<Resource<List<TaskItem>>>
    fun getFavoriteTaskItems(): Flow<Resource<List<TaskItem>>>
    suspend fun deleteTaskItem(taskItem: TaskItem)
    suspend fun insertTaskItem(taskItem: TaskItem)
     suspend fun getTaskItemById(id: UUID): TaskItem

    fun getDoneTaskItems(filterType: FilterType): List<TaskItem>
    fun getCurrentWeekTaskList(currentWeekMilist: Long): Flow<Resource<List<TaskItem>>>
    suspend fun updateTask(taskItem: TaskItem)

    suspend fun getTaskByCategoryName(categoryName: String): Map<Category, List<TaskItem>>
    suspend fun getTaskByCategoryId(id: UUID): Map<Category, List<TaskItem>>

    suspend fun deleteByCategoryId (categoryId:UUID)
}