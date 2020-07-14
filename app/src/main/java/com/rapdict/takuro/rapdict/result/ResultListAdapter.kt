package com.rapdict.takuro.rapdict.result

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.Common.CustomTextWatcher
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.ListQuestionBinding
import com.rapdict.takuro.rapdict.databinding.ListResultBinding
import com.rapdict.takuro.rapdict.databinding.ListResultWriteBinding


open class ResultListAdapter(private val viewModel : ResultListViewModel, private val parentLifecycleOwner: LifecycleOwner) : androidx.recyclerview.widget.RecyclerView.Adapter<ResultListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListViewHolder {
        val binding = DataBindingUtil.inflate<ListResultWriteBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_result_write,
                parent,
                false
        )
        return ResultListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ResultListViewHolder, position: Int) {
        holder.binding.data = viewModel
        holder.binding.position = position
        holder.binding.checkboxSave.setOnClickListener {
            if(viewModel.checkedList[position].value!!){
                viewModel.checkedList[position].value = false
            }else{
                viewModel.checkedList[position].value = true
            }
        }
        holder.binding.questionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                viewModel.questionList[position].value = holder.binding.questionSpinner.selectedItem as String?
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }
}