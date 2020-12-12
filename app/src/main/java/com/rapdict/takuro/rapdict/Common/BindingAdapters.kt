package com.rapdict.takuro.rapdict.Common

import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.databinding.BindingAdapter


@BindingAdapter("android:layout_weight")
fun setLayoutWeight(view: View, weight: Float) {
    val layoutParams = view.layoutParams as? LinearLayout.LayoutParams
    layoutParams?.let {
        it.weight = weight
        view.layoutParams = it
    }
}

@BindingAdapter("android:OnItemSelected")
fun onItemSelected(view: Spinner, position: Int) {
    view.setSelection(position)
}
