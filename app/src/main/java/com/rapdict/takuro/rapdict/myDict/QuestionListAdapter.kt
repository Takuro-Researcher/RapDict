package com.rapdict.takuro.rapdict.myDict

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.Common.CustomTextWatcher
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.ListQuestionBinding


open class QuestionListAdapter(private val viewModel : QuestionListViewModel, private val parentLifecycleOwner: LifecycleOwner) : androidx.recyclerview.widget.RecyclerView.Adapter<QuestionListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListViewHolder {
        val binding = DataBindingUtil.inflate<ListQuestionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_question,
                parent,
                false
        )
        return QuestionListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionListViewHolder, position: Int) {
        holder.binding.data = viewModel
        holder.binding.position = position

        holder.binding.furiganaListDoc.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.furiganaList[position].value = p0.toString()
            }
        })
        holder.binding.questionListDoc.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.questionList[position].value = p0.toString()
            }
        })
        //ここでviewholderのlifecycleOwnerにセットする！
        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }

}

