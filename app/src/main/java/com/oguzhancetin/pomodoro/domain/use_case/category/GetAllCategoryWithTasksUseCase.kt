package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoryWithTasksUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    operator fun invoke(): Flow<List<CategoryWithTask>> {
        return categoryRepository.getAllCategoryWithTask()
    }

}