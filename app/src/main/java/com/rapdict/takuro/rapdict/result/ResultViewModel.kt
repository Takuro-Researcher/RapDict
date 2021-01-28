package com.rapdict.takuro.rapdict.result


import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rapdict.takuro.rapdict.Repository.AnswerRepository

import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.database.Answer
import kotlinx.coroutines.launch

data class AnswerData(
        val id: Long,
        val checked_id: MutableLiveData<Int> = MutableLiveData(0),
        val isChecked: MutableLiveData<Boolean> = MutableLiveData(false),
        val answer: MutableLiveData<String> = MutableLiveData(""),
        val isAdd: Boolean = false
) {
    fun changeCheckedId(position: Int) {
        checked_id.value = position
    }
}

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val answersRaw = mutableListOf<AnswerData>()
    private var _answers = MutableLiveData<MutableList<AnswerData>>()
    private var index = 0L

    // ゲーム画面で記録したものを参照し、RecyclerView用に再編集する。
    val answers: LiveData<MutableList<AnswerData>> = _answers

    // フォームへの表示用
    var wordsTexts: List<String> = listOf()

    // 実際にデータの保存用に使う
    private var words: List<Word> = listOf()

    // カードの追加可能か
    var addAble: MutableLiveData<Boolean> = MutableLiveData()
    var addCardCount: MutableLiveData<Int> = MutableLiveData()

    // データ参照用のRepositoryクラス
    private val _answerRepository = AnswerRepository(application)

    //ViewModel初期化時にロード
    init {
        addAble.value = true
        addCardCount.value = 0
    }

    fun addAbleCheck(): Boolean {
        addCardCount.value = addCardCount.value?.plus(1)
        if (addCardCount.value!! >= 5) {
            addAble.value = false
            return false
        }
        return true
    }

    // 受け取った答え用に変更する。
    fun initializeAnswerWord(ans: Map<Int, String>, words: List<Word>) {
        ans.forEach { answersRaw.add(AnswerData(id = index, checked_id = MutableLiveData(it.key), answer = MutableLiveData(it.value))) }
        _answers.value = ArrayList(answersRaw)
        wordsTexts = words.map { it.word ?: "" }
        index += 1
    }

    // 新たに韻を思いついた人のために、韻を追加するプログラム
    fun addAnswers() {
        answersRaw.add(AnswerData(id = index, isAdd = true))
        _answers.value = ArrayList(answersRaw)
        index += 1
    }

    // 韻を保存する
    fun saveAnswers(){
        val answerList: ArrayList<Answer> = ArrayList()
        _answers.value?.forEach {
            val isChecked = it.isChecked.value ?: false
            if (isChecked){
                // 保存を行うための処理
                val textAnswer = it.answer.value ?: ""
                val checkedId = it.checked_id.value ?: 0
                val answer = Answer(0,
                        answer = textAnswer,
                        answerLen = textAnswer.length,
                        question = wordsTexts[checkedId],
                        favorite = 0)
                if (textAnswer.isNotEmpty()) {
                    answerList.add(answer)
                }
            }
        }
        // DBに保存する
        viewModelScope.launch {
            _answerRepository.saveAnswer(answerList)
        }
    }

    // チェックしてある答えの数を返す
    fun getCheckAnswerNum():Int{
        var counter = 0
        _answers.value?.forEach {
            val isChecked = it.isChecked.value ?: false
            if (isChecked){ counter += 1 }
        }
        return counter
    }

}