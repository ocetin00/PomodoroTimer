package com.oguzhancetin.pomodoro

import android.app.Application
import androidx.datastore.preferences.core.edit
import com.oguzhancetin.pomodoro.common.util.preference.dataStore
import com.oguzhancetin.pomodoro.util.Times
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class PomodoroApplication : Application() {
    /*  @Inject
      lateinit var workerFactory: HiltWorkerFactory

      override fun getWorkManagerConfiguration() =
          Configuration.Builder()
              .setWorkerFactory(workerFactory)
              .build()*/

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Main).launch {
            initializeDataStoreValues()
        }
    }


    private suspend fun initializeDataStoreValues() {
        this.dataStore.edit { settings ->
            val currentValue = settings[Times.Pomodoro().getPrefKey()]
            if (currentValue == null) {
                settings[Times.Pomodoro().getPrefKey()] = 1500000
            }
        }
        this.dataStore.edit { settings ->
            val currentValue = settings[Times.Long().getPrefKey()]
            if (currentValue == null) {
                settings[Times.Long().getPrefKey()] = 900000
            }
        }
        this.dataStore.edit { settings ->
            val currentValue = settings[Times.Short().getPrefKey()]
            if (currentValue == null) {
                settings[Times.Short().getPrefKey()] = 300000
            }
        }
    }

}