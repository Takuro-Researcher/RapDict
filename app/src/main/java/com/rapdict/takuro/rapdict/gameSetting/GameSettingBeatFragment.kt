package com.rapdict.takuro.rapdict.gameSetting

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingBeatBinding
import kotlinx.android.synthetic.main.fragment_game_setting_beat.*


class GameSettingBeatFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentGameSettingBeatBinding? = null
    private val viewModel: GameSettingViewModel by activityViewModels()
    lateinit var mediaPlayer: MediaPlayer
    private var src: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初期化
        src = CommonTool.choiceMusic(false, viewModel.settingData.type, 2)
        mediaPlayer = MediaPlayer.create(activity, src)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameSettingBeatBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.data = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        game_setting_music_button.setOnClickListener {
            viewModel.beatTypeArray
            src = CommonTool.choiceMusic(false, viewModel.settingData.type, 2)
            mediaPlayer.pause()
            mediaPlayer = MediaPlayer.create(activity, src)
            mediaPlayer.start()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
    }
}