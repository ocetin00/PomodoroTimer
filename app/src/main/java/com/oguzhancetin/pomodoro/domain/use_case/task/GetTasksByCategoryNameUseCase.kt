package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskCategory
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksByCategoryNameUseCase @Inject constructor(
    private val taskItemRepository: TaskItemRepository
) {
    suspend operator fun invoke(categoryName:String): Map<Category, List<TaskItem>> {
        return taskItemRepository.getTaskByCategoryName(categoryName)

    }
}