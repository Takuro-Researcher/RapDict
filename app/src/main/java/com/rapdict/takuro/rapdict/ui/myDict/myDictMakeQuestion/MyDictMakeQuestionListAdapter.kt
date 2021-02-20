package com.rapdict.takuro.rapdict.ui.myDict.myDictMakeQuestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rapdict.takuro.rapdict.databinding.ListQuestionBinding

private object DiffCallback : DiffUtil.ItemCallback<MyDictMakeWordData>() {
    override fun areItemsTheSame(oldItem: MyDictMakeWordData, newItem: MyDictMakeWordData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyDictMakeWordData, newItem: MyDictMakeWordData): Boolean {
        return oldItem == newItem
    }
}

class MyDictMakeQuestionListAdapter(private val viewModel: MyDictMakeQuestionViewModel,
                                    private val parentLifecycleOwner: LifecycleOwner
) : androidx.recyclerview.widget.ListAdapter<MyDictMakeWordData, MyDictMakeQuestionListAdapter.MyDictQuestionViewHolder>(DiffCallback) {

    class MyDictQuestionViewHolder(private val binding: ListQuestionBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyDictMakeWordData, viewLifecycleOwner: LifecycleOwner, viewModel: MyDictMakeQuestionViewModel) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                question = item
                data = viewModel
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDictQuestionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyDictQuestionViewHolder(ListQuestionBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MyDictQuestionViewHolder, position: Int) {
        // これ何故か、getItem(position)ではエラーになる。
        val data = viewModel.myDictMakeWords.value?.get(position)
        holder.bind(data!!, parentLifecycleOwner, viewModel)
    }

    override fun getItemCount(): Int {
        return viewModel.myDictMakeWords.value?.size ?: 0
    }

}

