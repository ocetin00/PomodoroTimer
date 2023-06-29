package com.oguzhancetin.pomodoro.domain.use_case.pomodoro

import com.oguzhancetin.pomodoro.core.model.Pomodoro
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ocetin00 on 12.02.2023
 */

@Singleton
class AddPomodoroUseCase @Inject constructor(private val pomodoroRepository: PomodoroRepository) {

    suspend operator fun invoke(pomodoro: Pomodoro) {
        return pomodoroRepository.insertPomodoro(pomodoro)
    }

}