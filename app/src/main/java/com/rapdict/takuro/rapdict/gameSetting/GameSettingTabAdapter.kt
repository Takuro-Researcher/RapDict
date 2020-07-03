package com.rapdict.takuro.rapdict.gameSetting

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter


class GameSettingTabAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return GameSettingBeatFragment() }
            else -> { return GameSettingWordFragment() }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return "ビートの設定" }
            else -> { return "言葉の設定" }
        }
    }
    override fun getCount(): Int {
        return 2
    }
}