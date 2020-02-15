package com.rapdict.takuro.rapdict


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.item_list.view.*

class ListViewHolder(mView : View) : RecyclerView.ViewHolder(mView) {

    val rhymeText: TextView = mView.rhyme_text
    val rawText: TextView = mView.raw_text
    val favorite: LottieAnimationView = mView.favorite_star
    val card:LinearLayout =mView.card
}