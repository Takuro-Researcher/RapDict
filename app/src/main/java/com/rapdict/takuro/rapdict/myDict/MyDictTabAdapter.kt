package com.rapdict.takuro.rapdict.myDict

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rapdict.takuro.rapdict.gameSetting.GameSettingFragment

class MyDictTabAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return MyDictMakeFragment() }
            1 -> { return MyDictChoiceFragment() }
            else ->  { return MyDictMakeQuestionFragment() }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return "辞書作成" }
            1 -> { return "辞書を選択" }
            else ->  { return "言葉を登録" }
        }
    }
    override fun getCount(): Int {
        return 3
    }
}