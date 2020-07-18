package com.rapdict.takuro.rapdict.gameSetting

import android.app.Activity
import android.content.Context
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter


class GameSettingViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> { return GameSettingBeatFragment() }
            else -> { return GameSettingWordFragment() }
        }
    }
}