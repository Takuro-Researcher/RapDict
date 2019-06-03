package com.rapdict.takuro.rapdict

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
import sample.intent.AnswerData
import android.text.TextUtils.isEmpty
import android.view.Gravity.CENTER
import android.view.Gravity.TOP
import com.rapdict.takuro.rapdict.WidgetController.Companion.int_Dp2Px
import android.R.string.cancel
import kotlin.concurrent.timer


class Rhyme_Return_Activity : AppCompatActivity() {
    private val dataFormat = SimpleDateFormat("ss.SS", Locale.US)
    private var helper: SQLiteOpenHelper? = null
    internal var finish_q = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = getIntent()

        /* データベースに関する記述　*/
        helper = SQLiteOpenHelper(applicationContext)
        val db = helper!!.readableDatabase
        val wordAccess = WordAccess()
        /* 出題されるワード */
        question_list = ArrayList<Word>()
        question_list = wordAccess.getWords(db, intent!!.getIntExtra(MIN, 0), intent!!.getIntExtra(MAX, 0), intent!!.getIntExtra(QUESTION, 0))

        /* ワードに対する答え */
        answer_list = ArrayList<AnswerData>()
        /* 要素のデザインを決めるコントローラ */
        val widgetController = WidgetController(applicationContext)

        val tableRow = arrayOfNulls<TableRow>(7)
        for (i in tableRow.indices) {
            tableRow[i] = TableRow(this)
            tableRow[i]?.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }

        /*デザインに関する記述*/
        val varLayout = TableLayout(this)
        varLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //2列をひとまとめにするレイアウトを指定。
        val layoutParams = TableRow.LayoutParams()
        layoutParams.weight = 1f

        /*TextView 初期化*/
        var furigana_text = TextView(this)
        var word_text = TextView(this)
        var question_text = TextView(this)

        //1列目　問題数を表示する部分
        val back_button = Button(this)
        back_button.text = "設定へ戻る"
        val question_number = intent!!.getIntExtra(QUESTION, 0)
        //第2引数にレイアウト情報を書く
        tableRow[0]?.addView(widgetController.settings(question_text, 20f, 20f, 20f, 20f, 15f, "問題数:  " + Integer.toString(question_number), TOP, 1), layoutParams)
        tableRow[0]?.addView(back_button)


        //2列目 制限時間を表示する部分
        var timerText = TextView(this)
        layoutParams.span = 2
        timerText=widgetController.settings(timerText, 20f, 20f, 20f, 0f, 15f, dataFormat.format(0), TOP, 1)
        var time_num = intent!!.getIntExtra(TIME, 0)
        val timer =object : CountDownTimer((time_num * 1000).toLong(), 10.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.setText(dataFormat.format(millisUntilFinished));
            }
            override fun onFinish() {
                finish_q++
                if (finish_q === intent.getIntExtra(QUESTION, 0)) {
                    cancel()
                    val intent = Intent(applicationContext, Result_Activity::class.java)
                    intent.putExtra(ANSWER_LIST, answer_list)
                    startActivity(intent)
                    cancel()
                } else if(finish_q < intent.getIntExtra(QUESTION, 0)){
                    var remain_q:Int =intent!!.getIntExtra(QUESTION,0)-finish_q
                    question_text.text ="問題数: "+ remain_q.toString()
                    furigana_text.text = (question_list!!.get(remain_q-1).furigana)
                    word_text.text =(question_list!!.get(remain_q-1).word)
                    start()
                }else{
                    cancel()
                }
            }
        }
        timer.start()
        tableRow[1]?.addView(timerText, layoutParams)

        //3列目 文字列
        word_text = widgetController.settings(word_text, 20f, 20f, 20f, 15f, 17f, question_list!!.get(question_number-1).word!!, CENTER, 0)
        tableRow[2]?.addView(word_text, layoutParams)

        //4列目 フリガナ

        furigana_text = widgetController.settings(furigana_text, 20f, 20f, 20f, 0f, 8f, question_list!!.get(question_number-1).furigana!!, CENTER, 0)
        tableRow[3]?.addView(furigana_text, layoutParams)

        //5列目 入力
        var add_button = Button(this)
        add_button = widgetController.settings(add_button, 10f, 10f, 10f, 10f, 8f, "思いついた!", CENTER, 0)
        var next_button = Button(this)
        next_button = widgetController.settings(next_button, 10f, 10f, 10f, 10f, 8f, "次の問題へ", CENTER, 0)

        //カラムの表記を設定
        val layoutParams2 = TableRow.LayoutParams()
        layoutParams2.weight = 1f
        layoutParams2.setMargins(int_Dp2Px(30f, applicationContext), int_Dp2Px(50f, applicationContext), int_Dp2Px(30f, applicationContext), int_Dp2Px(30f, applicationContext))
        val layoutParams3 = TableRow.LayoutParams()
        layoutParams3.weight = 1f
        layoutParams3.setMargins(int_Dp2Px(15f, applicationContext), int_Dp2Px(25f, applicationContext), int_Dp2Px(15f, applicationContext), int_Dp2Px(15f, applicationContext))
        val layoutParams4 = TableRow.LayoutParams()
        layoutParams4.weight = 1f
        layoutParams4.span = 2
        layoutParams4.setMargins(int_Dp2Px(30f, applicationContext), int_Dp2Px(40f, applicationContext), int_Dp2Px(30f, applicationContext), int_Dp2Px(15f, applicationContext))

        tableRow[4]?.addView(add_button, layoutParams2)
        tableRow[4]?.addView(next_button, layoutParams2)


        val record_button = Button(this)
        widgetController.settings(record_button, 10f, 10f, 10f, 10f, 8f, "次の問題へ!", CENTER, 0)

        val editText = arrayOfNulls<EditText>(4)
        for (i in editText.indices) {
            editText[i] = EditText(this)
            editText[i]?.setWidth(0)
            editText[i]?.setHint("ライムを入力")
        }

        add_button.setOnClickListener {
            tableRow[4]?.removeView(add_button)
            tableRow[4]?.removeView(next_button)
            timer.cancel()
            for (i in 0 until intent!!.getIntExtra(RET, 0)) {
                if (i == 0 || i == 1) {
                    tableRow[4]?.addView(editText[i], layoutParams3)
                } else if (i == 2 || i == 3) {
                    tableRow[5]?.addView(editText[i], layoutParams3)
                }
            }
            tableRow[6]?.addView(record_button, layoutParams4)
        }
        next_button.setOnClickListener {
            finish_q++
            System.out.println("next:"+finish_q)
            if (finish_q < intent!!.getIntExtra(QUESTION, 0)) {
                var remain_q:Int =intent!!.getIntExtra(QUESTION,0)-finish_q
                question_text.text ="問題数: "+ remain_q.toString()
                furigana_text.text = (question_list!!.get(remain_q-1).furigana)
                word_text.text =(question_list!!.get(remain_q-1).word)
                timer.start()
            } else  {
                val intent = Intent(applicationContext, Result_Activity::class.java)
                intent.putExtra(ANSWER_LIST, answer_list)
                startActivity(intent)
                timer.cancel()
            }
        }
        back_button.setOnClickListener { }

        val finalAdd_button1 = add_button
        val finalNext_button1 = next_button
        record_button.setOnClickListener(object : View.OnClickListener {
            internal var answer = arrayOfNulls<AnswerData>(4)
            override fun onClick(view: View) {
                finish_q++
                for (i in 0 until intent!!.getIntExtra(RET, 0)) {
                    answer[i] = AnswerData()
                    if (!isEmpty(editText[i]?.getText())) {
                        answer[i]?.answerSet(question_list!!.get(question_number-finish_q).word_id!!, editText[i]?.getText().toString(), question_list!!.get(question_number-finish_q).word!!)
                        editText[i]?.setText("")
                        answer_list!!.add(answer[i]!!)
                    }
                }
                tableRow[4]?.removeAllViews()
                if (intent!!.getIntExtra(RET, 0) > 2) {
                    tableRow[5]?.removeAllViews()
                }

                tableRow[6]?.removeAllViews()
                tableRow[4]?.addView(finalAdd_button1)
                tableRow[4]?.addView(finalNext_button1)
                if (finish_q <intent!!.getIntExtra(QUESTION,  0)) {
                    var remain_q:Int =intent!!.getIntExtra(QUESTION,0)-finish_q
                    question_text.text ="問題数: "+ remain_q.toString()
                    furigana_text.text = (question_list!!.get(remain_q-1).furigana)
                    word_text.text =(question_list!!.get(remain_q-1).word)
                    timer.start()
                } else {
                    val intent = Intent(applicationContext, Result_Activity::class.java)
                    intent.putExtra(ANSWER_LIST, answer_list)
                    startActivity(intent)
                    timer.cancel()
                }
            }
        })
        //レイアウトをビューに適用
        for (i in tableRow.indices) {
            varLayout.addView(tableRow[i])
        }
        setContentView(varLayout)
    }

    /*画面を写った瞬間に起動される。カウントダウンタイマーを停止させれば、後ろ側で問題が進むことはなく、
    　次に訪れた時はonCreateによる問題設定やレイアウトは再定義される*/
    public override fun onStop() {
        super.onStop()
        //裏側でストップウォッチをちゃんと止めるために使用
        finish_q = intent!!.getIntExtra(QUESTION, 0)+1
        finish()

    }
    companion object {
        private var question_list: ArrayList<Word>? = null
        private var answer_list: ArrayList<AnswerData>? = null
        private val QUESTION = "question"
        private val TIME = "time"
        private val MIN = "min"
        private val MAX = "max"
        private val RET = "ret"
        private val ANSWER_LIST = "answer_list"
    }
}