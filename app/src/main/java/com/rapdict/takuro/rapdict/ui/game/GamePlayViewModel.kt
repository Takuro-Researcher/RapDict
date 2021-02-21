package com.rapdict.takuro.rapdict.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rapdict.takuro.rapdict.model.entity.Word


class GamePlayViewModel(val questionWords: List<Word>,
                        private val questionNumber: Int) : ViewModel() {

    //監視対象のLiveData


    private var _questionIndex: Int = 0
    private val _remainQuestion: MutableLiveData<Int> = MutableLiveData()
    private val _word: MutableLiveData<String> = MutableLiveData("")
    private val _furigana: MutableLiveData<String> = MutableLiveData("")
    private val _isFinish: MutableLiveData<Boolean> = MutableLiveData()


    val remainQuestion: LiveData<Int> = _remainQuestion
    val word: LiveData<String> = _word
    val furigana: LiveData<String> = _furigana
    val isFinish: LiveData<Boolean> = _isFinish
    val answer: MutableLiveData<String> = MutableLiveData()

    // 対象Wordのインデックスと文字列
    private var _answerMap: MutableMap<Int, String> = mutableMapOf()
    val answerMap: Map<Int, String>
        get() = _answerMap

    init {
        setWord()
    }

    // インデックスを合わせて問題を変更する。
    fun setWord() {
        questionWords[_questionIndex].run {
            _word.postValue(word)
            _furigana.postValue(furigana)
            val remainQuestionNUmber = questionWords.size - (_questionIndex + 1)
            _remainQuestion.postValue(remainQuestionNUmber)
        }
        _questionIndex += 1
    }

    // 今終わった問題の保存と問題の変更
    fun changeQuestion() {
        // 最終問題に到達した
        if (questionWords.size == _questionIndex) {
            _isFinish.value = true
            return
        }
        setWord()
    }

    // 入力された韻をViewModel内のMapに保存する
    fun saveAnswer() {
        val tmpAnswer = answer.value ?: ""
        if (tmpAnswer.isNotEmpty()) {
            _answerMap[_questionIndex - 1] = tmpAnswer
        }
    }

    //
    fun nextRhyme() {
        saveAnswer()
        changeQuestion()
        answer.postValue("")
    }
}