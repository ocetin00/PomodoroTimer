package com.oguzhancetin.pomodoro

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oguzhancetin.pomodoro.di.appModule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify


class DTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkModule() {
        appModule.verify()
    }
}