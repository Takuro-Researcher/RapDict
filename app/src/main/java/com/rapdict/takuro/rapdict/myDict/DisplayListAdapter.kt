package com.rapdict.takuro.rapdict.myDict

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.DisplayListBinding


open class DisplayListAdapter(private val viewModel : DisplayListViewModel, private val parentLifecycleOwner: LifecycleOwner) : androidx.recyclerview.widget.RecyclerView.Adapter<DisplayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayListViewHolder {
        val binding = DataBindingUtil.inflate<DisplayListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.display_list,
                parent,
                false
        )
        return DisplayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisplayListViewHolder, position: Int) {
        holder.binding.data = this.viewModel
        holder.binding.position = position
        //ここでviewholderのlifecycleOwnerにセットする！
        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }

}

