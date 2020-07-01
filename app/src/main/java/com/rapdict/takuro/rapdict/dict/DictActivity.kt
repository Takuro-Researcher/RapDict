package com.rapdict.takuro.rapdict.dict

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import apps.test.marketableskill.biz.recyclerview.ListAdapter
import com.rapdict.takuro.rapdict.*
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.activity_dict.*
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel


class DictActivity : AppCompatActivity() {

    @SuppressLint("NewApi", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict)

        //検索用のレンジプログレスバーの設定
        range_progress_seek_bar.setIndicatorTextDecimalFormat("0")
        val recomIntent  = Intent(this, MainActivity::class.java)

        val recomdialog =AlertDialog.Builder(this)
        recomdialog.setCancelable(false)
        recomdialog.setMessage("韻を踏みに行きましょう")
        recomdialog.setPositiveButton("戻る"){
            _, _ -> startActivity(recomIntent)
        }
        //韻呼び出し
        range_progress_seek_bar.setRange(0F, 20F,1.0f)

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
            var search :List<Int> = listOf()
            when(selectedId){
                R.id.withoutFav ->{ search =listOf(0) }
                R.id.onlyFav ->{ search =listOf(1) }
                R.id.flatFav ->{ search =listOf(0,1) }
            }
            runBlocking {
                val dao = App.db.answerDao()
                val searchWords = dao.findByLenght(minVal,maxVal,search)
                itemListViewModel.bindAnswer(searchWords)
                adapter.notifyDataSetChanged()
            }
        }

        //スワイプ時の削除処理
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
                // UIの更新
                val swipePosition = viewHolder.adapterPosition

                val deleteId = itemListViewModel.idList.get(swipePosition).value
                itemListViewModel.removeAnswer(swipePosition)
                adapter.notifyItemRemoved(swipePosition)
                // テーブルから削除
                runBlocking {
                    val dao = App.db.answerDao()
                    deleteId?.let { dao.deleteByIds(it) }
                }
            }
        }

        //RecyclerViewにスワイプ処理をアタッチ
        val itemTouchHelper =ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(RecyclerView)
    }

}



