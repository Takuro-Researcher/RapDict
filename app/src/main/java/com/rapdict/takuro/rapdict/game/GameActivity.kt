package com.rapdict.takuro.rapdict.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Common.HttpApiRequest
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import org.json.JSONArray
import org.json.JSONObject

open class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = intent
        val answerNum = intent.getIntExtra("RETURN",0)
        val timeNum = intent.getIntExtra("TIME",0)
        val questionNum =intent.getIntExtra("QUESTION",0)
        val minWordNum = intent.getIntExtra("MIN_WORD",0)
        val maxWordNum = intent.getIntExtra("MAX_WORD",0)
        val bundle =Bundle()
        val words = ArrayList<Word>()
        val httpApiRequest =HttpApiRequest()
        var rhyme = String()
        httpApiRequest.setOnCallBack(object : HttpApiRequest.CallBackTask(){
            override fun CallBack(result: String) {
                super.CallBack(result)
                bundle.putString("RHYMES",result)
                val transaction = supportFragmentManager.beginTransaction()
                val gameFragment = GameFragment()
                gameFragment.arguments = bundle
                transaction.add(R.id.fragmentGame, gameFragment)
                transaction.commit()
            }
        })

        bundle.putInt("RETURN",answerNum)
        bundle.putInt("TIME",timeNum)
        bundle.putInt("QUESTION",questionNum)
        bundle.putInt("MIN_WORD",minWordNum)
        bundle.putInt("MAX_WORD",maxWordNum)

        setContentView(R.layout.activity_game)
        // コードからフラグメントを追加
        if (savedInstanceState == null) {
            httpApiRequest.execute(CommonTool.makeApiUrl(minWordNum,maxWordNum,questionNum))
        }
    }

}
