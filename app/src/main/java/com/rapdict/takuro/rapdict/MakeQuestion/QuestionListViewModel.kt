package com.rapdict.takuro.rapdict.MakeQuestion

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import sample.intent.AnswerData

class QuestionListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var questionList = mutableListOf<MutableLiveData<String>>()
    var furiganaList = mutableListOf<MutableLiveData<String>>()



    fun draw(answerList:Array<AnswerData>) {

    }

}