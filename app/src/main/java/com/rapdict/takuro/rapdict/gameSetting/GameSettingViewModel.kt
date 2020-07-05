package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.R
import kotlinx.coroutines.runBlocking
import java.text.FieldPosition

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var barArray: MutableLiveData<List<Int>> = MutableLiveData()
    var minArray: MutableLiveData<List<Int>> = MutableLiveData()
    var maxArray: MutableLiveData<List<Int>> = MutableLiveData()
    var questionArray: MutableLiveData<List<Int>> = MutableLiveData()
    var dictNameArray: MutableLiveData<MutableList<String>> = MutableLiveData()
    var beatTypeArray:MutableLiveData<List<String>> = MutableLiveData()
    var drumOnly:MutableLiveData<Boolean> = MutableLiveData()
    var src_data:MutableLiveData<Int> = MutableLiveData()

    var choiceDictUid :Int =-1
    var dictUidArray= mutableListOf<Int>()
    //ViewModel初期化時にロード
    init {
        loadUserData()

    }
    private fun loadUserData(){
        barArray.value = listOf(2,4,8)
        minArray.value = CommonTool.makeNumArray(3,7)
        maxArray.value = CommonTool.makeNumArray(8,11)
        questionArray.value = CommonTool.makeNumArray(10,30,10)
        beatTypeArray.value = listOf("low","middle","high","tri")
        drumOnly.value = false

        val nameArray = mutableListOf("日本語辞書")
        val uidArray = mutableListOf(-1)

        runBlocking {
            var dictDao = db.mydictDao()
            val wordDao = db.wordDao()
            var data =wordDao.countByDict()
            var dictUids = mutableListOf<Int>()
            data.forEach {
                dictUids.add(it.dictid)
            }
            val dictData = dictDao.findByIds(dictUids)
            dictData.forEach {
                nameArray.add(it.name!!)
                uidArray.add(it.uid)
            }
        }
        dictUidArray = uidArray
        dictNameArray.value = nameArray
    }

    fun changeUseDict(position: Int):Int{
        choiceDictUid = dictUidArray[position]
        return choiceDictUid
    }

    fun changedUseDict(min:Int,max:Int){
        // 今選んでいる辞書のIDを変更する
        if (min == max){
            minArray.value = listOf(min)
            maxArray.value = listOf(max)
        }else{
            minArray.value = CommonTool.makeNumArray(min,max-1)
            maxArray.value = CommonTool.makeNumArray(max-1,max)
        }

    }

//    fun updateMaxData(min:Int){
//        maxArray.value = commonTool.makeNumArray(min+1,10)
//    }
}