package com.rapdict.takuro.rapdict.game

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Common.HttpApiRequest
import com.rapdict.takuro.rapdict.R
import kotlinx.android.synthetic.main.activity_game.*


open class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = intent
        val answerNum = intent.getIntExtra("RETURN",0)
        val timeNum = intent.getIntExtra("TIME",0)
        val questionNum =intent.getIntExtra("QUESTION",0)
        val minWordNum = intent.getIntExtra("MIN_WORD",0)
        val maxWordNum = intent.getIntExtra("MAX_WORD",0)


        val transaction = supportFragmentManager.beginTransaction()

        val bundle =Bundle()
        val httpApiRequest =HttpApiRequest()

        httpApiRequest.setOnCallBack(object : HttpApiRequest.CallBackTask(){
            override fun CallBack(result: String) {
                super.CallBack(result)
                bundle.putString("RHYMES",result)
                waiting_display.text =  application.getString(R.string.startingDisp)
                game_start_button.visibility = View.VISIBLE
                loading.visibility = View.INVISIBLE
            }
        })



        bundle.putInt("RETURN",answerNum)
        bundle.putInt("TIME",timeNum)
        bundle.putInt("QUESTION",questionNum)

        setContentView(R.layout.activity_game)

        game_start_button.setOnClickListener {
            game_start_button.visibility = View.INVISIBLE
            waiting_display.visibility = View.INVISIBLE
            val gameFragment = GamePlayFragment()
            gameFragment.arguments = bundle
            transaction.add(R.id.fragmentGame, gameFragment)
            transaction.commit()
        }
        // コードからフラグメントを追加
        if (savedInstanceState == null) {
            httpApiRequest.execute(CommonTool.makeApiUrl(minWordNum,maxWordNum,questionNum))
        }
    }

}
