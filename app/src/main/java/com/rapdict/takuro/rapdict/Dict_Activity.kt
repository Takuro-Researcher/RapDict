package com.rapdict.takuro.rapdict

import android.annotation.SuppressLint
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
import kotlin.math.ceil


class Dict__Activity : AppCompatActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null
    private var current_disp = 0

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //DbAccess関連のインスタンス生成
        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase
        var wordAccess = WordAccess()

        //Activityを制御するLayoutインスタンス生成
        val frame_linear_layout = LinearLayout(this)
        frame_linear_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        frame_linear_layout.orientation= LinearLayout.VERTICAL
        val parent_layout = LinearLayout(this)
        parent_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parent_layout.orientation = LinearLayout.VERTICAL
        val over_layout = TableLayout(this)
        over_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT,0.1F)
        val under_layout = TableLayout(this)
        under_layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT,0.1F)

        //TableRowの列を制御するLayoutParamsを記述
        val layoutParams3 = TableRow.LayoutParams()
        layoutParams3.weight = 1f
        layoutParams3.setMargins(WidgetController.int_Dp2Px(10f, applicationContext), WidgetController.int_Dp2Px(10f, applicationContext), WidgetController.int_Dp2Px(10f, applicationContext), WidgetController.int_Dp2Px(10f, applicationContext))

        //widegetの設定を行うインスタンスを生成
        val widgetController = WidgetController(this)

        //一列目、検索に使用するTableRow
        val search_row = TableRow(this)
        val search_Button = Button(this)
        search_Button != widgetController.settings(search_Button,5f,5f,5f,5f,8f,"検索開始", CENTER,0)
        val min_spinner = Spinner(this)
        val min = arrayListOf<Int>()
        val max_spinner = Spinner(this)
        val max = arrayListOf<Int>()

        for(i in 3..7){
            min.add(i)
        }
        for(i  in 4..10){
            max.add(i)
        }
        var min_adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_item,min)
        var max_adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_item,max)
        min_spinner.adapter = min_adapter
        min_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var min_value: Int = 0
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
        max_spinner.adapter = max_adapter

        search_row.addView(min_spinner,layoutParams3)
        search_row.addView(max_spinner,layoutParams3)
        search_row.addView(search_Button,layoutParams3)
        search_row.setBackgroundColor(Color.rgb(255,255,200))
        over_layout.addView(search_row)


        //2列目
        val varLayout = TableLayout(this)
        varLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val layoutParams = TableRow.LayoutParams()
        layoutParams.weight = 1f
        val tableRow = arrayOfNulls<TableRow>(100)
        val checkBox = arrayOfNulls<CheckBox>(100)
        val question_view = arrayOfNulls<TextView>(100)
        val answer_view = arrayOfNulls<TextView>(100)
        var answers =ArrayList<AnswerView>()

        search_Button.setOnClickListener{
            var max =max_spinner.selectedItem as Int
            var min =min_spinner.selectedItem as Int
            answers = wordAccess.getAnswers(db!!,min,max,0)
            System.out.println(answers.count())
            for(i in 0..99){
                tableRow[i]?.removeAllViews()
            }
            for(i in 0..answers.count()-1){
                tableRow[i]= TableRow(this)
                tableRow[i]?.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                answer_view[i] = TextView(this)
                question_view[i] = TextView(this)
                checkBox[i] = CheckBox(this)
                question_view[i] = widgetController.settings(question_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i + (current_disp*100) ).question!!, Gravity.TOP, 1)
                answer_view[i] = widgetController.settings(answer_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i+ (current_disp*100) ).answer!!, Gravity.TOP, 1)
                tableRow[i]?.addView(question_view[i],layoutParams)
                tableRow[i]?.addView(answer_view[i],layoutParams)
                tableRow[i]?.addView(checkBox[i],layoutParams)
                varLayout.addView(tableRow[i])
                if(answers.count()> 100 && i===99){
                    break;
                }
            }
        }

        val scrollView = ScrollView(this)
        scrollView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT,0.8F)
        scrollView.addView(varLayout)


        //3列目
        val underRow = TableRow(this)
        val back_Button = Button(this)
        val next_Button = Button(this)
        val delete_Button = Button(this)
        back_Button != widgetController.settings(back_Button,5f,5f,5f,5f,9f,"←　",Gravity.CENTER,0)
        //100件以上取得した時に、値を表示する処理。前の100件に戻る。
        back_Button.setOnClickListener{
            if(current_disp!=0){
                current_disp+=-1
                for(i in 0..99){
                    tableRow[i]?.removeAllViews()
                }
                for(i in 0..99){
                    tableRow[i]= TableRow(this)
                    tableRow[i]?.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                    answer_view[i] = TextView(this)
                    question_view[i] = TextView(this)
                    checkBox[i] = CheckBox(this)
                    question_view[i] = widgetController.settings(question_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i + (current_disp*100) ).question!!, Gravity.TOP, 1)
                    answer_view[i] = widgetController.settings(answer_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i+ (current_disp*100) ).answer!!, Gravity.TOP, 1)
                    tableRow[i]?.addView(question_view[i],layoutParams)
                    tableRow[i]?.addView(answer_view[i],layoutParams)
                    tableRow[i]?.addView(checkBox[i],layoutParams)
                    varLayout.addView(tableRow[i])
                    if(answers.count()> 100 && i===99 || answers.count()==current_disp*100+i+1){
                        break;
                    }
                }
            }
        }
        //100件以上取得した時に値を表示する処理。次の100件に戻る
        next_Button.setOnClickListener(){
            if(!answers.isEmpty() && current_disp!= ceil(answers.count()/100.toDouble()).toInt()-1){
                    current_disp+=1
                    for(i in 0..99){
                        tableRow[i]?.removeAllViews()
                    }
                    for(i in 0..answers.count()-1){
                        tableRow[i]= TableRow(this)
                        tableRow[i]?.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                        answer_view[i] = TextView(this)
                        question_view[i] = TextView(this)
                        checkBox[i] = CheckBox(this)
                        question_view[i] = widgetController.settings(question_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i + (current_disp*100) ).question!!, Gravity.TOP, 1)
                        answer_view[i] = widgetController.settings(answer_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i+ (current_disp*100) ).answer!!, Gravity.TOP, 1)
                        tableRow[i]?.addView(question_view[i],layoutParams)
                        tableRow[i]?.addView(answer_view[i],layoutParams)
                        tableRow[i]?.addView(checkBox[i],layoutParams)
                        varLayout.addView(tableRow[i])
                        if(answers.count()> 100 && i===99 || answers.count()==current_disp*100+i+1){
                            break;
                        }
                    }
                }
        }



        next_Button != widgetController.settings(next_Button,5f,5f,5f,5f,9f,"　→",Gravity.CENTER,0)
        delete_Button != widgetController.settings(delete_Button,5f,5f,5f,5f,9f,"削除",Gravity.CENTER,0)
        underRow.addView(back_Button,layoutParams3)
        underRow.addView(next_Button,layoutParams3)
        underRow.addView(delete_Button,layoutParams3)
        underRow.setBackgroundColor(Color.rgb(147,112,219))
        under_layout.addView(underRow)

        //上中下段、全て適用させる。
        parent_layout.addView(over_layout)
        parent_layout.addView(scrollView)
        parent_layout.addView(under_layout)
        frame_linear_layout.addView(parent_layout)
        setContentView(frame_linear_layout)

    }
}



