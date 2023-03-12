package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.common.util.FilterType
import com.oguzhancetin.pomodoro.data.local.dao.TaskItemDao
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.data.mapper.toMapTaskEntity
import com.oguzhancetin.pomodoro.data.mapper.toMapTaskItem
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.handleCoroutineException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskItemRepositoryImpl @Inject constructor(private val taskItemDao: TaskItemDao) :
    TaskItemRepository {
    override fun getTaskItems(): Flow<Resource<List<TaskItem>>> {
        return taskItemDao.tasKItems()
            .onStart {
                Resource.Loading<List<TaskItemEntity>>()
            }
            .map {
                Resource.Success(it.map { m -> m.toMapTaskItem() })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }
    }

    override fun getFavoriteTaskItems(): Flow<Resource<List<TaskItem>>> {
        return taskItemDao.favoriteTaskItems()
            .onStart {
                Resource.Loading<List<TaskItemEntity>>()
            }
            .map {
                Resource.Success(it.map { m -> m.toMapTaskItem() })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }
    }

    override suspend fun deleteTaskItem(taskItem: TaskItem) =
        taskItemDao.delete(taskItem.toMapTaskEntity())

    override suspend fun insertTaskItem(taskItem: TaskItem) = taskItemDao.insert(taskItem.toMapTaskEntity())
    override fun getTaskItemById(id: Int) = taskItemDao.taskItem(id).toMapTaskItem()
    override fun getDoneTaskItems(filterType: FilterType): List<TaskItem> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskItem: TaskItem) = taskItemDao.updateTaskItem(taskItem.toMapTaskEntity())
}