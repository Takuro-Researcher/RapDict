package com.rapdict.takuro.rapdict.result

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class ResultListViewHolder(v:View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v.rootView){
    var binding:ViewDataBinding = DataBindingUtil.bind(v)!!
}