package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CommonTool
import kotlinx.coroutines.runBlocking

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var barArray: MutableLiveData<List<Int>> = MutableLiveData()
    var minArray: MutableLiveData<List<Int>> = MutableLiveData()
    var maxArray: MutableLiveData<List<Int>> = MutableLiveData()
    var questionArray: MutableLiveData<List<Int>> = MutableLiveData()
    var dictNameArray: MutableLiveData<MutableList<String>> = MutableLiveData()
    var beatTypeArray:MutableLiveData<List<String>> = MutableLiveData()
    var drumOnly:MutableLiveData<Boolean> = MutableLiveData()

    var dictUidArray : MutableLiveData<MutableList<Int>> = MutableLiveData()

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){
        barArray.value = listOf(2,4,8)
        minArray.value = CommonTool.makeNumArray(3,7)
        maxArray.value = CommonTool.makeNumArray(8,11)
        questionArray.value = CommonTool.makeNumArray(10,30,10)
        beatTypeArray.value = listOf("low","middle","high","technical")
        drumOnly.value = false

        val nameArray = mutableListOf("日本語辞書")
        val uidArray = mutableListOf(-1)

        runBlocking {
            var dictDao = db.mydictDao()
            var dictData = dictDao.findAll()
            dictData.forEach {
                nameArray.add(it.name!!)
                uidArray.add(it.uid)
            }
            dictNameArray.value = nameArray
            dictUidArray.value = uidArray
        }


    }
//    fun updateMaxData(min:Int){
//        maxArray.value = commonTool.makeNumArray(min+1,10)
//    }
}