package com.rapdict.takuro.rapdict.myDict

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class DisplayListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var questionList = mutableListOf<MutableLiveData<String>>()
    var furiganaList = mutableListOf<MutableLiveData<String>>()

    init {
        questionList.add(MutableLiveData<String>().apply { value = "試験中" })
        furiganaList.add(MutableLiveData<String>().apply { value = "しけんちゅう" })
    }

    fun addCard() {
        questionList.add(MutableLiveData<String>().apply { value = "" })
        furiganaList.add(MutableLiveData<String>().apply { value = "" })
    }

}