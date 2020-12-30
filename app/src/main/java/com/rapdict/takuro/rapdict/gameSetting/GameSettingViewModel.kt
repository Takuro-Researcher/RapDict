package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Common.SpfCommon
import kotlinx.coroutines.runBlocking

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    val barArray: MutableLiveData<List<Int>> = MutableLiveData(listOf(2,4,8))
    var minArray: MutableLiveData<List<Int>> = MutableLiveData()
    var maxArray: MutableLiveData<List<Int>> = MutableLiveData()
    val questionArray: MutableLiveData<List<Int>> = MutableLiveData(listOf(5,10,20,30))
    var dictNameArray: MutableLiveData<List<String>> = MutableLiveData()
    val beatTypeArray:MutableLiveData<List<String>> = MutableLiveData(listOf("low","middle","high","tei"))
    var drumOnly:MutableLiveData<Boolean> = MutableLiveData()
    var settingData :GameSettingData = GameSettingData(2,"low",false,0,1,0,-1)
    val num: Int = 2

    val bar: Int = 0
    val min: Int = 0
    val max: Int = 0
    val question: Int  = 0
    val dictName: Int = 0
    val beatType: Int = 0


    var dictUidArray= listOf<Int>()
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
            val tmp = mutableListOf<String>("日本語辞書")
            dicts.forEach {
                val dict_data = dictDao.findOneByIds(it.dictid).name ?: ""
                tmp.add(dict_data)
            }
            dictNameArray.value = tmp
        }




        drumOnly.value = false

        // TODO DBの状態を見た上で、Spinnerに反映すべき値を辞書で作成する
        // TODO 主に見るべきは辞書が何種類登場するか
        // １，　辞書リスト獲得　２．SettingDataから辞書リストの該当辞書を確認　３．既にあるかを確認し、なければ終わらす


        // TODO　該当インデックスを、ViewModelのパブリックメンバにインデックスを渡す処理
        if(settingData != null) {

        }

    }

    fun makeGameSettingData():GameSettingData{
        val data: GameSettingData = settingData
        return data
    }

    fun changeUseDict(position: Int):Int{
        settingData.dictUid =dictUidArray[position]
        return dictUidArray[position]
    }

    // TODO　これもイベントのオブザーバルで変更する
    fun changeDrumOnly(){
        if (drumOnly.value ==true){
            drumOnly.value = false
            settingData.drumOnly = false
        }else{
            drumOnly.value = true
            settingData.drumOnly = true
        }
    }
    // TODO　これもイベントのオブザーバルで変更する
    fun changeUseDictMinMax(uid:Int){
        var minMax = getMinMaxMyDict(uid)
        var min = minMax.first
        var max = minMax.second
        // 今選んでいる辞書のIDを変更する
        if (min == max){
            minArray.value = listOf(min)
            maxArray.value = listOf(max)
        }else{
            minArray.value = CommonTool.makeNumArray(min,max)
            maxArray.value = CommonTool.makeNumArray(min,max)
        }

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