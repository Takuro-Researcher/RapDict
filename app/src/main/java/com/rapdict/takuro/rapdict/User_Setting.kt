package com.rapdict.takuro.rapdict

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class User_Setting: MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userdata_setting)
        //Toolbar
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        val user_data = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val editor  = user_data.edit()

        //ウィジェットのインスタンス生成
        val user_name_View:EditText = findViewById<EditText>(R.id.edit_name)
        val exp_View:TextView = findViewById<TextView>(R.id.exp)
        val now_shougou_View:TextView = findViewById<TextView>(R.id.shougou)
        val rename_button :Button = findViewById<Button>(R.id.rename_button)

        //現状のプリファレンスのデータを抽出
        val user_exp =user_data.getInt("exp",0)
        val user_shougou =user_data.getInt("shougou",1)
        val user_name =user_data.getString("user_name","ゲスト")


        //現在のユーザデータを画面に反映
        exp_View.text = user_exp.toString()+" exp"
        now_shougou_View.text = user_shougou.toString()
        user_name_View.setText(user_name)

        rename_button.setOnClickListener{
            val re_name = user_name_View.text.toString()
            editor.putString("user_name",re_name)
            editor.apply()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}