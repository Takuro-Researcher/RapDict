package com.rapdict.takuro.rapdict.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.ResultListBinding


open class ResultListAdapter(private val viewModel : ResultListViewModel, private val parentLifecycleOwner: LifecycleOwner) : androidx.recyclerview.widget.RecyclerView.Adapter<ResultListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_list,parent,false)
        val binding = DataBindingUtil.inflate<ResultListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.result_list,
                parent,
                false
        )
        return ResultListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultListViewHolder, position: Int) {
        holder.binding.data = this.viewModel
        holder.binding.position = position

        var checkbox =holder.binding.checkboxSave
        holder.binding.checkboxSave.setOnClickListener {
            if(viewModel.checkedList[position].value!!){
                viewModel.checkedList[position].value = false
            }else{
                viewModel.checkedList[position].value = true
            }
        }

        //ここでviewholderのlifecycleOwnerにセットする！
        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }
}