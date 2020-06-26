package com.rapdict.takuro.rapdict.result

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.model.Answer

import sample.intent.AnswerData

class ResultListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var textList   = mutableListOf<MutableLiveData<String>>()
    var questionList = mutableListOf<MutableLiveData<String>>()
    var checkedList = mutableListOf<MutableLiveData<Boolean>>()
    var wordTextArray: MutableLiveData<ArrayList<String>> = MutableLiveData()


    fun draw(answerList:ArrayList<Answer>,wordList:Array<Word>) {
        answerList.forEach { answer ->
            questionList.add(MutableLiveData<String>().apply { value = answer.question })
            textList.add(MutableLiveData<String>().apply { value = answer.answer })
            checkedList.add(MutableLiveData<Boolean>().apply { value = false })
        }
        var arraytextList = ArrayList<String>()
        wordList.forEach {
            arraytextList.add(it.word)
        }
        wordTextArray.value = arraytextList
    }

    fun addCard() {
        questionList.add(MutableLiveData<String>().apply { value = "" })
        textList.add(MutableLiveData<String>().apply { value = "" })
        checkedList.add(MutableLiveData<Boolean>().apply { value = false })
    }

}