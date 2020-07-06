package com.rapdict.takuro.rapdict.gameSetting

import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingWordBinding


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProviders
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.game.GameActivity
import com.rapdict.takuro.rapdict.game.GamePlayFragment
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.myDict.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_game_setting_word.*
import kotlinx.android.synthetic.main.fragment_mydict_choice.*
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel


class GameSettingWordFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentGameSettingWordBinding? = null
    private var viewModel: GameSettingViewModel? =null
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
        viewModel = ViewModelProviders.of(parentFragment!!).get(GameSettingViewModel::class.java)
        val intent:Intent = Intent(view.context, GameActivity::class.java)
        start_game_button.setOnClickListener {
            viewModel!!.choiceMin = game_setting_min_spinner.selectedItem as Int
            viewModel!!.choiceMax = game_setting_max_spinner.selectedItem as Int
            viewModel!!.choiceQuestion = game_setting_question_spinner.selectedItem as Int

            var data:GameSettingData =viewModel!!.makeGameSettingData()
            try {
                val mapper     = jacksonObjectMapper()
                val jsonString = mapper.writeValueAsString(data)
                intent.putExtra("DATA",jsonString)
            } catch(e: Exception) {
                e.printStackTrace()
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // null値でonItemSelectedが起動しないように初回起動しないようにした。本来こっちのほうがいい？
        val useDictListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                var uid = viewModel!!.changeUseDict(position)
                var min = 3
                var max = 11
                if (uid != -1){
                    runBlocking {
                        val dao = db.wordDao()
                        min = dao.findByDictIdsMin(uid)
                        max = dao.findByDictIdsMax(uid)
                    }
                }
                viewModel!!.changedUseDict(min,max)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        game_setting_use_dict_spinner.setSelection(0,false)
        game_setting_use_dict_spinner.onItemSelectedListener = useDictListener
    }
}