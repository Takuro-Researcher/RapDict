package com.rapdict.takuro.rapdict.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class GameViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){

    }

}