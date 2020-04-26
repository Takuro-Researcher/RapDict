package com.rapdict.takuro.rapdict.game

import android.app.Application
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class GamePlayViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var oneRowVisibility: MutableLiveData<Int> = MutableLiveData()
    var twoRowVisibility: MutableLiveData<Int> = MutableLiveData()
    var editOneWeight: MutableLiveData<Float> = MutableLiveData()
    var editTwoWeight: MutableLiveData<Float> = MutableLiveData()
    var editThreeWeight: MutableLiveData<Float> = MutableLiveData()
    var editFourWeight: MutableLiveData<Float> = MutableLiveData()

    //ViewModel初期化時にロード
    init {
        oneRowVisibility.value = View.VISIBLE
        twoRowVisibility.value = View.GONE
        editOneWeight.value = 1.0f
        editTwoWeight.value = 0f
        editThreeWeight.value = 0f
        editFourWeight.value = 0f

    }

    fun draw(answer:Int){

    }
}