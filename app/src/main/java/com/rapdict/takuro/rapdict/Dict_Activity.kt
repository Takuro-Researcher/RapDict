package com.rapdict.takuro.rapdict

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.airbnb.lottie.LottieAnimationView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil


class Dict__Activity : AppCompatActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null
    //現在表示しているページを表示
    private var current_disp = 0
    //現在表示しているAnswerの数を表示
    private var current_answer_num =0

    @SuppressLint("NewApi", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict)
        //DbAccess関連のインスタンス生成
        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase
        val wordAccess = WordAccess()


    }
}



