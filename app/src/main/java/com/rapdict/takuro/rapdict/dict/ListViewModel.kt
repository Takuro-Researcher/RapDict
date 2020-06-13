package com.rapdict.takuro.rapdict.dict

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import com.rapdict.takuro.rapdict.helper.WordAccess
import java.text.FieldPosition

class ListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var rawList   = mutableListOf<MutableLiveData<String>>()
    var rhymeList = mutableListOf<MutableLiveData<String>>()
    var idList = mutableListOf<MutableLiveData<Int>>()
    var favoList = mutableListOf<MutableLiveData<Boolean>>()
    var colorList = mutableListOf<MutableLiveData<Int>>()
    var favoriteProgressList = mutableListOf<MutableLiveData<Float>>()


    private var db: SQLiteDatabase? = null
    private var helper: SQLiteOpenHelper? = null
    private val answerView = AnswerView()

    //ViewModel初期化時にロード
    init {
        loadAnswerData()
    }
    private fun loadAnswerData(){
        helper = SQLiteOpenHelper(getApplication())
        db = helper!!.writableDatabase
        val answerList:ArrayList<AnswerView> = answerView.getAnswers(db!!,0,30,2)
        bindAnswer(answerList)
    }

    fun bindAnswer(answerList:ArrayList<AnswerView>) {
        clearAnswer()
        answerList.forEach { answer ->
            idList.add(MutableLiveData<Int>().apply { value = answer.answerview_id})
            rawList.add(MutableLiveData<String>().apply { value = answer.question })
            rhymeList.add(MutableLiveData<String>().apply { value = answer.answer })
            colorList.add(MutableLiveData<Int>().apply { value = AnswerView.favo2background(answer.favorite!!) })
            favoList.add(MutableLiveData<Boolean>().apply { value = answer.favorite })
            favoriteProgressList.add(MutableLiveData<Float>().apply { value = if(answer.favorite!!) 1f else 0f  })
        }
    }
    // 削除処理時にLiveDataにも変更を加える
    fun removeAnswer(position: Int){
        idList.removeAt(position)
        rawList.removeAt(position)
        rhymeList.removeAt(position)
        colorList.removeAt(position)
        favoList.removeAt(position)
        favoriteProgressList.removeAt(position)
    }
    fun updateFavorite(position: Int,bool:Boolean){
        val answerView = AnswerView()
        db = SQLiteOpenHelper(getApplication()).writableDatabase
        answerView.answer_update_fav(db!!,idList[position].value!!,bool)
    }

    private fun clearAnswer(){
        idList.clear()
        rawList.clear()
        rhymeList.clear()
        colorList.clear()
        favoList.clear()
        favoriteProgressList.clear()
    }
}