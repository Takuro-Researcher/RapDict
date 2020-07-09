package com.rapdict.takuro.rapdict.myDict

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyDictTabAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return MyDictMakeFragment() }
            1 -> { return MyDictChoiceFragment() }
            2 -> { return MyDictMakeQuestionFragment() }
            else -> { return MyDictDisplayFragment() }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return "単語帳作成" }
            1 -> { return "単語帳選択" }
            2 -> { return "単語を登録" }
            else ->  { return "単語表示" }
        }
    }
    override fun getCount(): Int {
        return 4
    }
}