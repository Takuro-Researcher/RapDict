package com.rapdict.takuro.rapdict.myDict

import android.text.Editable
import com.rapdict.takuro.rapdict.Common.CustomTextWatcher
import com.rapdict.takuro.rapdict.databinding.ListQuestionBinding


class QuestionListViewHolder(val binding: ListQuestionBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
    init {
        binding.furiganaListDoc.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                binding.data!!.furiganaList[binding.position!!].value = p0.toString()
            }
        })
        binding.questionListDoc.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                binding.data!!.questionList[binding.position!!].value = p0.toString()
            }
        })
    }
}