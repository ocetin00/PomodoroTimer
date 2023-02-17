package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepositoryImpl
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ocetin00 on 21.01.2023
 */

@Singleton
class GetTasksUseCase @Inject constructor(
    private val taskItemRepository: TaskItemRepository
    ) {
    operator fun invoke(): Flow<Resource<List<TaskItem>>> {
        return taskItemRepository.getTaskItems()
    }
}