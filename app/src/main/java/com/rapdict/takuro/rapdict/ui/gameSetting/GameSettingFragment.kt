package com.rapdict.takuro.rapdict.myDict


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.ui.gameSetting.GameSettingViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_game_setting.*

data class GameSettingData(var bar: Int, var type: String, var drumOnly: Boolean, var min: Int, var max: Int, var question: Int, var dictUid: Int)

class GameSettingFragment : androidx.fragment.app.Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_game_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        game_setting_pager.adapter = GameSettingViewPagerAdapter(this)
        game_setting_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }
}