package com.rapdict.takuro.rapdict.game

import android.app.Application
import android.media.MediaPlayer
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Word

class GamePlayViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData


    private var questionNum: Int = 0
    private var words: List<Word>  = listOf()
    var question: MutableLiveData<Int> = MutableLiveData(1)
    private var _question:Int = 0
    var question_text: MutableLiveData<String> = MutableLiveData("")
    var furigana: String = ""
    var isFinish: MutableLiveData<Boolean> = MutableLiveData()

    fun arg_to_member(question_num:Int, wordList: List<Word>){
        questionNum =  question_num
        words = wordList
        question_text.value = words[_question].word ?: ""
        furigana = words[_question].furigana ?: ""
        _question += 1
        question.value = question_num - _question
    }

    fun changeQuestion(){
        if(questionNum == _question){
            isFinish.value = true
            return
        }
        question_text.value = words[_question].word ?: ""
        furigana = words[_question].furigana ?: ""
        _question += 1
        question.value = questionNum - _question
    }


}