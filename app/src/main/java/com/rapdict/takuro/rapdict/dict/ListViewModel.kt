package com.rapdict.takuro.rapdict.dict

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.database.Answer
import kotlinx.coroutines.runBlocking

class ListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var questionList   = mutableListOf<MutableLiveData<String>>()
    var rhymeList = mutableListOf<MutableLiveData<String>>()
    var idList = mutableListOf<MutableLiveData<Int>>()
    var favoList = mutableListOf<MutableLiveData<Boolean>>()
    var colorList = mutableListOf<MutableLiveData<Int>>()
    var favoriteProgressList = mutableListOf<MutableLiveData<Float>>()


    //ViewModel初期化時にロード
    init {
        loadAnswerData()
    }
    private fun loadAnswerData(){
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
        val uid = idList.get(position).value
        val bool_int = if (bool) 1 else 0
        runBlocking {
            val dao = App.db.answerDao()
            dao.updateByIdsFavorite(uid!!,bool_int)
        }
    }

    private fun clearAnswer(){
        idList.clear()
        questionList.clear()
        rhymeList.clear()
        colorList.clear()
        favoList.clear()
        favoriteProgressList.clear()
    }


    fun favo2background(favorite:Boolean):Int{
        return if (favorite){
            Color.YELLOW
        }else{
            Color.WHITE
        }
    }
}