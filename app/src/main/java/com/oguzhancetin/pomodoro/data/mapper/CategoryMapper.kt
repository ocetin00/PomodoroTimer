package com.oguzhancetin.pomodoro.data.mapper

import com.oguzhancetin.pomodoro.data.local.entity.CategoryEntity
import com.oguzhancetin.pomodoro.data.local.entity.PomodoroEntity
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.Pomodoro


fun CategoryEntity.toCategory(): Category {
    return Category(
        id = this.id,
        name = this.name
    )
}

fun Category.toCategoryEntity():CategoryEntity  {
    return CategoryEntity(
        id = this.id,
        name = this.name
    )
}