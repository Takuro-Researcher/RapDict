package com.rapdict.takuro.rapdict.myDict

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.ListDisplayBinding
import kotlinx.coroutines.runBlocking


open class DisplayListAdapter(private val viewModel : DisplayListViewModel, private val parentLifecycleOwner: LifecycleOwner) : androidx.recyclerview.widget.RecyclerView.Adapter<DisplayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayListViewHolder {
        val binding = DataBindingUtil.inflate<ListDisplayBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_display,
                parent,
                false
        )
        return DisplayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisplayListViewHolder, position: Int) {
        holder.binding.data = this.viewModel

        holder.binding.position = position


        //ここでviewholderのlifecycleOwnerにセットする！
        holder.binding.displayCard.setOnLongClickListener {
            var adapter =this
            val saveDialog = AlertDialog.Builder(holder.binding.root.context).apply{
                setCancelable(false)
                setTitle("選択した単語を削除")
                setPositiveButton("OK") { _, _ ->
                    runBlocking {
                        val dao = db.wordDao()
                        dao.deleteByIds(viewModel.wordUidList[position].value!!)
                        viewModel.clear_list(position)
                        adapter.notifyDataSetChanged()
                    }
                }
                setNegativeButton("NO",null)
            }
            saveDialog.show()

            true // trueを返す
        }
        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }

}

