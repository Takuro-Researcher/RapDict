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
    var editTwoVisibility : MutableLiveData<Int>  = MutableLiveData()
    var buttonEnabled :MutableLiveData<Boolean> = MutableLiveData()


    //ViewModel初期化時にロード
    init {
        oneRowVisibility.value = View.VISIBLE
        twoRowVisibility.value = View.GONE
        editOneWeight.value = 1.0f
        editTwoWeight.value = 0f
        editThreeWeight.value = 0f
        editFourWeight.value = 0f
        editTwoVisibility.value = View.GONE
        buttonEnabled.value = true
    }

    fun draw(answer:Int){
        when(answer) {
            2 -> {
                editTwoVisibility.value = View.VISIBLE
                editOneWeight.value = 0.5f
                editTwoWeight.value = 0.5f
            }
            3 -> {
                twoRowVisibility.value = View.VISIBLE
                editThreeWeight.value = 0.5f
                editFourWeight.value = 0.5f
            }
            4 -> {
                twoRowVisibility.value = View.VISIBLE
                editTwoVisibility.value = View.VISIBLE
                editOneWeight.value = 0.5f
                editTwoWeight.value = 0.5f
                editThreeWeight.value = 0.5f
                editFourWeight.value = 0.5f
            }
        }
    }
}