package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(id:UUID): Category {
        return categoryRepository.categoryById(id)
    }

}