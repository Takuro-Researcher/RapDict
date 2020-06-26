package com.rapdict.takuro.rapdict.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.ResultListBinding
import com.rapdict.takuro.rapdict.databinding.ResultWriteListBinding


open class ResultListAdapter(private val viewModel : ResultListViewModel, private val parentLifecycleOwner: LifecycleOwner) : androidx.recyclerview.widget.RecyclerView.Adapter<ResultListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListViewHolder {
        var xml:Int
        if(viewType==1){
            xml = R.layout.result_write_list
        }else{
            xml = R.layout.result_list
        }
        val view = LayoutInflater.from(parent.context).inflate(xml,parent,false)
        return ResultListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ResultListViewHolder, position: Int) {
        var binding:ViewDataBinding
        if (viewModel.textList[position].value !=""){
            binding = holder.binding as ResultListBinding
            binding.data = this.viewModel
            binding.position = position
            binding.checkboxSave.setOnClickListener {
                if(viewModel.checkedList[position].value!!){
                    viewModel.checkedList[position].value = false
                }else{
                    viewModel.checkedList[position].value = true
                }
            }
            binding.lifecycleOwner = parentLifecycleOwner

        }else{
            binding = holder.binding as ResultWriteListBinding
            binding.data = this.viewModel
            binding.position = position
            binding.checkboxSave.setOnClickListener {
                if(viewModel.checkedList[position].value!!){
                    viewModel.checkedList[position].value = false
                }else{
                    viewModel.checkedList[position].value = true
                }
            }
        }
        
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }
//
    override fun getItemViewType(position: Int): Int {
        if (viewModel.textList[position].value == ""){
            return 1
        }else{
            return 0
        }
    }
}