package com.rapdict.takuro.rapdict.Main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.GameSetting.GameSettingFragment
import com.rapdict.takuro.rapdict.Dict.Dict__Activity
import com.rapdict.takuro.rapdict.Exp.UserExpFragment
import com.rapdict.takuro.rapdict.UserSetting.UserSettingFragment

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        // コードからフラグメントを追加
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentFrameLayout, UserExpFragment())
            transaction.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // メニューが選択されたときの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rhyme_return -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrameLayout, GameSettingFragment())
                        .commit()
                return true
            }
            R.id.rhyme_dict -> {
                val intent = Intent(this, Dict__Activity::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrameLayout, UserSettingFragment())
                        .commit()
                return true
            }
            R.id.user_exp ->{
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrameLayout, UserExpFragment())
                        .commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}