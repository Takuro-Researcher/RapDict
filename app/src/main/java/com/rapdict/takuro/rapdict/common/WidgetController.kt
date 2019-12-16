package com.rapdict.takuro.rapdict.common

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class WidgetController(internal var context: Context) {

    fun settings(textView: TextView, padding_right: Float, padding_left: Float, padding_bottom: Float, padding_top: Float, textsize: Float, text: String, gravity: Int, width: Int): TextView {
        textView.setPadding(int_Dp2Px(padding_left, context), int_Dp2Px(padding_top, context), int_Dp2Px(padding_right, context), int_Dp2Px(padding_bottom, context))
        textView.textSize = Dp2Px(textsize, context)
        textView.width = width
        textView.gravity = gravity
        textView.text = text
        return textView
    }

    //EditTextのデザインを決める。
    fun settings(editText: EditText, padding_right: Float, padding_left: Float, padding_bottom: Float, padding_top: Float, textsize: Float, gravity: Int, width: Int): EditText {
        editText.setPadding(int_Dp2Px(padding_left, context), int_Dp2Px(padding_top, context), int_Dp2Px(padding_right, context), int_Dp2Px(padding_bottom, context))
        editText.textSize = Dp2Px(textsize, context)
        editText.gravity = gravity
        editText.width = width
        return editText
    }

    //Buttonのデザインを決める
    fun settings(button: Button, padding_right: Float, padding_left: Float, padding_bottom: Float, padding_top: Float, textsize: Float, text: String, gravity: Int, width: Int): Button {
        button.setPadding(int_Dp2Px(padding_left, context), int_Dp2Px(padding_top, context), int_Dp2Px(padding_right, context), int_Dp2Px(padding_bottom, context))
        button.textSize = Dp2Px(textsize, context)
        button.width = width
        button.gravity = gravity
        button.text = text
        return button
    }

    companion object {
        //Dpをピクセルに変換する関数
        fun Dp2Px(dp: Float, context: Context): Float {
            val metrics = context.resources.displayMetrics
            return dp * metrics.density
        }
        //Dpをピクセルに変換、int型に変換する。
        fun int_Dp2Px(dp: Float, context: Context): Int {
            val metrics = context.resources.displayMetrics
            val n = dp * metrics.density
            return n.toInt()
        }
    }
}
