package com.rapdict.takuro.rapdict

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        //初期表示
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout,UserExpFragment())
                .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // メニューが選択されたときの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rhyme_return -> {
                val intent = Intent(this, Rhyme_Return_Setting_Activity::class.java)
                startActivity(intent)
                return true
            }
            R.id.rhyme_dict -> {
                val intent = Intent(this, Dict__Activity::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                val intent = Intent(this, User_Setting::class.java)
                startActivity(intent)
                return true
            }
            R.id.user_exp->{
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrameLayout,UserExpFragment())
                        .commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
