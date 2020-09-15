package com.rapdict.takuro.rapdict.Common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rapdict.takuro.rapdict.gameSetting.GameSettingData

class SpfCommon (_spf: SharedPreferences){
    private val spf:SharedPreferences = _spf
    private val editor = spf.edit()
    private var mapper = jacksonObjectMapper()
    fun settingSave(gameSettingData: GameSettingData){
        var jsonSettingData = mapper.writeValueAsString(gameSettingData)
        editor.putString("SETTING",jsonSettingData)
        editor.apply()
    }

    fun settingRead():GameSettingData?{
        var jsonData = spf.getString("SETTING","")
        if(jsonData.isNullOrEmpty()){
            return null
        }
        var gameSettingData:GameSettingData =mapper.readValue(jsonData)
        return gameSettingData
    }
}