package com.rapdict.takuro.rapdict.dict

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Repository.AnswerRepository
import com.rapdict.takuro.rapdict.database.Answer
import kotlinx.android.synthetic.main.fragment_dict.view.*
import kotlinx.coroutines.*

data class DictData(
        val id: Long,
        val question:String,
        val rhyme:String,
        val uid: Int,
        val isFavorite:MutableLiveData<Boolean>
)


class DictViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    private var dictDataListRaw = mutableListOf<DictData>()
    private var _dictDataList = MutableLiveData<MutableList<DictData>>()
    val dictDataList: LiveData<MutableList<DictData>> = _dictDataList
    val minMaxAction: MutableLiveData<Unit> = MutableLiveData()

    // データ参照用のRepositoryクラス
    private val _answerRepository = AnswerRepository(application)
    private var index = 0L

    var min: Int = 0
    var max: Int = 1
    private var favoState:List<Int> = listOf(1)
    var radioType:MutableLiveData<Int> = MutableLiveData<Int>()

    init{
        radioType.postValue(R.id.flatFav)
    }
    fun convertRidList(){
        if(radioType.value == R.id.onlyFav){
            favoState = listOf(1)
        }else if(radioType.value == R.id.withoutFav){
            favoState = listOf(0)
        }else if(radioType.value == R.id.flatFav){
            favoState = listOf(0,1)
        }
    }

    fun loadData(){
        val data = runBlocking {
            _answerRepository.getAnswer(min,max,favoState)
        }
        dictDataListRaw.clear()
        index = 0

        data.apply {
            if(isNotEmpty()){
                forEach {
                    it.apply {
                        val favorite:Boolean = favorite == 1
                        val rhyme:String = answer ?: ""
                        val question:String = question ?: ""
                        val uid:Int = uid
                        val dictData = DictData(index,question,rhyme,uid,MutableLiveData(favorite))
                        index += 1
                        dictDataListRaw.add(dictData)
                    }
                }
            }
        }
        _dictDataList.value = ArrayList(dictDataListRaw)

    }


}
