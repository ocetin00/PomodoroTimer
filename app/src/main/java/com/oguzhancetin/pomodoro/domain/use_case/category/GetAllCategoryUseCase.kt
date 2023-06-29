package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getAllCategory()
    }

}