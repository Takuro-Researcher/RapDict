package com.rapdict.takuro.rapdict.ui.gameSetting


import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingWordBinding
import com.rapdict.takuro.rapdict.myDict.GameSettingData
import com.rapdict.takuro.rapdict.ui.game.GameActivity
import com.rapdict.takuro.rapdict.ui.util.MoveDialog
import kotlinx.android.synthetic.main.fragment_game_setting_word.*


class GameSettingWordFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentGameSettingWordBinding? = null
    private val viewModel: GameSettingViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameSettingWordBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        //　ViewModelの初期化が入る前に、データの更新が入った場合、辞書データを変更する
        if (!viewModel.isNotUpdateMyDict()) {
            viewModel.changeDictData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.data = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ゲーム開始時。settingDataに直しJsonで渡す
        start_game_button.setOnClickListener {
            val min: Int = game_setting_min_spinner.selectedItem as Int
            val max: Int = game_setting_max_spinner.selectedItem as Int
            if (min <= max) {
                val data = viewModel.settingData
                val saveDialog = MoveDialog(requireContext(), { startGame(data) },
                        {}, "ゲームを開始します",
                        data.type + "ビート" + data.question + "問").dialog
                saveDialog.show()
            } else {
                Toast.makeText(activity, "最小が最大を超えないようにして下さい", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGame(data: GameSettingData) {
        val intent = Intent(requireContext(), GameActivity::class.java)
        val spfCommon = SpfCommon(PreferenceManager.getDefaultSharedPreferences(activity))
        try {
            spfCommon.settingSave(data)
            val mapper = jacksonObjectMapper()
            val jsonString = mapper.writeValueAsString(data)
            intent.putExtra("DATA", jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        startActivity(intent)
    }
}