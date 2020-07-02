package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.CommonTool

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var test_text: MutableLiveData<String> = MutableLiveData()


    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){
       test_text.value = "ああああ"
    }
//    fun updateMaxData(min:Int){
//        maxArray.value = commonTool.makeNumArray(min+1,10)
//    }
}