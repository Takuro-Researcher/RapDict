package com.rapdict.takuro.rapdict.Common

import android.app.Application
import com.rapdict.takuro.rapdict.exp.UserExpViewModel
import org.koin.android.experimental.dsl.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(applicationContext)
            modules(module)
        }
    }

    // Koinモジュール
    private val module: Module = module {
        viewModel { UserExpViewModel(get()) }
    }
}