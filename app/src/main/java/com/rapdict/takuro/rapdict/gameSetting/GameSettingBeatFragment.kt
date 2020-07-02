package com.rapdict.takuro.rapdict.gameSetting

import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingWordBinding


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.myDict.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_game_setting_beat.*
import org.koin.android.viewmodel.ext.android.viewModel


class GameSettingBeatFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentGameSettingWordBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameSettingWordBinding.inflate(inflater, container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(parentFragment!!).get(GameSettingViewModel::class.java)
        binding?.data= viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(parentFragment!!).get(GameSettingViewModel::class.java)
        val backIntent = Intent(activity,MainActivity::class.java)
        viewModel.test_text.value ="死去"
    }
}