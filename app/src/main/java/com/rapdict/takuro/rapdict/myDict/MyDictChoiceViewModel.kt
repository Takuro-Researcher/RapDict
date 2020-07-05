package com.rapdict.takuro.rapdict.myDict

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.database.Mydict
import kotlinx.coroutines.runBlocking

class MyDictChoiceViewModel (application: Application) : AndroidViewModel(application) {
    //監視対象のLiveData
    var dictNameList :MutableLiveData<List<String>> = MutableLiveData()
    var uidList = mutableListOf<MutableLiveData<Int>> ()
    var db_uid = MutableLiveData<Int>()
    var count = MutableLiveData<String>()
    var text10overVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        count.value = "0"
    }
    fun init_load(){
        var data = listOf<Mydict>()
        runBlocking {
            val dao = App.db.mydictDao()
            data = dao.findAll()
        }
        loadDictName(data)
    }
    // 選択辞書変更時。
    fun changed_uid(position: Int){
        db_uid.value = uidList[position].value
    }

    fun loadDictName(array:List<Mydict>){
        val list = mutableListOf<String>()
        array.forEach {
            list.add(it.name!!)
            uidList.add(MutableLiveData<Int>().apply { value =it.uid })
        }
        db_uid.value = array[0].uid
        countChange()
        dictNameList.value = list.toList()
    }

    fun countChange(){
        var count_data =0
        runBlocking {
            val dao = db.wordDao()
            count_data = dao.countByDictIds(db_uid.value!!)
        }
        count.value = count_data.toString()
        if (count_data<10){
            text10overVisibility.value= View.VISIBLE
        }else{
            text10overVisibility.value = View.GONE
        }

    }
}