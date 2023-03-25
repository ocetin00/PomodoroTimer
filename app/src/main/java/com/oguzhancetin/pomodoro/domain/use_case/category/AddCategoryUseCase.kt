package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(category: Category) {
        return categoryRepository.insertCategory(category)
    }

}