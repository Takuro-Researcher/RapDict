package com.rapdict.takuro.rapdict.dict

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import com.rapdict.takuro.rapdict.helper.WordAccess

class DictViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var rawList   = mutableListOf<MutableLiveData<String>>()
    var rhymeList = mutableListOf<MutableLiveData<String>>()

    private var db: SQLiteDatabase? = null
    private var helper: SQLiteOpenHelper? = null
    private val wordAccess = WordAccess()

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){
        helper = SQLiteOpenHelper(getApplication())
        db = helper!!.writableDatabase
        val answerList:ArrayList<AnswerView> = wordAccess.getAnswers(db!!,0,30,2)

    }
    private fun loadTextColor(){
    }


}