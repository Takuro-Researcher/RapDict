package com.rapdict.takuro.rapdict.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.myDict.GameSettingFragment
import com.rapdict.takuro.rapdict.ui.dict.DictFragment
import com.rapdict.takuro.rapdict.ui.myDict.MyDictFragment
import com.rapdict.takuro.rapdict.ui.tutorial.TutorialFragment
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        // コードからフラグメントを追加
        if (savedInstanceState == null) {
            val isMove = intent.getBooleanExtra("MOVE", false)
            val transaction = supportFragmentManager.beginTransaction()
            // ゲーム画面からの移動ならば、ゲーム画面へ直接移動する
            if (isMove) {
                transaction.add(R.id.fragmentFrameLayout, GameSettingFragment())
                transaction.commit()
            } else {
                transaction.add(R.id.fragmentFrameLayout, TutorialFragment())
                transaction.commit()
            }


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
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentFrameLayout, DictFragment())
                            .commit()
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

    override fun onBackPressed() {
        // バックキーの無効化
        moveTaskToBack(true)
    }

}
