package com.rapdict.takuro.rapdict.result

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import sample.intent.AnswerData

class ResultListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var textList   = mutableListOf<MutableLiveData<String>>()
    var questionList = mutableListOf<MutableLiveData<String>>()
    var questionIdList = mutableListOf<MutableLiveData<Int>>()


    fun draw(answerList:Array<AnswerData>) {
        answerList.forEach { answer ->
            questionList.add(MutableLiveData<String>().apply { value = answer.question })
            textList.add(MutableLiveData<String>().apply { value = answer.answer })
            questionIdList.add(MutableLiveData<Int>().apply { value = answer.question_id })
        }
    }

}