package com.rapdict.takuro.rapdict.gameSetting

import android.app.Application
import android.content.SharedPreferences
import android.mtp.MtpConstants
import android.preference.PreferenceManager
import androidx.databinding.Bindable

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.database.Mydict
import kotlinx.coroutines.runBlocking
import java.text.FieldPosition
import kotlin.math.min

class GameSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var barArray: MutableLiveData<List<Int>> = MutableLiveData()
    var minArray: MutableLiveData<List<Int>> = MutableLiveData()
    var maxArray: MutableLiveData<List<Int>> = MutableLiveData()
    var questionArray: MutableLiveData<List<Int>> = MutableLiveData()
    var dictNameArray: MutableLiveData<List<String>> = MutableLiveData()
    var beatTypeArray:MutableLiveData<List<String>> = MutableLiveData()
    var drumOnly:MutableLiveData<Boolean> = MutableLiveData()
    var src_data:MutableLiveData<Int> = MutableLiveData()

    var settingData :GameSettingData = GameSettingData(2,"low",false,0,1,0,-1)

    var dictUidArray= listOf<Int>()
    //ViewModel初期化時にロード
    init {
        loadUserData()
    }

    private fun loadUserData(){
        val spfCommon = SpfCommon(PreferenceManager.getDefaultSharedPreferences(getApplication()))
        val settingData = spfCommon.settingRead()
        var initbarArray= mutableListOf<Int>()
        var initQuestionArray = mutableListOf<Int>()
        var initBeatTypeArray = mutableListOf<String>()
        var initNameArray = mutableListOf<String>()
        val initMinArray= mutableListOf<Int>()
        val initMaxArray= mutableListOf<Int>()
        var initUidArray = mutableListOf<Int>()
        drumOnly.value = false
        System.out.println(settingData)
        // Spfで既に遊んだ記録があったら
        if(settingData != null) {
            // 一番最初に選ばれるようにする
            initbarArray.add(settingData.bar)
            initQuestionArray.add(settingData.question)
            initBeatTypeArray.add(settingData.type)
            initMinArray.add(settingData.min)
            initMaxArray.add(settingData.max)
            drumOnly.value = settingData.drumOnly

            //　自分で作成した辞書を使っていた場合。
            if(settingData.dictUid != -1) {
                var choicedDict: Mydict? = null
                runBlocking {
                    val dictdao = db.mydictDao()
                    choicedDict = dictdao.findOneByIds(settingData.dictUid)
                }
                // 辞書が削除されていないとき
                if (choicedDict != null) {
                    initNameArray.add(choicedDict!!.name!!)
                    initUidArray.add(choicedDict!!.uid)
                    var minMax = getMinMaxMyDict(settingData.dictUid)
                    initMinArray.addAll(CommonTool.makeNumArray(minMax.first,minMax.second,1))
                    initMaxArray.addAll(CommonTool.makeNumArray(minMax.first,minMax.second,1))
                }
            //日本語辞書を使っていた場合
            }else{
                initMinArray.addAll(CommonTool.makeNumArray(3,11,1))
                initMaxArray.addAll(CommonTool.makeNumArray(3,11,1))
            }
        // まだ一度もゲームをしていなかった場合
        }else{
            initMinArray.addAll(CommonTool.makeNumArray(3,11,1))
            initMaxArray.addAll(CommonTool.makeNumArray(3,11,1))
        }
        //日本語辞書を追加
        initNameArray.add("日本語辞書")
        initUidArray.add(-1)

        barArray.value = initbarArray.plus(listOf(2,4,8)).distinct()
        questionArray.value = initQuestionArray.plus(CommonTool.makeNumArray(10,30,10)).distinct()
        beatTypeArray.value = initBeatTypeArray.plus(listOf("low","middle","high","tri")).distinct()
        minArray.value = initMinArray.distinct()
        maxArray.value = initMaxArray.distinct()

        runBlocking {
            val dictDao = db.mydictDao()
            val wordDao = db.wordDao()
            val data =wordDao.countByDict()
            val dictUids = mutableListOf<Int>()
            data.forEach {
                dictUids.add(it.dictid)
            }
            val dictData = dictDao.findByIds(dictUids)
            dictData.forEach {
                initNameArray.add(it.name!!)
                initUidArray.add(it.uid)
            }
        }
        dictUidArray = initUidArray.distinct()
        dictNameArray.value = initNameArray.distinct()
        settingDataInit()
    }

    fun makeGameSettingData():GameSettingData{
        val data: GameSettingData = settingData
        return data
    }

    fun changeUseDict(position: Int):Int{
        settingData.dictUid =dictUidArray[position]
        return dictUidArray[position]
    }


    fun changeDrumOnly(){
        if (drumOnly.value ==true){
            drumOnly.value = false
            settingData.drumOnly = false
        }else{
            drumOnly.value = true
            settingData.drumOnly = true
        }
    }
    // 辞書のIDを与え、minMaxを変更する
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
        var max = 11
        if (uid != -1){
            runBlocking {
                val dao = db.wordDao()
                min = dao.findByDictIdsMin(uid)
                max = dao.findByDictIdsMax(uid)
            }
        }
        return  Pair(min,max)
    }

    fun settingDataInit(){
        settingData.bar = barArray.value!![0]
        settingData.type = beatTypeArray.value!![0]
        settingData.dictUid = dictUidArray[0]
        settingData.min =minArray.value!![0]
        settingData.max =maxArray.value!![0]
        settingData.question = questionArray.value!![0]
        settingData.drumOnly = drumOnly.value!!
    }

//    fun updateMaxData(min:Int){
//        maxArray.value = commonTool.makeNumArray(min+1,10)
//    }
}