package com.oguzhancetin.pomodoro

import android.app.Activity
import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import com.google.android.play.core.review.ReviewManagerFactory
import com.oguzhancetin.pomodoro.core.preference.dataStore
import com.oguzhancetin.pomodoro.core.time.Time
import com.oguzhancetin.pomodoro.di.appModule
import com.oguzhancetin.pomodoro.di.dbModule
import com.oguzhancetin.pomodoro.di.repositoryModule
import com.oguzhancetin.pomodoro.di.useCaseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PomodoroApplication : Application() {
    /*  @Inject
      lateinit var workerFactory: HiltWorkerFactory

      override fun getWorkManagerConfiguration() =
          Configuration.Builder()
              .setWorkerFactory(workerFactory)
              .build()*/

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(listOf(appModule, dbModule,useCaseModule,repositoryModule))
        }
        CoroutineScope(Dispatchers.Main).launch {
            initializeDataStoreValues()
        }
    }

    private suspend fun initializeDataStoreValues() {
        this.dataStore.edit { settings ->
            val currentValue = settings[Time.Pomodoro().getPrefKey()]
            if (currentValue == null) {
                settings[Time.Pomodoro().getPrefKey()] = 1500000
            }
        }
        this.dataStore.edit { settings ->
            val currentValue = settings[Time.Long().getPrefKey()]
            if (currentValue == null) {
                settings[Time.Long().getPrefKey()] = 900000
            }
        }
        this.dataStore.edit { settings ->
            val currentValue = settings[Time.Short().getPrefKey()]
            if (currentValue == null) {
                settings[Time.Short().getPrefKey()] = 300000
            }
        }
    }
}