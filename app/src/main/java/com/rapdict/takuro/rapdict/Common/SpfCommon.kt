package com.rapdict.takuro.rapdict.Common

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SpfCommon (_context: Application){
    private val context:Application = _context
    private val spf: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    public  fun userSave(){
        val editor = spf.edit()
        editor.putString("user_name","保存確認")

    }
}