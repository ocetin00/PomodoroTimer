package com.oguzhancetin.pomodoro.domain.model

import java.util.UUID



data class Category(
    val id: UUID,
    var name: String
)