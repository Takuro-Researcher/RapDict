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
        val jsonData:String = intent.getStringExtra("DATA")
        val transaction = supportFragmentManager.beginTransaction()

        val bundle =Bundle()
        System.out.println(jsonData)
    }

}
