package com.rapdict.takuro.rapdict

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
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
                val intent2 = Intent(this, Dict__Activity::class.java)
                startActivity(intent2)
                return true
            }
            R.id.settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
