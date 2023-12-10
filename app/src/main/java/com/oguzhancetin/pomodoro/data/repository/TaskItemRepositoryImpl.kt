package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.util.FilterType
import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import com.oguzhancetin.pomodoro.core.mapper.toCategory
import com.oguzhancetin.pomodoro.core.mapper.toMapTaskEntity
import com.oguzhancetin.pomodoro.core.mapper.toMapTaskItem
import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.core.model.TaskItem
import kotlinx.coroutines.flow.*
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

class TaskItemRepositoryImpl  constructor(private val taskItemDao: TaskItemDao) :
    TaskItemRepository {
    override fun getTaskItems(): Flow<List<TaskItem>> {
        return taskItemDao.tasKItems()

            .map {
                it.map { m -> m.toMapTaskItem() }
            }

    }

    override fun getCurrentWeekTaskList(currentWeekMilist: Long): Flow<List<TaskItem>> {
        return taskItemDao.doneTaskItems(currentWeekMilist)
            .map {
                it.map { m -> m.toMapTaskItem() }
            }
    }

    override fun getFavoriteTaskItems(): Flow<List<TaskItem>> {
        return taskItemDao.favoriteTaskItems()
            .map {
                it.map { m -> m.toMapTaskItem() }
            }

    }

    override suspend fun deleteTaskItem(taskItem: TaskItem) =
        taskItemDao.delete(taskItem.toMapTaskEntity())

    override suspend fun insertTaskItem(taskItem: TaskItem) =
        taskItemDao.insert(taskItem.toMapTaskEntity())

    override suspend fun getTaskItemById(id: UUID): TaskItem {
        return taskItemDao.taskItem(id).toMapTaskItem()

    }


    override fun getDoneTaskItems(filterType: FilterType): List<TaskItem> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskItem: TaskItem) =
        taskItemDao.updateTaskItem(taskItem.toMapTaskEntity())

    override suspend fun getTaskByCategoryName(categoryName: String): Map<Category, List<TaskItem>> {
        return taskItemDao.getTaskByCategoryName(categoryName)
            .mapKeys {
                it.key.toCategory()
            }
            .mapValues {
                it.value.map { it.toMapTaskItem() }
            }
    }

    override suspend fun getTaskByCategoryId(id: UUID): Map<Category, List<TaskItem>> {
        return taskItemDao.getTaskByCategoryId(id)
            .mapKeys {
                it.key.toCategory()
            }.mapValues {
                it.value.map { it.toMapTaskItem() }
            }
    }

    override suspend fun deleteByCategoryId(categoryId: UUID) {
        taskItemDao.deleteTaskItemByCategoryId(categoryId)
    }
}