package com.rapdict.takuro.rapdict

import android.app.FragmentManager
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList

import sample.intent.AnswerData

import android.view.Gravity.CENTER
import android.view.Gravity.TOP
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.rapdict.takuro.rapdict.WidgetController.Companion.int_Dp2Px

class Result_Activity : AppCompatActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = FrameLayout(this)
        layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.setBackgroundResource(R.drawable.clear_gradient_list)
        val animationDrawable: AnimationDrawable? = layout.background as AnimationDrawable
        animationDrawable?.setEnterFadeDuration(2000)
        animationDrawable?.setExitFadeDuration(4000)
        animationDrawable?.start();

        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase
        val answer_list = getIntent().getSerializableExtra(ANSWER_LIST) as ArrayList<sample.intent.AnswerData>
        val widgetController = WidgetController(this)

        val tableRow = arrayOfNulls<TableRow>(120)
        val checkBox = arrayOfNulls<CheckBox>(120)
        val question_view = arrayOfNulls<TextView>(120)
        val answer_view = arrayOfNulls<TextView>(120)
        val record_list = ArrayList<AnswerData>()

        //ゲームクリアを知らせる文面を記述
        val achieveRow = TableRow(this)
        val achieve_inviteRow =TableRow(this)
        var achieve_text = TextView(this)
        achieve_text = widgetController.settings(achieve_text, 20f, 20f, 20f, 15f, 15f, "おつかれさま！！！", CENTER, 0)
        var achieve_invitetext = TextView(this)
        achieve_invitetext = widgetController.settings(achieve_invitetext, 20f, 20f, 20f, 15f, 8f, answer_list.size.toString()+"個踏みました。気に入った韻を保存しましょう", CENTER, 0)



        val buttonRow = TableRow(this)
        val varLayout = TableLayout(this)
        varLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //行を等分割する
        val layoutParams = TableRow.LayoutParams()
        layoutParams.weight = 1f
        layoutParams.setMargins(int_Dp2Px(0f, applicationContext), int_Dp2Px(10f, applicationContext), int_Dp2Px(0f, applicationContext), int_Dp2Px(10f, applicationContext))
        //3分割された行を同時に使用する
        val layoutParams4 = TableRow.LayoutParams()
        layoutParams4.weight = 1f
        layoutParams4.span = 3
        //left top right bottomの順番でマージンを設定する
        layoutParams4.setMargins(int_Dp2Px(15f, applicationContext), int_Dp2Px(15f, applicationContext), int_Dp2Px(15f, applicationContext), int_Dp2Px(15f, applicationContext))

        achieveRow.addView(achieve_text,layoutParams4)
        achieve_inviteRow.addView(achieve_invitetext,layoutParams4)
        varLayout.addView(achieveRow)
        varLayout.addView(achieve_inviteRow)

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
        layout.addView(scrollView)
        setContentView(layout)
        val answer = arrayOf<AnswerData>(AnswerData())

        record_button.setOnClickListener {
            var dialog =Db_DialogFragment()
            dialog.title="データの保存確認"
            for (i in answer_list.indices) {
                if (checkBox[i]?.isChecked() == true) {
                    answer[0] = answer_list[i]
                    record_list.add(answer[0])
                }
            }
            dialog.message = record_list.count().toString()+"個の韻を保存します"
            dialog.onOkClickListener = DialogInterface.OnClickListener { dialog, id ->
               for(answer in record_list){
                    helper!!.answer_saveData(db!!,answer.answer!!, answer.question_id!!,0)
               }
                val intent = Intent(applicationContext, Rhyme_Return_Setting_Activity::class.java)
                startActivity(intent)
            }
            dialog.show(fragmentManager,"sample")
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
