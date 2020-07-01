package com.rapdict.takuro.rapdict.myDict

import android.app.Application
import android.view.View

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

import com.rapdict.takuro.rapdict.R

class MyDictMakeViewModel(application: Application) : AndroidViewModel(application) {
    //監視対象のLiveData
    var dictName: MutableLiveData<String> = MutableLiveData()

    fun isValid() =!dictName.value.isNullOrBlank()
    // addSource に変更検知する。
    val canSubmit = MediatorLiveData<Boolean>().also { result ->
        result.addSource(dictName) { result.value = isValid() }
    }
}