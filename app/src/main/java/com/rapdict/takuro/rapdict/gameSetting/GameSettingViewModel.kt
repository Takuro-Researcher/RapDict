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
    var minArray: List<Int> = List(20) { it + 1 }
    var maxArray: List<Int> = List(20) { it + 1 }
    val questionArray: List<Int> = listOf(5, 10, 15, 20)
    var dictNameArray: List<String> = listOf()
    val beatTypeArray: List<String> = listOf("low", "middle", "high", "tri")
    var drumOnly = MutableLiveData<Boolean>(false)
    var bar = MutableLiveData<Int>()
    var min = MutableLiveData<Int>()
    var max = MutableLiveData<Int>()
    var question = MutableLiveData<Int>()
    var dictName = MutableLiveData<Int>()
    var beatType = MutableLiveData<Int>()
    private var dictValueArray: Map<Int, String> = mapOf()

    val settingData: GameSettingData = GameSettingData(2, "low", false, 0, 1, 0, -1)

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }

    private fun loadUserData() {
        val settingData = SpfCommon(PreferenceManager.getDefaultSharedPreferences(getApplication())).settingRead()
        runBlocking {
            val dictDao = db.mydictDao()
            val wordDao = db.wordDao()
            val dicts = wordDao.countByDict()
            val tmp: MutableMap<Int, String> = mutableMapOf<Int, String>(-1 to "日本語辞書")
            dicts.forEach {
                val dict_data = dictDao.findOneByIds(it.dictid)
                val name = dict_data.name ?: ""
                val uid = dict_data.uid
                tmp.put(uid, name)
            }
            dictNameArray = tmp.values.toMutableList()
            dictValueArray = tmp
        }

        // TODO DBの状態を見た上で、Spinnerに反映すべき値を辞書で作成する


        // TODO　該当インデックスを、ViewModelのパブリックメンバにインデックスを渡す処理
        if (settingData != null) {
            bar.value = barArray.indexOf(settingData.bar)
            min.value = minArray.indexOf(settingData.min)
            max.value = maxArray.indexOf(settingData.max)
            question.value = questionArray.indexOf(settingData.question)
            beatType.value = beatTypeArray.indexOf(settingData.type)
            drumOnly.value = settingData.drumOnly
            dictName.value = dictNameArray.indexOf(dictValueArray[settingData.dictUid])
        }
    }

    // TODO　これもイベントのオブザーバルで変更する
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
        System.out.println(settingData.bar)
    }

    fun changeMax(position: Int) {
        settingData.max = maxArray[position]
        System.out.println(settingData.max)
    }

    fun changeMin(position: Int) {
        settingData.min = minArray[position]
        System.out.println(settingData.min)
    }

    fun changeQuestion(position: Int) {
        settingData.question = questionArray[position]
        System.out.println(settingData.question)
    }

    fun changeBeatType(position: Int) {
        settingData.type = beatTypeArray[position]
        System.out.println(settingData.type)

    }

    fun changeDict(position: Int) {
        val dictData = dictValueArray.filterValues { value -> value == dictNameArray[position] }
        settingData.dictUid = dictData.keys.toList()[0]
    }
}