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
        //DbAccess関連のインスタンス生成
        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase
        val wordAccess = WordAccess()

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
        val min_adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_item,min)
        val max_adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_item,max)
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
        val favorite: Array<LottieAnimationView?> = arrayOfNulls<LottieAnimationView>(100)
        val answer_ids =ArrayList<Int>()
        var answers =ArrayList<AnswerView>()

        search_Button.setOnClickListener{
            val max =max_spinner.selectedItem as Int
            val min =min_spinner.selectedItem as Int
            answers = wordAccess.getAnswers(db!!,min,max,0)
            //初期検索は確実に0件
            current_disp=0

            for(i in 0..99){
                tableRow[i]?.removeAllViews()
                answer_ids?.clear()
            }
            for(i in 0..answers.count()-1){
                tableRow[i]= TableRow(this)
                tableRow[i]?.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                answer_view[i] = TextView(this)
                favorite[i] = LottieAnimationView(this)
                favorite[i]?.setAnimation("favorite-app-icon.json")
                if(answers.get(i + (current_disp*100) ).favorite!!) {
                    favorite[i]?.progress =80F
                }
                favorite[i]?.setOnClickListener(){
                    if(answers.get(i + (current_disp*100) ).favorite!!) {
                        favorite[i]?.progress =0F
                        answers.get(i + (current_disp*100) ).favorite = false
                        helper!!.answer_update_fav(db!!,answers.get(i + (current_disp*100) ).answerview_id,false)
                    }else{
                        favorite[i]?.playAnimation()
                        answers.get(i + (current_disp*100) ).favorite = true
                        helper!!.answer_update_fav(db!!,answers.get(i + (current_disp*100) ).answerview_id,true)
                    }
                }
                question_view[i] = TextView(this)
                checkBox[i] = CheckBox(this)
                question_view[i] = widgetController.settings(question_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i + (current_disp*100) ).question!!, Gravity.TOP, 1)
                answer_view[i] = widgetController.settings(answer_view[i]!!, 10f, 10f, 10f, 10f, 6f, answers.get(i+ (current_disp*100) ).answer!!, Gravity.TOP, 1)
                tableRow[i]?.addView(question_view[i],layoutParams)
                tableRow[i]?.addView(answer_view[i],layoutParams)
                tableRow[i]?.addView(checkBox[i],layoutParams)
                tableRow[i]?.addView(favorite[i],layoutParams)

                varLayout.addView(tableRow[i])

                answer_ids.add(answers.get(i + (current_disp*100) ).answerview_id)
                current_answer_num=i

                if(answers.count()> 100 && i==99){
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
        next_Button != widgetController.settings(next_Button,5f,5f,5f,5f,9f,"　→",Gravity.CENTER,0)
        delete_Button != widgetController.settings(delete_Button,5f,5f,5f,5f,9f,"削除",Gravity.CENTER,0)

        //100件以上取得した時に、値を表示する処理。前の100件に戻る。
        back_Button.setOnClickListener{
            if(current_disp!=0){
                current_disp+=-1
                answer_ids.clear()
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

                    answer_ids.add(answers.get(i + (current_disp*100) ).answerview_id)

                    if(answers.count()> 100 && i==99 || answers.count()==current_disp*100+i+1){
                        current_answer_num = i
                        break;
                    }
                }
            }
        }
        //100件以上取得した時に値を表示する処理。次の100件に進む。
        next_Button.setOnClickListener(){
            if(!answers.isEmpty() && current_disp!= ceil(answers.count()/100.toDouble()).toInt()-1){
                    current_disp+=1
                    answer_ids.clear()
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

                        answer_ids.add(answers.get(i + (current_disp*100) ).answerview_id)

                        if(answers.count()> 100 && i==99 || answers.count()==current_disp*100+i+1){
                            current_answer_num = i
                            break;
                        }
                    }
                }
        }
        delete_Button.setOnClickListener(){
            var dele_answers = ArrayList<Int>()
            var dialog =Db_DialogFragment()
            dialog.title="データの削除"
            for (i in 0..current_answer_num) {
                if (checkBox[i]?.isChecked() == true) {
                    dele_answers.add(answer_ids[i])
                }
            }
            dialog.message = dele_answers.count().toString()+"個の韻を削除します"
            dialog.onOkClickListener = DialogInterface.OnClickListener { dialog, id ->
                helper!!.answer_delete(db!!,dele_answers)
                for(i in 0..99){
                    tableRow[i]?.removeAllViews()
                }
            }
            dialog.show(fragmentManager,"sample")

        }



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



