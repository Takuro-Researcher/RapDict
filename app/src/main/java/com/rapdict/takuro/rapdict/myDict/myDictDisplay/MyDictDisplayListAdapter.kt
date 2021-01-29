package com.rapdict.takuro.rapdict.myDict.myDictDisplay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rapdict.takuro.rapdict.databinding.ListDisplayBinding


private object DiffCallback : DiffUtil.ItemCallback<MyDictDisplayWordData>() {
    override fun areItemsTheSame(oldItem: MyDictDisplayWordData, newItem: MyDictDisplayWordData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyDictDisplayWordData, newItem: MyDictDisplayWordData): Boolean {
        return oldItem == newItem
    }
}

class MyDictDisplayListAdapter(private val viewModel: MyDictDisplayViewModel,
                               private val parentLifecycleOwner: LifecycleOwner
) : androidx.recyclerview.widget.ListAdapter<MyDictDisplayWordData, MyDictDisplayListAdapter.MyDictDisplayViewHolder>(DiffCallback) {

    class MyDictDisplayViewHolder(private val binding: ListDisplayBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyDictDisplayWordData, viewLifecycleOwner: LifecycleOwner, viewModel: MyDictDisplayViewModel,position: Int) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                displayWord = item
                this.position = position
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDictDisplayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyDictDisplayViewHolder(ListDisplayBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MyDictDisplayViewHolder, position: Int) {
        holder.bind(getItem(position), parentLifecycleOwner, viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModel.myDictDisplayWords.value?.size ?: 0
    }

}

