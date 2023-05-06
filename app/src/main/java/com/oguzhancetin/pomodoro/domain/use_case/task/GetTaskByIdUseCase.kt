package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject


class GetTaskByIdUseCase @Inject constructor(
    private val taskItemRepository: TaskItemRepository
) {
     operator fun invoke(id: UUID): Flow<Resource<TaskItem>> {
        return taskItemRepository.getTaskItemById(id)

    }
}