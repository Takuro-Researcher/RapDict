package com.rapdict.takuro.rapdict.exp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserExpViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var userName: MutableLiveData<String> = MutableLiveData()
    var target: MutableLiveData<String> = MutableLiveData()

    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){
        val spf:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
        val isNamed = spf.getBoolean("userName",false)
        if (isNamed){
            userName.value = spf.getString("userName","")
        }

        target.value = "イベント"
    }


}
