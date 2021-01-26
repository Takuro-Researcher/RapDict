package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.SpfCommon
import kotlinx.coroutines.runBlocking

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    val barArray: List<Int> = listOf(2, 4, 8)
    var minArray: List<Int> = List(20) { it + 2 }
    var maxArray: List<Int> = List(20) { it + 2 }
    val questionArray: List<Int> = listOf(5, 10, 15, 20)
    var dictNameArray: List<String> = listOf()
    val beatTypeArray: List<String> = listOf("low", "middle", "high", "tri")
    var drumOnly = MutableLiveData<Boolean>(false)
    var bar = MutableLiveData<Int>()

    var min = MutableLiveData<Int>()

    // 3文字にしないとDBで取ってこれない可能性が高い
    var max = MutableLiveData<Int>(1)
    var question = MutableLiveData<Int>()
    var dictName = MutableLiveData<Int>()
    var beatType = MutableLiveData<Int>()
    private var dictValueArray: Map<Int, String> = mapOf()
    private var isUpdateMyDictBoolean: Boolean = false

    var settingData: GameSettingData = GameSettingData(2, "low", false, 2, 3, 5, -1)

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }

    private fun loadUserData() {
        val settingTmp = SpfCommon(PreferenceManager.getDefaultSharedPreferences(getApplication())).settingRead()
        runBlocking {
            val dictDao = db.mydictDao()
            val dicts = dictDao.findAll()
            val tmp: MutableMap<Int, String> = mutableMapOf<Int, String>(-1 to "日本語辞書")
            dicts.forEach {
                val name = it.name ?: ""
                val uid = it.uid
                tmp.put(uid, name)
            }
            dictNameArray = tmp.values.toMutableList()
            dictValueArray = tmp
        }
        if (settingTmp != null) {
            settingData = settingTmp
            bar.value = barArray.indexOf(settingData.bar)
            min.value = minArray.indexOf(settingData.min)
            max.value = maxArray.indexOf(settingData.max)
            question.value = questionArray.indexOf(settingData.question)
            beatType.value = beatTypeArray.indexOf(settingData.type)
            drumOnly.value = settingData.drumOnly
            dictName.value = dictNameArray.indexOf(dictValueArray[settingData.dictUid])
        }
    }

    fun isUpdateMyDict():Boolean{
        runBlocking {
            val wordDao = db.wordDao()
            val dicts = wordDao.countByDict()
            isUpdateMyDictBoolean = dicts.size == dictNameArray.size
        }
        return isUpdateMyDictBoolean
    }
    fun changeDictData(){
        runBlocking {
            val dictDao = db.mydictDao()
            val dicts = dictDao.findAll()
            val tmp: MutableMap<Int, String> = mutableMapOf<Int, String>(-1 to "日本語辞書")
            dicts.forEach {
                val name = it.name ?: ""
                val uid = it.uid
                tmp.put(uid, name)
            }
            dictNameArray = tmp.values.toMutableList()
            dictValueArray = tmp
        }
    }

    fun changeDrumOnly() {
        if (drumOnly.value == true) {
            drumOnly.value = false
            settingData.drumOnly = false
        } else {
            drumOnly.value = true
            settingData.drumOnly = true
        }
    }

    // Spinnerの変更を検知する
    fun changeBar(position: Int) {
        settingData.bar = barArray[position]
    }

    fun changeMax(position: Int) {
        settingData.max = maxArray[position]
    }

    fun changeMin(position: Int) {
        settingData.min = minArray[position]
    }

    fun changeQuestion(position: Int) {
        settingData.question = questionArray[position]
    }

    fun changeBeatType(position: Int) {
        settingData.type = beatTypeArray[position]
    }

    fun changeDict(position: Int) {
        val dictData = dictValueArray.filterValues { value -> value == dictNameArray[position] }
        settingData.dictUid = dictData.keys.toList()[0]
    }

}