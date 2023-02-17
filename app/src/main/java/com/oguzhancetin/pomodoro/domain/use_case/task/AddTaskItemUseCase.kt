package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ocetin00 on 22.01.2023
 */

@Singleton
class AddTaskItemUseCase @Inject constructor(private val taskItemRepository: TaskItemRepository) {

    suspend operator fun invoke(taskItem: TaskItem) {
        return taskItemRepository.insertTaskItem(taskItem)
    }

}