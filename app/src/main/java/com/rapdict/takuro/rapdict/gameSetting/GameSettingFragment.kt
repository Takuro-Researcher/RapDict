package com.rapdict.takuro.rapdict.myDict


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.gameSetting.GameSettingTabAdapter
import kotlinx.android.synthetic.main.fragment_game_setting.*
import kotlinx.android.synthetic.main.fragment_mydict1.*
import kotlinx.android.synthetic.main.fragment_mydict_make.*
import kotlinx.coroutines.runBlocking


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

        val tabs_count:Int = GameSettingTabAdapter(childFragmentManager).count
        var mydict_count:Int = 0
        // 自分辞書が一件でも保存されていたら動く。
        game_setting_pager.adapter = GameSettingTabAdapter(childFragmentManager)
        game_setting_tab_layout.setupWithViewPager(mydict_pager)
    }

    fun adapterUnabled(tabs_count:Int){
        for(i in 1..tabs_count-1) {
            (mydict_tab_layout.getChildAt(0) as LinearLayout).getChildAt(i).isEnabled = false
            (mydict_tab_layout.getChildAt(0) as LinearLayout).getChildAt(i).setBackgroundColor(resources.getColor(R.color.darkGray))
        }
    }
}