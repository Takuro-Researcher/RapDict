package com.rapdict.takuro.rapdict.Common

import android.app.Application
import com.rapdict.takuro.rapdict.database.RapDataBase

class App : Application() {
    companion object {
        lateinit var db: RapDataBase
    }

    override fun onCreate() {
        super.onCreate()
        db = RapDataBase.getInstance(applicationContext)
    }
}
