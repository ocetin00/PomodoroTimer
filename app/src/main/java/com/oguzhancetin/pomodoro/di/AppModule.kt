package com.oguzhancetin.pomodoro.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.oguzhancetin.pomodoro.core.database.PomodoroDatabase
import com.oguzhancetin.pomodoro.core.database.dao.CategoryDao
import com.oguzhancetin.pomodoro.core.database.dao.PomodoroDao
import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import com.oguzhancetin.pomodoro.core.database.entity.CategoryEntity
import com.oguzhancetin.pomodoro.data.repository.CategoryRepository
import com.oguzhancetin.pomodoro.data.repository.CategoryRepositoryImpl
import com.oguzhancetin.pomodoro.data.repository.MainRepository
import com.oguzhancetin.pomodoro.data.repository.MainRepositoryImpl
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepository
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepositoryImpl
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepository
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepositoryImpl
import com.oguzhancetin.pomodoro.domain.use_case.category.AddCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryWithTasksUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetCategoryByIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.AddPomodoroUseCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.GetAllPomodoroUseCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.GetCurrentWeekPomodoroListUseCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.GetPomodoroByFinishUserCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.GetPomodoroByIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.InsertPomodoroUseCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.UpdatePomodoroUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.AddTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.DeleteTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetCurrentWeekDoneTaskItems
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTaskByIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTasksUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.UpdateTaskItemUseCase
import com.oguzhancetin.pomodoro.presentation.MainActivityViewModel
import com.oguzhancetin.pomodoro.presentation.screen.main.MainViewModel
import com.oguzhancetin.pomodoro.presentation.screen.report.ReportViewModal
import com.oguzhancetin.pomodoro.presentation.screen.setting.SettingViewModel
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskDetailViewModel
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import java.util.UUID


val appModule = module {
    viewModelOf(::MainActivityViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ReportViewModal)
    viewModelOf(::SettingViewModel)
    viewModelOf(::TaskViewModel)
    viewModelOf(::TaskDetailViewModel)
}
val dbModule = module {
    single  {
        Room.databaseBuilder(
            androidContext(),
            PomodoroDatabase::class.java,
            "pomodoro_database2"
        ).addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val categoryDao = get<CategoryDao>()
                        val category = CategoryEntity(UUID.randomUUID(), "General")
                        categoryDao.upsert(category)
                    }
                }
            }
        ).fallbackToDestructiveMigration().build()
    }
    single <TaskItemDao> { get<PomodoroDatabase>().taskItemDao() }
    single <PomodoroDao> { get<PomodoroDatabase>().pomodoroDao() }
    single <CategoryDao> { get<PomodoroDatabase>().taskCategoryDao() }
    single <TaskItemRepository>{ TaskItemRepositoryImpl(get()) }
    single <PomodoroRepository>{ PomodoroRepositoryImpl(get())}
    single <CategoryRepository>{ CategoryRepositoryImpl(get()) }
    single <MainRepository>{ MainRepositoryImpl(get()) }
}
val repositoryModule = module {
    single { TaskItemRepositoryImpl(get()) }
    single { PomodoroRepositoryImpl(get()) }
    single { CategoryRepositoryImpl(get()) }
    single { MainRepositoryImpl(get()) }
}
val useCaseModule = module {
    single { AddCategoryUseCase(get()) }
    single { GetAllCategoryUseCase(get()) }
    single {GetAllCategoryWithTasksUseCase(get())}
    single { GetCategoryByIdUseCase(get()) }
    single { AddPomodoroUseCase(get()) }
    single { GetAllPomodoroUseCase(get()) }
    single { GetCurrentWeekPomodoroListUseCase(get()) }
    single { GetPomodoroByFinishUserCase(get()) }
    single { GetPomodoroByIdUseCase(get()) }
    single { InsertPomodoroUseCase(get()) }
    single { UpdatePomodoroUseCase(get()) }
    single { GetCurrentWeekDoneTaskItems(get()) }
    single { GetTasksUseCase(get()) }
    single { AddTaskItemUseCase(get()) }
    single { UpdateTaskItemUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { DeleteTaskItemUseCase(get()) }
}

