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

    private var helper: SQLiteOpenHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        helper = SQLiteOpenHelper(applicationContext)


        val user_data = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val user_name = user_data.getString("user_name","ゲスト")
        val user_shougou = user_data.getString("shougou","何者でもない故、何者にでもなれる者")

        val user_nameView:TextView =findViewById(R.id.user_disp)
        user_nameView.setText(user_name+"さん")

        val shougouView:TextView = findViewById(R.id.shougou)
        shougouView.setText(user_shougou)

        val constraintLayout=findViewById<View>(R.id.layout)
        val animationDrawable: AnimationDrawable? = constraintLayout.background as AnimationDrawable
        animationDrawable?.setEnterFadeDuration(2000)
        animationDrawable?.setExitFadeDuration(4000)
        animationDrawable?.start();

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
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
