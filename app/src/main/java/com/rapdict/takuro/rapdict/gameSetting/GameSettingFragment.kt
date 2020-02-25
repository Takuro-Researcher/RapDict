package com.rapdict.takuro.rapdict.gameSetting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingBinding
import com.rapdict.takuro.rapdict.game.GameActivity
import kotlinx.android.synthetic.main.fragment_game_setting.*
import org.koin.android.viewmodel.ext.android.viewModel

class GameSettingFragment : androidx.fragment.app.Fragment() {
    private var binding:FragmentGameSettingBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameSettingBinding.inflate(inflater, container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gameSettingViewModel:GameSettingViewModel by viewModel()
        binding?.settingData  = gameSettingViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questionSpinner = question_spinner as Spinner
        val timeSpinner = time_spinner as Spinner
        val minSpinner = min_spinner as Spinner
        val maxSpiner = max_spinner as Spinner
        val returnSpinner = return_spinner as Spinner



        //最小文字>最大文字とならないように、スピナーの値を順次変更する
        minSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        start_button.setOnClickListener {
            val intent: Intent = Intent(view.context, GameActivity::class.java)
            intent.putExtra("QUESTION",questionSpinner.selectedItem as Int)
            intent.putExtra("TIME",timeSpinner.selectedItem as Int)
            intent.putExtra("MIN_WORD",minSpinner.selectedItem as Int)
            intent.putExtra("MAX_WORD",maxSpiner.selectedItem as Int)
            intent.putExtra("RETURN",returnSpinner.selectedItem as Int)
            startActivity(intent)
        }
    }

}
