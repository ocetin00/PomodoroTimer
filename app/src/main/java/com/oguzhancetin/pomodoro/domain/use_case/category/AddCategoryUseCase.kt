package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.data.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase  constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(category: Category) {
        categoryRepository.upsertCategory(category)
    }

}