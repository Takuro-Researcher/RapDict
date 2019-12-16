package com.rapdict.takuro.rapdict

import android.widget.TextView
import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import sample.intent.AnswerData


class ResultListAdapter(val context: Context,
                        val items: Array<AnswerData>) : BaseAdapter() {
    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): AnswerData {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(com.rapdict.takuro.rapdict.R.layout.result_list,parent,false)
        val answer = items[position]
        view.findViewById<TextView>(com.rapdict.takuro.rapdict.R.id.result_list_row).text = answer.question
        view.findViewById<TextView>(com.rapdict.takuro.rapdict.R.id.result_list_answer).text = answer.answer
        return view
    }
}

