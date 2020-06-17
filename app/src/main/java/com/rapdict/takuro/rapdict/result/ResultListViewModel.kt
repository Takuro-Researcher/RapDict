package com.rapdict.takuro.rapdict.result

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.model.Answer

import sample.intent.AnswerData

class ResultListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var textList   = mutableListOf<MutableLiveData<String>>()
    var questionList = mutableListOf<MutableLiveData<String>>()
    var checkedList = mutableListOf<MutableLiveData<Boolean>>()


    fun draw(answerList:Array<Answer>) {
        answerList.forEach { answer ->
            questionList.add(MutableLiveData<String>().apply { value = answer.question })
            textList.add(MutableLiveData<String>().apply { value = answer.answer })
            checkedList.add(MutableLiveData<Boolean>().apply { value = false })
        }
    }

}