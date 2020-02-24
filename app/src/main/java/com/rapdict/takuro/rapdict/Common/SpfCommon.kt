package com.rapdict.takuro.rapdict.Common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SpfCommon (_spf: SharedPreferences){
    private val spf:SharedPreferences = _spf
    // Nameと目標を変更する処理
    fun userSave(name:String,target: Int){
        val editor = spf.edit()
        editor.putString("userName",name)
        // TODO 目標追加
        editor.putInt("target",target)
        editor.apply()
    }
}