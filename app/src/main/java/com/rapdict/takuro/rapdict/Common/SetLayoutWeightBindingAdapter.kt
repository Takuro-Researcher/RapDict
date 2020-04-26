package com.rapdict.takuro.rapdict.Common

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter

object SetLayoutWeightBindingAdapter {
    @BindingAdapter("android:layout_weight")
    @JvmStatic
    fun setLayoutWeight(view: View, weight: Float) {
        val layoutParams = view.layoutParams as? LinearLayout.LayoutParams
        layoutParams?.let {
            it.weight = weight
            view.layoutParams = it
        }
    }
}