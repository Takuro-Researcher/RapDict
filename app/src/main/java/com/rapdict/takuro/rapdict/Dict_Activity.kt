package com.rapdict.takuro.rapdict

import android.app.ActionBar
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.Gravity.isVertical
import android.view.ViewGroup
import android.widget.*
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.view.ViewGroup.LayoutParams.MATCH_PARENT


class Dict__Activity : AppCompatActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase

        val frame_linear_layout = LinearLayout(this)
        frame_linear_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        frame_linear_layout.orientation= LinearLayout.VERTICAL

        val parent_layout = LinearLayout(this)
        parent_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parent_layout.orientation =LinearLayout.VERTICAL


        val over_layout = LinearLayout(this)
        over_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT,0.2F)
       


        val under_layout = LinearLayout(this)
        under_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT,0.1F)


        val widgetController = WidgetController(this)
        val tableRow = arrayOfNulls<TableRow>(100)
        val checkBox = arrayOfNulls<CheckBox>(100)
        val question_view = arrayOfNulls<TextView>(100)
        val answer_view = arrayOfNulls<TextView>(100)
        val furigana_view = arrayOfNulls<TextView>(100)
        
        val len_Min_EditText = EditText(this)
        val len_Max_EditText = EditText(this)
        val search_Button = Button(this)
        search_Button != widgetController.settings(search_Button,10f,10f,10f,10f,8f,"検索開始", CENTER,0)

        len_Max_EditText.setWidth(0)
        len_Max_EditText.setHint("数値を入力")
        len_Min_EditText.setWidth(0)
        len_Min_EditText.setHint("数値を入力")
        val len_Min_EditText2 = EditText(this)
        val len_Max_EditText2 = EditText(this)
        val search_Button2 = Button(this)
        search_Button2 != widgetController.settings(search_Button,10f,10f,10f,10f,8f,"検索開始", CENTER,0)

        len_Max_EditText2.setWidth(0)
        len_Max_EditText2.setHint("オーバー")
        len_Min_EditText2.setWidth(0)
        len_Min_EditText2.setHint("オーバー")




        over_layout.addView(len_Max_EditText)
        over_layout.addView(len_Min_EditText)
        over_layout.addView(search_Button)

        under_layout.addView(len_Max_EditText2)
        under_layout.addView(len_Min_EditText2)
        under_layout.addView(search_Button2)



        val varLayout = TableLayout(this)
        varLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)



        val layoutParams = TableRow.LayoutParams()
        layoutParams.weight = 1f



        val scrollView = ScrollView(this)
        scrollView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT,0.7F)

        


        for (i in 1..15){
            tableRow[i]= TableRow(this)
            tableRow[i]?.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

            answer_view[i] = TextView(this)
            question_view[i] = TextView(this)

            question_view[i] = widgetController.settings(question_view[i]!!, 10f, 10f, 10f, 10f, 8f, "キリストは仮免で落ちた", Gravity.TOP, 1)
            answer_view[i] = widgetController.settings(answer_view[i]!!, 10f, 10f, 10f, 10f, 8f, "キリストは仮免で落ちた", Gravity.TOP, 1)

            tableRow[i]?.addView(question_view[i],layoutParams)
            tableRow[i]?.addView(answer_view[i],layoutParams)
            varLayout.addView(tableRow[i])
        }
        scrollView.addView(varLayout)

        parent_layout.addView(over_layout)
        parent_layout.addView(scrollView)
        parent_layout.addView(under_layout)
        frame_linear_layout.addView(parent_layout)
        setContentView(frame_linear_layout)

    }
}

