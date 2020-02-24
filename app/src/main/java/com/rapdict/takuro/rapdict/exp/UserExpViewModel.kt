package com.rapdict.takuro.rapdict.exp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.Preference
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rapdict.takuro.rapdict.R
import kotlinx.coroutines.launch

class UserExpViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var userName: MutableLiveData<String> = MutableLiveData()
    var target: MutableLiveData<String> = MutableLiveData()
    var userNameColor: MutableLiveData<Int> = MutableLiveData()
    var targetColor: MutableLiveData<Int> = MutableLiveData()
    val spf:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())

    //ViewModel初期化時にロード
    init {
        loadUserData()
        loadTextColor()
    }
    private fun loadUserData(){
        val app = getApplication<Application>()
        userName.value = spf.getString("userName",String.format(app.getString(R.string.unSetting),app.getString(R.string.user_name)))
        var targetVal = spf.getInt("target",-1)
        if (targetVal == -1){
            target.value = String.format(app.getString(R.string.unSetting),app.getString(R.string.target))
        }else{
            target.value = String.format(app.getString(R.string.todaysTarget),targetVal.toString())
        }

    }
    private fun loadTextColor(){
        userNameColor.value = Color.BLACK
        targetColor.value =Color.BLACK
        if(spf.getString("userName","")!!.isBlank()){
            userNameColor.value = Color.GRAY
        }
        if(spf.getInt("target",-1)!! == -1){
            targetColor.value = Color.GRAY
        }
    }


}
