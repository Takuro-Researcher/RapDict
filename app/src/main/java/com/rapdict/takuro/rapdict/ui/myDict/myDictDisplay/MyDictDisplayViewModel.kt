package com.rapdict.takuro.rapdict.ui.myDict.myDictDisplay

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rapdict.takuro.rapdict.model.entity.Word
import com.rapdict.takuro.rapdict.model.repository.WordRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class MyDictDisplayWordData(
        val id: Long,
        val question: String = "",
        val furigana: String = "",
        val uid: Int,
        val isDelete: MutableLiveData<Boolean> = MutableLiveData(false)
) {
    fun delete(): Boolean {
        isDelete.value = true
        return true
    }
}

class MyDictDisplayViewModel(application: Application) : AndroidViewModel(application) {

    private var myDictDisplayWordsRaw = mutableListOf<MyDictDisplayWordData>()
    private var _myDictDisplayWords = MutableLiveData<MutableList<MyDictDisplayWordData>>()
    private var initWords = mutableListOf<MyDictDisplayWordData>()
    private var index = 0L
    
    val myDictDisplayWords: LiveData<MutableList<MyDictDisplayWordData>> = _myDictDisplayWords

    // データ参照用のRepositoryクラス
    private val _wordRepository = WordRepository(application)

    fun bindData(uid: Int) {
        var data = listOf<Word>()
        myDictDisplayWordsRaw = mutableListOf()
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

    fun delete(position: MyDictDisplayWordData): Boolean {
        myDictDisplayWordsRaw.remove(position)
        val uid = position.uid
        _myDictDisplayWords.value = ArrayList(myDictDisplayWordsRaw)
        viewModelScope.launch {
            _wordRepository.removeWords(uid)
        }
        return true
    }
}