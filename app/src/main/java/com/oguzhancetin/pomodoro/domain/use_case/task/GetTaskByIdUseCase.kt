package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepository
import java.util.UUID
import javax.inject.Inject


class GetTaskByIdUseCase @Inject constructor(
    private val taskItemRepository: TaskItemRepository
) {
     suspend operator fun invoke(id: UUID): TaskItem {
        return taskItemRepository.getTaskItemById(id)
    }
}