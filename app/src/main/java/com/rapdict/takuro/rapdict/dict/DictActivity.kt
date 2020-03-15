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
import kotlinx.android.synthetic.main.item_list.*
import org.koin.android.viewmodel.ext.android.viewModel


class DictActivity : AppCompatActivity() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null
    lateinit var mItemListViewModel: ListViewModel


    @SuppressLint("NewApi", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict)


        //DbAccess関連のインスタンス生成
        helper = SQLiteOpenHelper(applicationContext)
        db = helper!!.writableDatabase
        val answerView = AnswerView()
        //検索用のレンジプログレスバーの設定
        range_progress_seek_bar.setIndicatorTextDecimalFormat("0")
        val lengthWords =answerView.getLengthMinMax(db!!)
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

        // Adapter作成
        val itemListViewModel:ListViewModel by viewModel()
        val adapter = ListAdapter(itemListViewModel,this)

        // RecyclerViewにAdapterとLayoutManagerの設定
        RecyclerView.adapter =adapter
        RecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)


        //検索
        search_button.setOnClickListener {
            val minVal =range_progress_seek_bar.leftSeekBar.progress.toInt()
            val maxVal =range_progress_seek_bar.rightSeekBar.progress.toInt()
            val selectedId = radioGroup.checkedRadioButtonId
            val searchWords =answerView.getAnswers(db!!,minVal,maxVal,AnswerView.getSearchFav(selectedId))
            itemListViewModel.bindAnswer(searchWords)
            adapter.notifyDataSetChanged()
        }

        //スワイプ時の削除処理
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
                val swipePosition = viewHolder.adapterPosition
                adapter.notifyItemRemoved(swipePosition)
                val deleteId = itemListViewModel.idList.get(swipePosition)
                itemListViewModel.removeAnswer(swipePosition)
                adapter.notifyItemRemoved(swipePosition)
//                helper?.answer_delete(db!!,rhymeData.deleteId)
            }
        }

        //RecyclerViewにスワイプ処理をアタッチ
        val itemTouchHelper =ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(RecyclerView)
    }

}



