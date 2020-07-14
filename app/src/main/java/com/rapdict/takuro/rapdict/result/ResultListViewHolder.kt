package com.rapdict.takuro.rapdict.result

import android.text.Editable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rapdict.takuro.rapdict.Common.CustomTextWatcher
import com.rapdict.takuro.rapdict.databinding.ListResultWriteBinding

class ResultListViewHolder(val binding: ListResultWriteBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
    init {
        binding.resultListRegisterAnswer.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                binding.data!!.textList[binding.position!!].value = p0.toString()
            }
        })

    }
}