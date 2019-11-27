package com.rapdict.takuro.rapdict

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
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
        var max = lengthWords.max()?.toFloat()
        var min =lengthWords.min()?.toFloat()
        val recomIntent  = Intent(this, MainActivity::class.java)

        val dialog =AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setMessage("韻を踏みに行きましょう")
        dialog.setPositiveButton("戻る"){
            dialog, which -> startActivity(recomIntent)
        }

        if (lengthWords.size==0 || lengthWords.size ==1) {
            max = 10.toFloat()
            min = 1.toFloat()
            dialog.show()
        }
        //韻呼び出し
        range_progress_seek_bar.setRange(min!!, max!!,1.0f)
        val answerList:ArrayList<AnswerView> = wordAccess.getAnswers(db!!,0,30,2)
        bindData(answerList)

        // Adapter作成
        val adapter = ListAdapter(this,rhymeData)

        // RecyclerViewにAdapterとLayoutManagerの設定
        RecyclerView.adapter =adapter
        RecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        //検索
        search_button.setOnClickListener {
            rhymeData.clear()
            val minVal =range_progress_seek_bar.leftSeekBar.progress.toInt()
            val maxVal =range_progress_seek_bar.rightSeekBar.progress.toInt()
            val selectedId = radioGroup.checkedRadioButtonId
            val searchWords =wordAccess.getAnswers(db!!,minVal,maxVal,getSearchFav(selectedId))
            bindData(searchWords)
            adapter.notifyDataSetChanged()
        }

        //スワイプ時の削除処理
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipePosition = viewHolder.adapterPosition
                adapter.rhymeRemove(swipePosition)
            }
        }

        //RecyclerViewにスワイプ処理をアタッチ
        val itemTouchHelper =ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(RecyclerView)
    }

    private fun bindData(answerList:ArrayList<AnswerView>) {
        for (answer in answerList){
            val rhyme =RhymeData(answer.answer.toString(),answer.question.toString(),answer.favorite!!,answer.answerview_id)
            rhymeData.add(rhyme)
        }
    }

    private fun getSearchFav(id:Int):Int{
        if (id==R.id.withoutFav){
            return 0
        }else if(id == R.id.onlyFav){
            return 1
        }
        return 2
    }

}



