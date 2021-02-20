package com.rapdict.takuro.rapdict.ui.gameSetting

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class GameSettingViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return GameSettingBeatFragment()
            }
            else -> {
                return GameSettingWordFragment()
            }
        }
    }
}