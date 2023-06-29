package com.oguzhancetin.pomodoro.domain.use_case.category

import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.data.repository.CategoryRepository
import java.util.UUID
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(id:UUID): Category {
        return categoryRepository.categoryById(id)
    }

}