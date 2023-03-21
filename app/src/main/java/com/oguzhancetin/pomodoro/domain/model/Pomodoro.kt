package com.oguzhancetin.pomodoro.domain.model

import java.util.*

/**
 * Created by ocetin00 on 12.02.2023
 */
data class Pomodoro(
    val id: UUID,
    override val doneDate: Long?,
): Doneable
