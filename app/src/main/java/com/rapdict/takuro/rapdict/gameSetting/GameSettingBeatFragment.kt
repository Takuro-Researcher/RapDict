package com.rapdict.takuro.rapdict.gameSetting
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingBeatBinding
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingWordBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.myDict.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_game_setting_beat.*
import kotlinx.android.synthetic.main.fragment_mydict_choice.*
import org.koin.android.viewmodel.ext.android.viewModel


class GameSettingBeatFragment : androidx.fragment.app.Fragment() {
    lateinit private var binding: FragmentGameSettingBeatBinding
    lateinit private var parentfragment : Fragment
    lateinit private var viewModel: GameSettingViewModel
    lateinit var mediaPlayer: MediaPlayer
    private var src:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初期化
        parentfragment = parentFragment ?: Fragment()
        viewModel = ViewModelProviders.of(parentfragment).get(GameSettingViewModel::class.java)
        src = CommonTool.choiceMusic(false,viewModel.settingData.type,2) ?: CommonTool.choiceMusic(false,"low",2)
        mediaPlayer = MediaPlayer.create(activity,src)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameSettingBeatBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(parentfragment).get(GameSettingViewModel::class.java)
        binding.data= viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backIntent = Intent(activity,MainActivity::class.java)
        game_setting_music_button.setOnClickListener {
            viewModel.beatTypeArray
            src = CommonTool.choiceMusic(false,viewModel.settingData.type,2)
            mediaPlayer.pause()
            mediaPlayer = MediaPlayer.create(activity, src)
            mediaPlayer.start()
        }

    }

    override fun onResume() {
        super.onResume()
        val viewModel = ViewModelProviders.of(parentfragment).get(GameSettingViewModel::class.java)
        // null値でonItemSelectedが起動しないように初回起動しないようにした。本来こっちのほうがいい？
        val beatTypeListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                viewModel.settingData.type = viewModel.beatTypeArray.value?.get(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        game_setting_beat_type_spinner.setSelection(0,false)
        game_setting_beat_type_spinner.onItemSelectedListener = beatTypeListener

        val barListner = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                viewModel.settingData.bar = viewModel.barArray.value?.get(position) ?: 2
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        game_setting_bar_spinner.setSelection(0,false)
        game_setting_bar_spinner.onItemSelectedListener = barListner
    }

    override fun onDetach() {
        super.onDetach()
        mediaPlayer.stop()
        mediaPlayer.reset();
        mediaPlayer.release();
    }
}