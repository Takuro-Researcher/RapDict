package com.rapdict.takuro.rapdict.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Word

class GamePlayViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    private var questionNum: Int = 0
    val question: MutableLiveData<Int> = MutableLiveData(1)
    private var _question: Int = 0
    var question_text: MutableLiveData<String> = MutableLiveData("")
    var furigana: String = ""
    var isFinish: MutableLiveData<Boolean> = MutableLiveData()
    var answer: MutableLiveData<String> = MutableLiveData()
    private var _answerMap:MutableMap<Int, String> = mutableMapOf()
    var words: List<Word> = listOf()
    val answerMap: MutableMap<Int, String>
        get() = _answerMap

    fun arg_to_member(question_num: Int, wordList: List<Word>) {
        questionNum = question_num
        words = wordList
        question_text.value = words[_question].word ?: ""
        furigana = words[_question].furigana ?: ""
        _question += 1
        question.value = question_num - _question
    }

    // 今終わった問題の保存と問題の変更
    fun changeQuestion() {
        if (questionNum == _question) {
            isFinish.value = true
            return
        }
        question_text.value = words[_question].word ?: ""
        furigana = words[_question].furigana ?: ""
        _question += 1
        question.value = questionNum - _question
    }

    fun saveAnswer() {
        val tmpAnswer = answer.value ?: ""
        if (tmpAnswer.isNotEmpty()) {
            _answerMap[_question -1] = tmpAnswer
        }
        System.out.println(_answerMap)
    }


}