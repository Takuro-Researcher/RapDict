package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.CommonTool

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var measureArray: MutableLiveData<List<Int>> = MutableLiveData()
    var minArray: MutableLiveData<List<Int>> = MutableLiveData()
    var maxArray: MutableLiveData<List<Int>> = MutableLiveData()
    var returnArray: MutableLiveData<List<Int>> = MutableLiveData()
    var questionArray: MutableLiveData<List<Int>> = MutableLiveData()
    private var commonTool:CommonTool = CommonTool()

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){
        val measure_array = listOf<Int>(2,4,8)
        measureArray.value = measure_array
        minArray.value = commonTool.makeNumArray(3,7)
        maxArray.value = commonTool.makeNumArray(4,10)
        returnArray.value = commonTool.makeNumArray(1,4)
        questionArray.value = commonTool.makeNumArray(10,30,10)
    }
    fun updateMaxData(min:Int){
        maxArray.value = commonTool.makeNumArray(min+1,10)
    }
}