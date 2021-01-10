package com.rapdict.takuro.rapdict.result



import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.rapdict.takuro.rapdict.Word

data class AnswerData(
        val id: Long,
        val checked_id: MutableLiveData<Int> = MutableLiveData(0),
        val isChecked: MutableLiveData<Boolean> = MutableLiveData(false),
        val answer: MutableLiveData<String> = MutableLiveData(""),
        val isAdd: Boolean = false
) {}

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val answersRaw = mutableListOf<AnswerData>()
    private var _answers = MutableLiveData<MutableList<AnswerData>>()

    // ゲーム画面で記録したものを参照し、RecyclerView用に再編集する。
    val answers: LiveData<MutableList<AnswerData>> = _answers
    // フォームへの表示用
    var words_texts : List<String> = listOf()
    // 実際にデータの保存用に使う
    private var words: List<Word> = listOf()

    // カードの追加可能か
    var addAble: MutableLiveData<Boolean> = MutableLiveData()
    var addCardCount:MutableLiveData<Int> = MutableLiveData()
    //ViewModel初期化時にロード
    init {
        addAble.value = true
        addCardCount.value = 0
    }
    fun addAbleCheck():Boolean{
        addCardCount.value = addCardCount.value?.plus(1)
        if(addCardCount.value!! >= 5 ){
            addAble.value = false
            return false
        }
        return true
    }
    // 受け取った答え用に変更する。
    fun initializeAnswerWord(ans: Map<Int, String>, words:List<Word>) {
        ans.forEach { answersRaw.add(AnswerData(checked_id =  MutableLiveData(it.key), answer = MutableLiveData(it.value))) }
        _answers.value = ArrayList(answersRaw)
        words_texts = words.map { it.word?: "" }
    }

    fun addAnswers() {
        answersRaw.add(AnswerData(isAdd = true))
        _answers.value = ArrayList(answersRaw)
    }
}