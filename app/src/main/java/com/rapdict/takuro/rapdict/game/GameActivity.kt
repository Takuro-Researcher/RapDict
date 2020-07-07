package com.rapdict.takuro.rapdict.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Common.getHttp
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.gameSetting.GameSettingData
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient


open class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = intent
        val jsonData:String = intent.getStringExtra("DATA")
        val mapper = jacksonObjectMapper()
        val transaction = supportFragmentManager.beginTransaction()
        val data:GameSettingData = mapper.readValue(jsonData)
        // 音源キーを取得
        val src:Int = CommonTool.choiceMusic(data.drumOnly,data.type,data.bar)
        val bundle =Bundle()

        bundle.putInt("BAR",src)

        // 言葉を取得
        val req = getHttp(CommonTool.makeApiUrl(data.min,data.max,data.question))

        req.setOnCallBack(object : getHttp.CallBackTask(){
            override fun CallBack(result: String) {
                super.CallBack(result)
                bundle.putString("RHYMES",result)
                bundle.putInt("RETURN",1)
                bundle.putInt("QUESTION",data.question)
                bundle.putBoolean("ISMYDICT",false)
                changedTexts()
            }
        })
        setContentView(R.layout.activity_game)

        if(data.dictUid==-1){
            req.execute()
        }else{
            var wordData = listOf<Word>()
            runBlocking {
                val dao = db.wordDao()
                wordData = dao.findByLenght(data.min,data.max,data.dictUid,data.question)
            }
            val backIntent  = Intent(this, MainActivity::class.java)
            val recomdialog = AlertDialog.Builder(this)
            recomdialog.setCancelable(false)
            recomdialog.setMessage("韻が一つも取得できませんでした\n最小文字＆最大文字を変えて試してください")
            recomdialog.setPositiveButton("戻る"){
                _, _ -> startActivity(backIntent)
            }
            if(wordData.size ==0){
                recomdialog.show()
            }
            val mapData =  mutableMapOf("rhymes" to wordData)

            bundle.putInt("QUESTION",wordData.size)
            val result= mapper.writeValueAsString(mapData)
            bundle.putString("RHYMES",result)
            bundle.putBoolean("ISMYDICT",true)
            changedTexts()
        }

        game_start_button.setOnClickListener {
            game_start_button.visibility = View.INVISIBLE
            waiting_display.visibility = View.INVISIBLE
            attention_sound.visibility = View.INVISIBLE
            val gameFragment = GamePlayFragment()
            gameFragment.arguments = bundle
            transaction.add(R.id.fragmentGame, gameFragment)
            transaction.commit()
        }

    }

    fun changedTexts(){
        waiting_display.text =  application.getString(R.string.startingDisp)
        attention_sound.visibility = View.VISIBLE
        game_start_button.visibility = View.VISIBLE
        loading.visibility = View.INVISIBLE

    }
}
