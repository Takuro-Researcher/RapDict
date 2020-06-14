package com.rapdict.takuro.rapdict.gameSetting

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Common.HttpApiRequest
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingBinding
import com.rapdict.takuro.rapdict.game.GameActivity
import kotlinx.android.synthetic.main.fragment_game_setting.*
import org.json.JSONArray
import org.json.JSONObject
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
        val gameSettingViewModel:GameSettingViewModel by viewModel()
        val questionSpinner = question_spinner as Spinner
        val timeSpinner = time_spinner as Spinner
        val minSpinner = min_spinner as Spinner
        val maxSpinner = max_spinner as Spinner
        val returnSpinner = return_spinner as Spinner

        //最小文字>最大文字とならないように、スピナーの値を順次変更する
        minSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                gameSettingViewModel.updateMaxData(minSpinner.selectedItem as Int)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        start_button.setOnClickListener {

            val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true

            if (isConnected ){
                val intent = Intent(view.context, GameActivity::class.java)
                intent.putExtra("QUESTION",questionSpinner.selectedItem as Int)
                intent.putExtra("TIME",timeSpinner.selectedItem as Int)
                intent.putExtra("MIN_WORD",minSpinner.selectedItem as Int)
                intent.putExtra("MAX_WORD",maxSpinner.selectedItem as Int)
                intent.putExtra("RETURN",returnSpinner.selectedItem as Int)
                startActivity(intent)
            }else{
                val dialog = AlertDialog.Builder(activity!!).apply{
                    setCancelable(false)
                    setMessage("通信を行うため、インターネットと接続してください")
                    setPositiveButton("OK",null)
                }
                dialog.show()
            }
        }
    }

}
