package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject


class GetTasksByCategoryIdUseCase @Inject constructor(
    private val taskItemRepository: TaskItemRepository
) {
    suspend operator fun invoke(id:UUID): Map<Category, List<TaskItem>> {
        return taskItemRepository.getTaskByCategoryId(id)

    }
}