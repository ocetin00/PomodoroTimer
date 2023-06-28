package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.core.Resource
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    operator fun invoke(): Flow<Resource<List<Category>>> {
        return categoryRepository.getAllCategory()
    }

}