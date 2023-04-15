package com.oguzhancetin.pomodoro.presentation.screen.util

import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.domain.model.TaskItem

sealed class UIState<T> {
    object Loading : UIState<Nothing>()
    data class Success<T>(
        val data:T
        ) : UIState<T>()

    data class Error<T>(val message: String) : UIState<T>()
}