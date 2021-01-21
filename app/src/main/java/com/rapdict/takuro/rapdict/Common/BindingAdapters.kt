package com.rapdict.takuro.rapdict.Common

import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView


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

interface OnItemSelectedListener {
    fun onItemSelected(i: Int)
}

@BindingAdapter("android:onChangeSpinner")
fun Spinner.setOnItemSelected(listener: OnItemSelectedListener) {
    if (listener == null) return
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener.onItemSelected(position)
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
}

@BindingAdapter("android:changeFavoColor")
fun changeFavoColor(view: LottieAnimationView, bool: Boolean) {
    if (bool) {
        view.playAnimation()
    } else {
        view.progress = 0F
    }
}
