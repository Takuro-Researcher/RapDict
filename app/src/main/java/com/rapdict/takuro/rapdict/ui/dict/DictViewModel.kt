package com.rapdict.takuro.rapdict.ui.dict

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.model.repository.AnswerRepository
import kotlinx.android.synthetic.main.list_dict.view.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class DictData(
        val id: Long,
        val question: String,
        val rhyme: String,
        val uid: Int,
        val isFavorite: MutableLiveData<Boolean>,
        val isDelete: MutableLiveData<Boolean> = MutableLiveData(false)) {
    fun favoriteChange(view: View) {
        val current = isFavorite.value ?: false
        if (current) {
            view.favorite_star.progress = 0F
        } else {
            view.favorite_star.playAnimation()
        }
        isFavorite.value = !current
    }
    fun delete():Boolean{
        isDelete.value = true
        return true
    }
}


class DictViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    private var dictDataListRaw = mutableListOf<DictData>()
    private var _dictDataList = MutableLiveData<MutableList<DictData>>()
    val dictDataList: LiveData<MutableList<DictData>> = _dictDataList
    // 監視用のアクション
    val dataGetAction: MutableLiveData<Unit> = MutableLiveData()

    // データ参照用のRepositoryクラス
    private val _answerRepository = AnswerRepository(application)
    private var index = 0L

    var min: Int = 0
    var max: Int = 1
    private var favoState: List<Int> = listOf(1)
    var radioType: MutableLiveData<Int> = MutableLiveData<Int>()

    init {
        radioType.postValue(R.id.flatFav)
    }

    fun deleteData(dictData: DictData){
        val uid = dictData.uid
        viewModelScope.launch {
            _answerRepository.deleteAnswer(uid)
        }
        dictDataListRaw.remove(dictData)
        _dictDataList.value = ArrayList(dictDataListRaw)
    }

    // データのお気に入りの変更
    fun updateFavorite(dictData: DictData) {
        val bool = dictData.isFavorite.value ?: false
        val boolInt = if (bool) 1 else 0
        val uid = dictData.uid
        viewModelScope.launch {
            _answerRepository.updateAnswer(uid, boolInt)
        }
    }


    // ridからデータベース用のリストに変更する
    fun convertRidList() {
        if (radioType.value == R.id.onlyFav) {
            favoState = listOf(1)
        } else if (radioType.value == R.id.withoutFav) {
            favoState = listOf(0)
        } else if (radioType.value == R.id.flatFav) {
            favoState = listOf(0, 1)
        }
    }

    // リストにデータを入力する。
    fun loadData() {
        val data = runBlocking {
            _answerRepository.getAnswer(min, max, favoState)
        }
        dictDataListRaw.clear()
        index = 0

        data.apply {
            if (isNotEmpty()) {
                forEach {
                    it.apply {
                        val favorite: Boolean = favorite == 1
                        val rhyme: String = answer ?: ""
                        val question: String = question ?: ""
                        val uid: Int = uid
                        val dictData = DictData(index, question, rhyme, uid, MutableLiveData(favorite))
                        index += 1
                        dictDataListRaw.add(dictData)
                    }
                }
            }
        }
        _dictDataList.value = ArrayList(dictDataListRaw)

    }


}
