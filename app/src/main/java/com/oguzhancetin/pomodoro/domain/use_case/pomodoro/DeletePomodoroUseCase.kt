package com.oguzhancetin.pomodoro.domain.use_case.pomodoro

import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.domain.repository.PomodoroRepository
import javax.inject.Inject

/**
 * Created by ocetin00 on 17.02.2023
 */
class DeletePomodoroUseCase  @Inject constructor(private val pomodoroRepository: PomodoroRepository) {

    suspend operator fun invoke(pomodoro: Pomodoro) {
        return pomodoroRepository.deletePomodoro(pomodoro)
    }

}