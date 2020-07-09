package com.rapdict.takuro.rapdict.result



import android.app.Application
import android.view.View

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import com.rapdict.takuro.rapdict.R

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var mainMessage: MutableLiveData<String> = MutableLiveData()
    var subMessage: MutableLiveData<String> = MutableLiveData()
    var visibleSaveButton: MutableLiveData<Int> = MutableLiveData()

    //ViewModel初期化時にロード
    init {
        mainMessage.value = application.getString(R.string.result_header)
        subMessage.value = application.getString(R.string.result_description)
        visibleSaveButton.value = View.VISIBLE
    }

    fun draw(main:String,sub:String){
        mainMessage.value = main
        subMessage.value = sub
        visibleSaveButton.value = View.GONE
    }
}