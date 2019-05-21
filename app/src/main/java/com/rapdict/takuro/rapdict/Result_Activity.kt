package com.rapdict.takuro.rapdict

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList

import sample.intent.AnswerData

import android.view.Gravity.CENTER
import android.view.Gravity.TOP
import android.widget.*
import com.rapdict.takuro.rapdict.WidgetController.Companion.int_Dp2Px

class Result_Activity : AppCompatActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase
        val answer_list = getIntent().getSerializableExtra(ANSWER_LIST) as ArrayList<sample.intent.AnswerData>
        val intent = getIntent()
        val widgetController = WidgetController(this)

        val tableRow = arrayOfNulls<TableRow>(120)
        val checkBox = arrayOfNulls<CheckBox>(120)
        val question_view = arrayOfNulls<TextView>(120)
        val answer_view = arrayOfNulls<TextView>(120)
        val record_list = ArrayList<AnswerData>()


        val buttonRow = TableRow(this)
        val varLayout = TableLayout(this)
        varLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val layoutParams = TableRow.LayoutParams()
        layoutParams.weight = 1f
        val layoutParams4 = TableRow.LayoutParams()
        layoutParams4.weight = 1f
        layoutParams4.span = 3
        layoutParams4.setMargins(int_Dp2Px(15f, applicationContext), int_Dp2Px(15f, applicationContext), int_Dp2Px(15f, applicationContext), int_Dp2Px(15f, applicationContext))
        //left top right bottomの順番でマージンを設定する
        layoutParams.setMargins(int_Dp2Px(0f, applicationContext), int_Dp2Px(10f, applicationContext), int_Dp2Px(0f, applicationContext), int_Dp2Px(10f, applicationContext))

        val scrollView = ScrollView(this)
        scrollView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        //   答えの数だけ初期化を行う。
        for (i in answer_list.indices) {
            tableRow[i] = TableRow(this)
            tableRow[i]?.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            answer_view[i] = TextView(this)
            question_view[i] = TextView(this)
            checkBox[i] = CheckBox(this)
        }
        var record_button = Button(this)
        record_button = widgetController.settings(record_button, 10f, 10f, 10f, 8f, 8f, "ワードを保存する！", CENTER, 0)
        buttonRow.addView(record_button, layoutParams4)

        if (answer_list != null || answer_list.size != 0) {
            var i = 0
            for (answer in answer_list) {
                question_view[i] = widgetController.settings(question_view[i]!!, 10f, 10f, 10f, 10f, 8f, answer.question!!, TOP, 1)
                answer_view[i] = widgetController.settings(answer_view[i]!!, 10f, 10f, 10f, 10f, 8f, answer.answer!!, TOP, 1)
                i++
            }
        }
        for (i in answer_list.indices) {
            tableRow[i]?.addView(question_view[i], layoutParams)
            tableRow[i]?.addView(answer_view[i], layoutParams)
            tableRow[i]?.addView(checkBox[i], layoutParams)
            varLayout.addView(tableRow[i])
        }
        varLayout.addView(buttonRow)
        scrollView.addView(varLayout)
        setContentView(scrollView)
        val answer = arrayOf<AnswerData>(AnswerData())

        record_button.setOnClickListener {
            for (i in answer_list.indices) {
                if (checkBox[i]?.isChecked() == true) {
                    answer[0] = answer_list[i]
                    record_list.add(answer[0])
                    helper!!.answer_saveData(db!!, answer[0].answer!!, answer[0].question_id!!)
                }
            }
            val intent = Intent(applicationContext, Rhyme_Return_Setting_Activity::class.java)
            startActivity(intent)
        }

    }

    //もうこの画面へ戻れないように。
    public override fun onStop() {
        super.onStop()
        finish()
    }

    companion object {
        private val ANSWER_LIST = "answer_list"
    }


}