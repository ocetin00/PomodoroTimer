package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepository
import javax.inject.Inject

class GetTasksByCategoryNameUseCase @Inject constructor(
    private val taskItemRepository: TaskItemRepository
) {
    suspend operator fun invoke(categoryName:String): Map<Category, List<TaskItem>> {
        return taskItemRepository.getTaskByCategoryName(categoryName)

    }
}