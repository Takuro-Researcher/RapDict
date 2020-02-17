package com.rapdict.takuro.rapdict.dict

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import apps.test.marketableskill.biz.recyclerview.ListAdapter
import com.rapdict.takuro.rapdict.*
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import com.rapdict.takuro.rapdict.helper.WordAccess
import kotlinx.android.synthetic.main.activity_dict.*
import kotlinx.android.synthetic.main.content_list.*
import kotlin.collections.ArrayList


class DictActivity : AppCompatActivity() {
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

        val recomdialog =AlertDialog.Builder(this)
        recomdialog.setCancelable(false)
        recomdialog.setMessage("韻を踏みに行きましょう")
        recomdialog.setPositiveButton("戻る"){
            _, _ -> startActivity(recomIntent)
        }

        if (lengthWords.size==0 || lengthWords.size ==1) {
            max = 10.toFloat()
            min = 1.toFloat()
            recomdialog.show()
        }
        //韻呼び出し
        range_progress_seek_bar.setRange(min!!, max!!,1.0f)
        val answerList:ArrayList<AnswerView> = wordAccess.getAnswers(db!!,0,30,2)
        bindData(answerList)

        // Adapter作成
        val adapter = ListAdapter(this,rhymeData)

        // RecyclerViewにAdapterとLayoutManagerの設定
        RecyclerView.adapter =adapter
        RecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)


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
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
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
            val rhyme = RhymeData(answer.answer.toString(), answer.question.toString(), answer.favorite!!, answer.answerview_id)
            rhymeData.add(rhyme)
        }
    }

    private fun getSearchFav(id:Int):Int{
        if (id== R.id.withoutFav){
            return 0
        }else if(id == R.id.onlyFav){
            return 1
        }
        return 2
    }

}



