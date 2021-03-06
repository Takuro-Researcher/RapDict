package com.rapdict.takuro.rapdict.myDict.myDictChoice

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.Repository.MyDictRepository
import com.rapdict.takuro.rapdict.Repository.WordRepository
import com.rapdict.takuro.rapdict.database.Mydict
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyDictChoiceViewModel (application: Application) : AndroidViewModel(application) {
    //監視対象のLiveData


    var dbUid = MutableLiveData<Int>()
    var dictCount: MutableLiveData<Int> = MutableLiveData()
    var useDict: String = ""

    // 必要なもの。辞書の名前たち。UIDの名前たち。現在選択されているUID(MutableLiveData)、表示用のカウント
    private lateinit var uidList : List<Int>

    var dictNameList:MutableLiveData<List<String>> = MutableLiveData()

    // Repository
    private val _wordRepository: WordRepository = WordRepository(application)
    private val _myDictRepository: MyDictRepository = MyDictRepository(application)

    init {
        initLoad()
    }

    // データの描画、画面移動時にも行う必要がある。
    fun initLoad(){
        var dicts: List<Mydict> = listOf()
        runBlocking {
            dicts = _myDictRepository.getDictAll()
        }
        // 辞書が存在した場合の処理を行う。
        if(dicts.isNotEmpty()){
            loadDictName(dicts)
        }
    }

    // 辞書を一つ削除
    fun removeMyDict(){
        GlobalScope.launch {
            _myDictRepository.removeuid2MyDict(dbUid.value!!)
        }
        val spf = PreferenceManager.getDefaultSharedPreferences(getApplication())
        val spfCommon:SpfCommon = SpfCommon(spf)
        val settingData = spfCommon.settingRead()
        // 現在のゲーム設定データに、セーブデータが存在していたら削除する。
        if(settingData != null){
            if(settingData.dictUid == dbUid.value){
                settingData.dictUid = -1
                spfCommon.settingSave(settingData)
            }
        }
    }

    // 選択辞書変更
    fun changeUid(position: Int){
        useDict = dictNameList.value!![position]
        dbUid.value = uidList[position]
    }

    //　辞書データから、必要なMutableLiveDataに再変換
    private fun loadDictName(array: List<Mydict>){
        val nameList = mutableListOf<String>()
        val uidData = mutableListOf<Int>()
        array.forEach {
            nameList.add(it.name!!)
            uidData.add(it.uid)
        }
        uidList = uidData.toList()
        dictNameList.value = nameList.toList()
        changeUid(0)
    }

    // 選択された辞書の単語数をカウントする
    fun countChange(){
        runBlocking {
            dictCount.value = _wordRepository.countWords(dbUid.value!!)
        }
    }
}