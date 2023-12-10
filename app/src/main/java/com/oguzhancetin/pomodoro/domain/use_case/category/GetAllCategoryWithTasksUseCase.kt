package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow


class GetAllCategoryWithTasksUseCase  constructor(private val categoryRepository: CategoryRepository) {
    operator fun invoke(): Flow<List<CategoryWithTask>> {
        return categoryRepository.getAllCategoryWithTask()
    }

}