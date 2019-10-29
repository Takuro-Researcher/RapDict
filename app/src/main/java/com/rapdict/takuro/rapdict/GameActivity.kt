package com.rapdict.takuro.rapdict

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View

open class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)
        // コードからフラグメントを追加
        if (savedInstanceState == null) {
            //val transaction = supportFragmentManager.beginTransaction()
            //transaction.add(R.id.fragmentFrameLayout, UserExpFragment())
            //transaction.commit()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_option, menu)
        return super.onCreateOptionsMenu(menu)
    }


}
