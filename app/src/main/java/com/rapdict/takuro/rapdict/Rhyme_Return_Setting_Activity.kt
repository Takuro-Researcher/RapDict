package com.rapdict.takuro.rapdict

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle

import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

import java.util.ArrayList
import java.util.Collections

class Rhyme_Return_Setting_Activity : MainActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rhyme_return_setting)
        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase

        //Toolbar
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)


        var question = arrayListOf<Int>()
        val time = arrayListOf<Int>()
        val min = arrayListOf<Int>()
        val max = arrayListOf<Int>()
        val ret = arrayListOf<Int>()

        //問題数10~30
        run {
            var i = 10
            while (i < 31) {
                question.add(i)
                i += 10
            }
        }
        //制限時間1~8
        run {
            var i = 3
            while (i < 30) {
                time.add(i)
                i += 3
            }
        }
        //最小文字2~7
        for (i in 3..7) {
            min.add(i)
        }
        //最大文字3~10
        for (i in 4..10) {
            max.add(i)
        }
        //
        for (i in 1..4) {
            ret.add(i)
        }

        var q_adapter =ArrayAdapter(this,android.R.layout.simple_spinner_item,question);
        var time_adapter =ArrayAdapter(this,android.R.layout.simple_spinner_item,time);
        var min_adapter =ArrayAdapter(this,android.R.layout.simple_spinner_item,min);
        var ret_adapter =ArrayAdapter(this,android.R.layout.simple_spinner_item,ret);
        var max_adapter =ArrayAdapter(this,android.R.layout.simple_spinner_item,max);


        val s1 = findViewById<View>(R.id.question_spinner) as Spinner
        s1.adapter = q_adapter

        val s2 = findViewById<View>(R.id.time_spinner) as Spinner
        s2.adapter = time_adapter

        val s3 = findViewById<View>(R.id.min_spinner) as Spinner
        s3.adapter = min_adapter



        //最小文字>最大文字とならないように、スピナーの値を順次変更する
        s3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            internal var min_value: Int = 0
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val spinner = adapterView as Spinner
                min_value = spinner.selectedItem as Int
                max_adapter.clear();
                for (f in min_value + 1..10) {
                    max_adapter.add(f)
                }

            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        val s4 = findViewById<View>(R.id.max_spinner) as Spinner
        s4.adapter = max_adapter


        val s5 = findViewById<View>(R.id.return_spinner) as Spinner
        s5.adapter = ret_adapter

        //ボタンをおされた時の処理
        val start_button = findViewById<View>(R.id.start_button) as Button
        start_button.setOnClickListener {
            val intent = Intent(applicationContext, Rhyme_Return_Activity::class.java)
            intent.putExtra(QUESTION, (findViewById<View>(R.id.question_spinner) as Spinner).selectedItem as Int)
            intent.putExtra(MIN, (findViewById<View>(R.id.min_spinner) as Spinner).selectedItem as Int)
            intent.putExtra(MAX, (findViewById<View>(R.id.max_spinner) as Spinner).selectedItem as Int)
            intent.putExtra(TIME, (findViewById<View>(R.id.time_spinner) as Spinner).selectedItem as Int)
            intent.putExtra(RET, (findViewById<View>(R.id.return_spinner) as Spinner).selectedItem as Int)
            startActivity(intent)
        }


        //Toolbarのトップページに戻るボタンを出現させる処理
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        private val QUESTION = "question"
        private val TIME = "time"
        private val MIN = "min"
        private val MAX = "max"
        private val RET = "ret"
    }


}



