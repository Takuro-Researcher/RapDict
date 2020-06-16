package com.rapdict.takuro.rapdict.Common

import android.app.Application
import com.rapdict.takuro.rapdict.makeQuestion.QuestionListViewModel
import com.rapdict.takuro.rapdict.dict.ListViewModel
import com.rapdict.takuro.rapdict.exp.UserExpViewModel
import com.rapdict.takuro.rapdict.game.GamePlayViewModel
import com.rapdict.takuro.rapdict.gameSetting.GameSettingViewModel
import com.rapdict.takuro.rapdict.model.RapDataBase
import com.rapdict.takuro.rapdict.result.ResultListViewModel
import com.rapdict.takuro.rapdict.result.ResultViewModel
import com.rapdict.takuro.rapdict.userSetting.UserSettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class App : Application() {
    companion object{
        lateinit var db: RapDataBase
    }

    override fun onCreate() {
        super.onCreate()
        db = RapDataBase.getInstance(applicationContext)
        startKoin{
            androidContext(applicationContext)
            modules(module)
        }
    }

    // Koinモジュール
    private val module: Module = module {
        viewModel { ResultViewModel(get()) }
        viewModel { UserExpViewModel(get()) }
        viewModel { UserSettingViewModel(get()) }
        viewModel { GameSettingViewModel(get()) }
        viewModel { ListViewModel(get()) }
        viewModel { GamePlayViewModel(get()) }
        viewModel { ResultListViewModel(get())}
        viewModel { QuestionListViewModel(get()) }
    }
}