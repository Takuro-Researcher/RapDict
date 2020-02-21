package com.rapdict.takuro.rapdict.userSetting

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.R

class UserSettingViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    var userName: MutableLiveData<String> = MutableLiveData()
    val spf: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())


    //ViewModel初期化時にロード
    init {
        loadUserData()
    }
    private fun loadUserData(){
        val app = getApplication<Application>()
        userName.value = spf.getString("userName","")
    }

}