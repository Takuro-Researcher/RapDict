package com.rapdict.takuro.rapdict.myDict.myDictMakeQuestion

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rapdict.takuro.rapdict.Repository.MyDictRepository
import com.rapdict.takuro.rapdict.Repository.WordRepository
import com.rapdict.takuro.rapdict.Word
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class MyDictMakeWordData(
        val id: Long,
        val question: MutableLiveData<String> = MutableLiveData(""),
        val furigana: MutableLiveData<String> = MutableLiveData("")
) {}


class MyDictMakeQuestionViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    private val myDictMakeWordsRaw = mutableListOf<MyDictMakeWordData>()
    private var _myDictMakeWords = MutableLiveData<MutableList<MyDictMakeWordData>>()


    private var index = 0L

    val myDictMakeWords: LiveData<MutableList<MyDictMakeWordData>> = _myDictMakeWords

    var registerWordsNum = MutableLiveData(0)

    // Dialogに見せるために使われる値
    var dbName: String = ""
    var isFuriganaEmpty: Boolean = false
    var isinCompleteFlag: Boolean = false

    // データ参照用のRepositoryクラス
    private val _myDictRepository = MyDictRepository(application)
    private val _wordRepository = WordRepository(application)


    init {
        addCard()
    }

    fun addCard() {
        myDictMakeWordsRaw.add(MyDictMakeWordData(id = index))
        _myDictMakeWords.value = ArrayList(myDictMakeWordsRaw)
        index += 1
    }

    fun registerQuestion(dictUid: Int) {
        val words: MutableList<Word> = mutableListOf()
        _myDictMakeWords.value?.forEach {
            var furigana = it.furigana.value ?: ""
            val question = it.question.value ?: ""
            val length = furigana.length
            var word: Word? = null
            // 質問が空の場合保存をしない。
            if (question.isNotEmpty()) {
                // フリガナだけが空の場合、本文をフリガナに書き換え保存する
                if (furigana.isEmpty()) {
                    furigana = question
                }
                word = Word(0, furigana, question, length, dictUid)
            }
            if (word != null) {
                words.add(word)
            }
        }
        registerWordsNum.value = words.size

        // DBに保存する
        runBlocking {
            _wordRepository.saveWords(words)
        }
    }

    fun updateStatus(uid: Int) {
        runBlocking {
            dbName = _myDictRepository.uid2dict(uid)?.name ?: ""
        }
        isFuriganaEmpty = false
        isinCompleteFlag = false
        _myDictMakeWords.value?.forEach {
            if (it.furigana.value!!.isEmpty()) {
                isFuriganaEmpty = true
            }
            if (it.question.value!!.isEmpty()) {
                isinCompleteFlag = true
            }
        }
    }

    // 表示する韻を全て削除し、一枚だけ足した初期状態に戻す
    fun clear() {
        myDictMakeWordsRaw.clear()
        index = 0L
        addCard()
        registerWordsNum.value = 0
    }

}