package com.oguzhancetin.pomodoro.domain.use_case.pomodoro

import com.oguzhancetin.pomodoro.core.model.Pomodoro
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ocetin00 on 17.02.2023
 */
@Singleton
class GetAllPomodoroUseCase @Inject constructor(private val pomodoroRepository: PomodoroRepository) {

    operator fun invoke(): Flow<List<Pomodoro>> {
        return pomodoroRepository.getAllPomodoro()
    }

}