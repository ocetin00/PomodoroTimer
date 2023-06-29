package com.oguzhancetin.pomodoro

import android.app.Application
import androidx.datastore.preferences.core.edit
import com.oguzhancetin.pomodoro.core.preference.dataStore
import com.oguzhancetin.pomodoro.core.time.Time
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