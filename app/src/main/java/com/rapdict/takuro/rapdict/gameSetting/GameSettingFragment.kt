package com.rapdict.takuro.rapdict.gameSetting

import android.content.AsyncQueryHandler
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingBinding
import com.rapdict.takuro.rapdict.game.GameActivity
import kotlinx.android.synthetic.main.fragment_game_setting.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

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
                HitAPITask().execute("https://script.google.com/macros/s/AKfycbwlFWDy9zFZK2nWMyXrHdHrlIJ2wZPx-ufvaLwukpjs0fMzqg/exec?min=6&max=12&num=30")

                val intent = Intent(view.context, GameActivity::class.java)
                intent.putExtra("QUESTION",questionSpinner.selectedItem as Int)
                intent.putExtra("TIME",timeSpinner.selectedItem as Int)
                intent.putExtra("MIN_WORD",minSpinner.selectedItem as Int)
                intent.putExtra("MAX_WORD",maxSpinner.selectedItem as Int)
                intent.putExtra("RETURN",returnSpinner.selectedItem as Int)
                startActivity(intent)
            }else{
                val dialog = AlertDialog.Builder(activity!!)
                dialog.setCancelable(false)
                dialog.setMessage("通信を行うため、インターネットと接続してください")
                dialog.setPositiveButton("OK",null)
                dialog.show()
            }


        }
    }
    inner class HitAPITask: AsyncTask<String, String, String>(){

        override fun doInBackground(vararg params: String?): String? {
            //ここでAPIを叩きます。バックグラウンドで処理する内容です。
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            val buffer: StringBuffer

            try {
                //param[0]にはAPIのURI(String)を入れます(後ほど)。
                //AsynkTask<...>の一つめがStringな理由はURIをStringで指定するからです。
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()  //ここで指定したAPIを叩いてみてます。

                //ここから叩いたAPIから帰ってきたデータを使えるよう処理していきます。

                //とりあえず取得した文字をbufferに。
                val stream = connection.inputStream
                reader = BufferedReader(InputStreamReader(stream))
                buffer = StringBuffer()
                var line: String?
                while (true) {
                    line = reader.readLine()
                    if (line == null) {
                        break
                    }
                    buffer.append(line)
                    //Log.d("CHECK", buffer.toString())
                }

                //ここからは、今回はJSONなので、いわゆるJsonをParseする作業（Jsonの中の一つ一つのデータを取るような感じ）をしていきます。

                //先ほどbufferに入れた、取得した文字列
                val jsonText = buffer.toString()

                //JSONObjectを使って、まず全体のJSONObjectを取ります。
                val parentJsonObj = JSONObject(jsonText)


                //ここから下は、接続エラーとかJSONのエラーとかで失敗した時にエラーを処理する為のものです。
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            //finallyで接続を切断してあげましょう。
            finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            //失敗した時はnullやエラーコードなどを返しましょう。
            return null
        }
    }

}
