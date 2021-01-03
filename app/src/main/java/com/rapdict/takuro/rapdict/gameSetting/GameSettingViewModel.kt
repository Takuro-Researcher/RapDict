package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Common.SpfCommon
import kotlinx.coroutines.runBlocking

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    val barArray:List<Int> = listOf(2,4,8)
    var minArray:List<Int> = List(20){it}
    var maxArray:List<Int> = List(20){it}
    val questionArray:List<Int> = listOf(5,10,15,20)
    var dictNameArray:List<String> = listOf()
    val beatTypeArray:List<String> = listOf("low","middle","high","tri")
    var drumOnly = MutableLiveData<Boolean>(false)
    var bar = MutableLiveData<Int>()
    var min = MutableLiveData<Int>()
    var max = MutableLiveData<Int>()
    var question = MutableLiveData<Int>()
    var dictName = MutableLiveData<Int>()
    var beatType = MutableLiveData<Int>()
    private var dictValueArray:Map<Int,String> = mapOf()

    val settingData :GameSettingData = GameSettingData(2,"low",false,0,1,0,-1)

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }

    private fun loadUserData(){
        val settingData = SpfCommon(PreferenceManager.getDefaultSharedPreferences(getApplication())).settingRead()
        runBlocking {
            val dictDao = db.mydictDao()
            val wordDao = db.wordDao()
            val dicts = wordDao.countByDict()
            val tmp:MutableMap<Int, String> = mutableMapOf<Int, String>(-1 to "日本語辞書")
            dicts.forEach {
                val dict_data = dictDao.findOneByIds(it.dictid)
                val name = dict_data.name?: ""
                val uid = dict_data.uid
                tmp.put(uid, name)
            }
            dictNameArray = tmp.values.toMutableList()
            dictValueArray = tmp
        }

        // TODO DBの状態を見た上で、Spinnerに反映すべき値を辞書で作成する


        // TODO　該当インデックスを、ViewModelのパブリックメンバにインデックスを渡す処理
        if(settingData != null) {
            bar.value = barArray.indexOf(settingData.bar)
            min.value = minArray.indexOf(settingData.min)
            max.value = maxArray.indexOf(settingData.max)
            question.value = questionArray.indexOf(settingData.question)
            beatType.value = beatTypeArray.indexOf(settingData.type)
            drumOnly.value = settingData.drumOnly
            dictName.value = dictNameArray.indexOf(dictValueArray[settingData.dictUid])
        }


    }

    fun makeGameSettingData():GameSettingData{
        val data: GameSettingData = settingData
        return data
    }


    // TODO　これもイベントのオブザーバルで変更する
    fun changeDrumOnly(){
        if (drumOnly.value == true){
            drumOnly.value = false
            settingData.drumOnly = false
        }else{
            drumOnly.value = true
            settingData.drumOnly = true
        }
    }
    // TODO　これもイベントのオブザーバルで変更する
    // 使う辞書に応じて、最小と最大を変更する
    fun changeUseDictMinMax(uid:Int){
        var minMax = getMinMaxMyDict(uid)
        var min = minMax.first
        var max = minMax.second
        // 今選んでいる辞書のIDを変更する
//        if (min == max){
//            minArray.value = listOf(min)
//            maxArray.value = listOf(max)
//        }else{
//            minArray.value = CommonTool.makeNumArray(min,max)
//            maxArray.value = CommonTool.makeNumArray(min,max)
//        }
    }

    fun getMinMaxMyDict(uid:Int):Pair<Int,Int>{
        var min = 3
        var max = 12
        if (uid != -1){
            runBlocking {
                val dao = db.wordDao()
                min = dao.findByDictIdsMin(uid)
                max = dao.findByDictIdsMax(uid)
            }
        }
        return  Pair(min,max)
    }
}