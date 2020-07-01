package com.rapdict.takuro.rapdict.myDict

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Word
import kotlinx.coroutines.runBlocking

class DisplayListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var questionList = mutableListOf<MutableLiveData<String>>()
    var furiganaList = mutableListOf<MutableLiveData<String>>()

    init {

    }

    fun clear(){
        questionList.clear()
        furiganaList.clear()
    }

    fun mydict_bind(uid:Int){
        var data =  listOf<Word>()
        runBlocking {
            val dao = db.wordDao()
            data = dao.findByDictIds(uid)
        }
        bind_word(data)
    }

    fun bind_word(list:List<Word>){
        list.forEach {
            questionList.add(MutableLiveData<String>().apply { value = it.word })
            furiganaList.add(MutableLiveData<String>().apply { value = it.furigana })
        }
    }
}