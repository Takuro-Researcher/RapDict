package com.rapdict.takuro.rapdict

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import apps.test.marketableskill.biz.recyclerview.ListAdapter
import kotlinx.android.synthetic.main.activity_dict.*
import kotlinx.android.synthetic.main.content_list.*
import kotlin.collections.ArrayList


class Dict__Activity : AppCompatActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null
    private var rhymeData : ArrayList<RhymeData> = ArrayList<RhymeData>()


    @SuppressLint("NewApi", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict)


        //DbAccess関連のインスタンス生成
        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase
        val wordAccess = WordAccess()
        //検索用のレンジプログレスバーの設定
        range_progress_seek_bar.setIndicatorTextDecimalFormat("0")
        val lengthWords =wordAccess.getLengthMinMax(db!!)
        val max= lengthWords.max()?.toFloat()
        val min =lengthWords.min()?.toFloat()
        range_progress_seek_bar.setRange(min!!, max!!,1.0f)

        //韻呼び出し
        val answerList:ArrayList<AnswerView> = wordAccess.getAnswers(db!!,0,30,2)
        bindData(answerList)

        // Adapter作成
        val adapter = ListAdapter(this,rhymeData)

        // RecyclerViewにAdapterとLayoutManagerの設定
        RecyclerView.adapter =adapter
        RecyclerView.layoutManager == LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }
    private fun bindData(answerList:ArrayList<AnswerView>) {
        for (answer in answerList){
            val rhyme =RhymeData(answer.answer.toString(),answer.question.toString(),answer.favorite!!,answer.question_id)
            rhymeData.add(rhyme)
        }
    }

}



