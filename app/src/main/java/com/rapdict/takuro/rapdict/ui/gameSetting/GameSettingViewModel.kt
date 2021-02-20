package com.rapdict.takuro.rapdict.ui.gameSetting

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.App.Companion.db
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.myDict.GameSettingData
import kotlinx.coroutines.runBlocking

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    val barArray: List<Int> = listOf(2, 4, 8)
    var minArray: List<Int> = List(20) { it + 2 }
    var maxArray: List<Int> = List(20) { it + 2 }
    val questionArray: List<Int> = listOf(5, 10, 15, 20)
    private var dictNameArrayRaw: List<String> = listOf()
    private var _dictNameArray = MutableLiveData<List<String>>()
    var dictNameArray = _dictNameArray


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
            dictNameArrayRaw = tmp.values.toList()
            dictValueArray = tmp
        }
        // MutableLiveData用
        _dictNameArray.value = ArrayList(dictNameArrayRaw)
        if (settingTmp != null) {
            settingData = settingTmp
            bar.value = barArray.indexOf(settingData.bar)
            min.value = minArray.indexOf(settingData.min)
            max.value = maxArray.indexOf(settingData.max)
            question.value = questionArray.indexOf(settingData.question)
            beatType.value = beatTypeArray.indexOf(settingData.type)
            drumOnly.value = settingData.drumOnly
            System.out.println(dictNameArrayRaw)
            dictName.value = dictNameArrayRaw.indexOf(dictValueArray[settingData.dictUid])
            System.out.println(dictName.value!!)
        }
    }

    fun isNotUpdateMyDict(): Boolean {
        var isNotUpdateMyDictBoolean: Boolean = false

        runBlocking {
            val dictDao = db.mydictDao()
            val dicts = dictDao.findAll()
            isNotUpdateMyDictBoolean = dicts.size + 1 == dictNameArrayRaw.size
        }
        return isNotUpdateMyDictBoolean
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
            dictNameArrayRaw = tmp.values.toList()
            dictValueArray = tmp
            _dictNameArray.value = ArrayList(dictNameArrayRaw)
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
        val dictData = dictValueArray.filterValues { value -> value == dictNameArrayRaw[position] }
        settingData.dictUid = dictData.keys.toList()[0]
    }

}