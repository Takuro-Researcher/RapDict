package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.CommonTool

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var questionArray: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    var minArray: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    var maxArray: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    var returnArray: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    var timeArray: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    private var commonTool:CommonTool = CommonTool()

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){
        questionArray.value = commonTool.makeNumArray(10,30,10)
        minArray.value = commonTool.makeNumArray(3,7)
        maxArray.value = commonTool.makeNumArray(4,10)
        returnArray.value = commonTool.makeNumArray(1,4)
        timeArray.value = commonTool.makeNumArray(3,10)
    }
    fun updateMaxData(min:Int){
        maxArray.value = commonTool.makeNumArray(min+1,10)
    }
}