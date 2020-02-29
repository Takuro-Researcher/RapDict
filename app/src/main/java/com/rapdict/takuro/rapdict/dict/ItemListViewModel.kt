package com.rapdict.takuro.rapdict.dict

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import com.rapdict.takuro.rapdict.helper.WordAccess
import java.text.FieldPosition

class ItemListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var rawList   = mutableListOf<MutableLiveData<String>>()
    var rhymeList = mutableListOf<MutableLiveData<String>>()
    var idList = mutableListOf<MutableLiveData<Int>>()
    var colorList = mutableListOf<MutableLiveData<Int>>()

    private var db: SQLiteDatabase? = null
    private var helper: SQLiteOpenHelper? = null
    private val wordAccess = WordAccess()

    //ViewModel初期化時にロード
    init {
        loadAnswerData()
    }
    private fun loadAnswerData(){
        helper = SQLiteOpenHelper(getApplication())
        db = helper!!.writableDatabase
        val answerList:ArrayList<AnswerView> = wordAccess.getAnswers(db!!,0,30,2)
        bindAnswer(answerList)
    }

    fun bindAnswer(answerList:ArrayList<AnswerView>) {
        clearAnswer()
        answerList.forEach { answer ->
            idList.add(MutableLiveData<Int>().apply { value = answer.answerview_id})
            rawList.add(MutableLiveData<String>().apply { value = answer.question })
            rhymeList.add(MutableLiveData<String>().apply { value = answer.answer })
            colorList.add(MutableLiveData<Int>().apply { value = AnswerView.favo2background(answer.favorite!!) })
        }
    }
    // 削除処理時にLiveData
    fun removeAnswer(position: Int){
        idList.removeAt(position)
        rawList.removeAt(position)
        rhymeList.removeAt(position)
        colorList.removeAt(position)
    }

    private fun clearAnswer(){
        idList.clear()
        rawList.clear()
        rhymeList.clear()
        colorList.clear()
    }
}