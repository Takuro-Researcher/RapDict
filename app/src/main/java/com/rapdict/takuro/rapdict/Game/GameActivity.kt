package com.rapdict.takuro.rapdict.Game

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rapdict.takuro.rapdict.Game.GameFragment
import com.rapdict.takuro.rapdict.R

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
        bundle.putInt("RETURN",answerNum)
        bundle.putInt("TIME",timeNum)
        bundle.putInt("QUESTION",questionNum)
        bundle.putInt("MIN_WORD",minWordNum)
        bundle.putInt("MAX_WORD",maxWordNum)

        setContentView(R.layout.activity_game)
        // コードからフラグメントを追加
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val gameFragment = GameFragment()
            gameFragment.arguments = bundle
            transaction.add(R.id.fragmentGame, gameFragment)
            transaction.commit()
        }

    }

}