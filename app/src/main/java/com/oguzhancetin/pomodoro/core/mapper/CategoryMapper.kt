package com.oguzhancetin.pomodoro.core.mapper

import com.oguzhancetin.pomodoro.core.database.entity.CategoryEntity
import com.oguzhancetin.pomodoro.core.model.Category



fun CategoryEntity.toCategory(): Category {
    return Category(
        id = this.id,
        name = this.name
    )
}

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name
    )
}