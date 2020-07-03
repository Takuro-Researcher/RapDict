package com.rapdict.takuro.rapdict.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.dict.DictActivity
import com.rapdict.takuro.rapdict.myDict.GameSettingFragment
import com.rapdict.takuro.rapdict.myDict.MyDictFragment
import com.rapdict.takuro.rapdict.tutorial.TutorialFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tutorial.*


open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        // コードからフラグメントを追加
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentFrameLayout, TutorialFragment())
            transaction.commit()
        }

        navi.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.game -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentFrameLayout, GameSettingFragment())
                            .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.dict -> {
                    val intent = Intent(this, DictActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.mydict -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentFrameLayout, MyDictFragment())
                            .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // メニューが選択されたときの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tutorial -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrameLayout, TutorialFragment())
                        .commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
