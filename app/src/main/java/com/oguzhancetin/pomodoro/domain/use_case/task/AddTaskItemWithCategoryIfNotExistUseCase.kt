package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.use_case.category.AddCategoryUseCase
import javax.inject.Inject

class AddTaskItemWithCategoryIfNotExistUseCase @Inject constructor(
    private val addTaskItemUseCase: AddTaskItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) {
    suspend operator fun invoke(category: Category, taskItem: TaskItem) {
        val categoryId = category.id
        addCategoryUseCase.invoke(category)
        addTaskItemUseCase.invoke(taskItem.apply { this.categoryId = categoryId })
    }
}