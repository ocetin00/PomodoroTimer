package com.oguzhancetin.pomodoro

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.work.WorkerParameters
import com.oguzhancetin.pomodoro.di.appModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class AppModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun appModuleTest() {
        appModule.verify(
            extraTypes = listOf(
                Application::class,
                Context::class,
                SavedStateHandle::class,
                WorkerParameters::class,
            )
        )
    }
}