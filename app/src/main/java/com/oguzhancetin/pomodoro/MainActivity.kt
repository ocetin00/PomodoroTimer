package com.oguzhancetin.pomodoro

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.play.core.review.testing.FakeReviewManager
import com.oguzhancetin.pomodoro.core.preference.IS_DARK_MODE_KEY
import com.oguzhancetin.pomodoro.core.preference.dataStore
import com.oguzhancetin.pomodoro.core.database.PomodoroDatabase
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepository
import com.oguzhancetin.pomodoro.presentation.MainActivityViewModel
import com.oguzhancetin.pomodoro.presentation.PomodoroApp
import com.oguzhancetin.pomodoro.presentation.theme.PomodoroTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel



class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val viewModel: MainActivityViewModel =  getViewModel()
        /*

        private const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
        private const val IGNORE_BATTERY_OPTIMIZATIONS =
    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                  val intent = Intent()
                  val packageName = packageName
                  val pm = getSystemService(POWER_SERVICE) as PowerManager
                  if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                      intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                      intent.data = Uri.parse("package:$packageName")
                      startActivity(intent)
                  }
              }
           // Register the contract in your fragment/activity and handle the result
              val permissionRequest =
                  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

                  }

              if (ContextCompat.checkSelfPermission(
                      this,
                      POST_NOTIFICATIONS,
                  ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                      this,
                      IGNORE_BATTERY_OPTIMIZATIONS,
                  ) == PackageManager.PERMISSION_GRANTED
              ) {

              } else {
                  permissionRequest.launch(arrayOf(POST_NOTIFICATIONS, IGNORE_BATTERY_OPTIMIZATIONS))
              }*/

        // Sets up permissions request launcher.
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (val state = uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }


        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            var isDarkMode = isSystemInDarkTheme()

            when (val state = uiState) {
                is MainActivityUiState.Success -> {
                    isDarkMode = state.isDarkModel
                    showInAppReview()
                }

                is MainActivityUiState.Loading -> {

                }
            }

            val systemUiController = rememberSystemUiController()
            systemUiController.systemBarsDarkContentEnabled = !isDarkMode
            if (isDarkMode) {
                systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = false
                )
            } else {
                systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = true)
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = true
                )
            }

            /* // Update the dark content of the system bars to match the theme
             DisposableEffect(systemUiController, isDarkMode) {

                 @ColorRes val backGroundColor =
                     if (isDarkMode) R.color.colorPrimaryDark else R.color.colorPrimary
                 window.decorView.setBackgroundColor(
                     ContextCompat.getColor(
                         this@MainActivity,
                         backGroundColor
                     )
                 )
                 onDispose {}
             }*/

            PomodoroTheme(darkTheme = isDarkMode) {
                PomodoroApp()
            }
        }
    }

    private fun showInAppReview() {
        val reviewManager = FakeReviewManager(this)//ReviewManagerFactory.create(this)

        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("Review", "isSuccessful")
                reviewManager.launchReviewFlow(this, it.result)
            }
        }
    }
}



sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(
        val isDarkModel: Boolean,
        val isAnyPomodoro: Boolean = false
    ) :
        MainActivityUiState
}


