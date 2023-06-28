package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ocetin00 on 22.01.2023
 */

@Singleton
class DeleteTaskItemUseCase @Inject constructor(private val taskItemRepository: TaskItemRepository) {

    suspend operator fun invoke(taskItem: TaskItem) {
        return taskItemRepository.deleteTaskItem(taskItem)
    }

}