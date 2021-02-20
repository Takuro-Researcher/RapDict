package com.rapdict.takuro.rapdict.ui.myDict

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rapdict.takuro.rapdict.ui.myDict.myDictChoice.MyDictChoiceFragment
import com.rapdict.takuro.rapdict.ui.myDict.myDictDisplay.MyDictDisplayFragment
import com.rapdict.takuro.rapdict.ui.myDict.myDictMake.MyDictMakeFragment
import com.rapdict.takuro.rapdict.ui.myDict.myDictMakeQuestion.MyDictMakeQuestionFragment


class MyDictTabAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return MyDictMakeFragment()
            }
            1 -> { return MyDictChoiceFragment()
            }
            2 -> { return MyDictMakeQuestionFragment()
            }
            else -> { return MyDictDisplayFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return "作成" }
            1 -> { return "選択" }
            2 -> { return "登録" }
            else ->  { return "一覧表示" }
        }
    }
    override fun getCount(): Int {
        return 4
    }
}