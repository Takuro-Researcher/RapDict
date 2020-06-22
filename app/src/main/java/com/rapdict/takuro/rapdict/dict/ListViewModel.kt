package com.rapdict.takuro.rapdict.dict

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import com.rapdict.takuro.rapdict.model.Answer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class ListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var questionList   = mutableListOf<MutableLiveData<String>>()
    var rhymeList = mutableListOf<MutableLiveData<String>>()
    var idList = mutableListOf<MutableLiveData<Int>>()
    var favoList = mutableListOf<MutableLiveData<Boolean>>()
    var colorList = mutableListOf<MutableLiveData<Int>>()
    var favoriteProgressList = mutableListOf<MutableLiveData<Float>>()


    private var db: SQLiteDatabase? = null
    private var helper: SQLiteOpenHelper? = null

    //ViewModel初期化時にロード
    init {
        loadAnswerData()
    }
    private fun loadAnswerData(){
        helper = SQLiteOpenHelper(getApplication())
        db = helper!!.writableDatabase
        var datas : List<Answer>
        runBlocking {
            val dao = App.db.answerDao()
            datas = dao.findAll()
            bindAnswer(datas)
        }

    }

    fun bindAnswer(answerList: List<Answer>) {
        clearAnswer()
        answerList.forEach { answer ->
            val answerBool = if (answer.favorite ==0) false else true
            idList.add(MutableLiveData<Int>().apply { value = answer.uid})
            questionList.add(MutableLiveData<String>().apply { value = answer.question })
            rhymeList.add(MutableLiveData<String>().apply { value = answer.answer })
            colorList.add(MutableLiveData<Int>().apply { value = favo2background(answerBool) })
            favoList.add(MutableLiveData<Boolean>().apply { value = answerBool })
            favoriteProgressList.add(MutableLiveData<Float>().apply { value = if(answerBool) 1f else 0f  })
        }
    }
    // 削除処理時にLiveDataにも変更を加える
    fun removeAnswer(position: Int){
        idList.removeAt(position)
        questionList.removeAt(position)
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
        questionList.clear()
        rhymeList.clear()
        colorList.clear()
        favoList.clear()
        favoriteProgressList.clear()
    }

    fun getSearchFav(id:Int):Int{
        if (id == R.id.withoutFav){
            return 0
        }else if(id == R.id.onlyFav){
            return 1
        }
        return 2
    }
    fun favo2background(favorite:Boolean):Int{
        return if (favorite){
            Color.YELLOW
        }else{
            Color.WHITE
        }
    }
}