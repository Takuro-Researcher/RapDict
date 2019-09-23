package com.rapdict.takuro.rapdict


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_list.view.*

class ListViewHolder(mView : View) : RecyclerView.ViewHolder(mView) {

    val rhyme_text : TextView = mView.rhyme_text
    val raw_text : TextView = mView.raw_text

}