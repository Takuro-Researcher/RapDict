package com.rapdict.takuro.rapdict.dict

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class DictViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var min: MutableLiveData<Int> = MutableLiveData()

    private var db: SQLiteDatabase? = null
    private var helper: SQLiteOpenHelper? = null

    //ViewModel初期化時にロード
    init {

    }

    private fun loadTextColor(){
    }
}