package com.oguzhancetin.pomodoro.domain.use_case.pomodoro

import com.oguzhancetin.pomodoro.core.model.Pomodoro
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepository
import javax.inject.Inject

/**
 * Created by ocetin00 on 17.02.2023
 */
class GetPomodoroByIdUseCase  @Inject constructor(private val pomodoroRepository: PomodoroRepository) {

    suspend operator fun invoke(pomodoro: Pomodoro) {
        return pomodoroRepository.insertPomodoro(pomodoro)
    }

}