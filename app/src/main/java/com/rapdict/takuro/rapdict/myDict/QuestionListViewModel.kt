package com.rapdict.takuro.rapdict.myDict

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class QuestionListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var questionList = mutableListOf<MutableLiveData<String>>()
    var furiganaList = mutableListOf<MutableLiveData<String>>()

    init {
        questionList.add(MutableLiveData<String>().apply { value = "" })
        furiganaList.add(MutableLiveData<String>().apply { value = "" })
    }

    fun addCard() {
        questionList.add(MutableLiveData<String>().apply { value = "" })
        furiganaList.add(MutableLiveData<String>().apply { value = "" })
    }

    fun clear(){
        for (i in questionList.indices) {
            questionList[i].value = ""
            furiganaList[i].value = ""
        }
    }

}