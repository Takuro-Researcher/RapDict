package com.rapdict.takuro.rapdict.myDict.myDictDisplay

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Repository.WordRepository
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.myDict.myDictMakeQuestion.MyDictMakeWordData
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.FieldPosition

data class MyDictDisplayWordData(
        val id: Long,
        val question: String = "",
        val furigana: String = "",
        val uid: Int
)

class MyDictDisplayViewModel(application: Application) : AndroidViewModel(application) {

    private var myDictDisplayWordsRaw = mutableListOf<MyDictDisplayWordData>()
    private var _myDictDisplayWords = MutableLiveData<MutableList<MyDictDisplayWordData>>()
    private var initWords = mutableListOf<MyDictDisplayWordData>()
    private var index = 0L


    val myDictDisplayWords: LiveData<MutableList<MyDictDisplayWordData>> = _myDictDisplayWords

    // データ参照用のRepositoryクラス
    private val _wordRepository = WordRepository(application)

    fun bindData(uid:Int){
        var data = listOf<Word>()
        runBlocking {
            data = _wordRepository.getWords(uid)
        }
        data.forEach {
            val word = it.word ?: ""
            val furigana = it.furigana ?: ""
            val uid = it.uid
            val wordData = MyDictDisplayWordData(index, word, furigana, uid)
            myDictDisplayWordsRaw.add(wordData)
            index += 1
        }
        _myDictDisplayWords.value = ArrayList(myDictDisplayWordsRaw)
        initWords = ArrayList(myDictDisplayWordsRaw)
    }

    fun delete(position: MyDictDisplayWordData): Boolean{
        myDictDisplayWordsRaw.remove(position)
        val uid = position.uid
        _myDictDisplayWords.value = ArrayList(myDictDisplayWordsRaw)
        viewModelScope.launch {
            _wordRepository.removeWords(uid)
        }
        return true
    }
}