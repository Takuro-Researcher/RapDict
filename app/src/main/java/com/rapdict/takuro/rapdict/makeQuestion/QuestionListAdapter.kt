package com.rapdict.takuro.rapdict.makeQuestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.QuestionListBinding
import com.rapdict.takuro.rapdict.databinding.ResultListBinding


open class QuestionListAdapter(private val viewModel : QuestionListViewModel, private val parentLifecycleOwner: LifecycleOwner) : androidx.recyclerview.widget.RecyclerView.Adapter<QuestionListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListViewHolder {
        val binding = DataBindingUtil.inflate<QuestionListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.question_list,
                parent,
                false
        )
        return QuestionListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionListViewHolder, position: Int) {
        holder.binding.data = this.viewModel
        holder.binding.position = position

        //ここでviewholderのlifecycleOwnerにセットする！
        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }
}