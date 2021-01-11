package com.rapdict.takuro.rapdict.result

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rapdict.takuro.rapdict.Common.CustomTextWatcher
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.ListResultBinding
import com.rapdict.takuro.rapdict.databinding.ListResultWriteBinding

private object DiffCallback : DiffUtil.ItemCallback<AnswerData>() {
    override fun areItemsTheSame(oldItem: AnswerData, newItem: AnswerData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AnswerData, newItem: AnswerData): Boolean {
        return oldItem == newItem
    }

}

class ResultListAdapter(private val viewModel: ResultViewModel,
                        private val parentLifecycleOwner: LifecycleOwner
) : ListAdapter<AnswerData, ResultListAdapter.ResultViewHolder>(DiffCallback) {

    class ResultViewHolder(private val v:View ) :
            RecyclerView.ViewHolder(v.rootView) {
        val binding:ViewDataBinding = DataBindingUtil.bind(v)!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val xml: Int
        if (viewType == 1) {
            xml = R.layout.list_result_write
        } else {
            xml = R.layout.list_result
        }
        val view = LayoutInflater.from(parent.context).inflate(xml, parent, false)
        return ResultViewHolder(view)

    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val binding: ViewDataBinding
        val viewType = getItemViewType(position)
        if (viewType == 1) {
            binding = holder.binding as ListResultWriteBinding
            binding.data = viewModel
            binding.answer = getItem(position)
            binding.position = position
        } else {
            binding = holder.binding as ListResultBinding
            binding.data = viewModel
            binding.answer = getItem(position)
            binding.position = position

        }
        binding.lifecycleOwner = parentLifecycleOwner
        binding.executePendingBindings()
    }



    override fun getItemCount(): Int {
        return viewModel.answers.value?.size ?: 0
    }

    //
    override fun getItemViewType(position: Int): Int {
        val answer = viewModel.answers.value?.get(position) ?: AnswerData(1)
        if (answer.isAdd) {
            return 1
        } else {
            return 0
        }
    }
}