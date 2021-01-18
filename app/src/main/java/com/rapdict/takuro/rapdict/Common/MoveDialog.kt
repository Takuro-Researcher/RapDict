package com.rapdict.takuro.rapdict.Common


import android.content.Context
import androidx.appcompat.app.AlertDialog

class MoveDialog(context: Context, positiveMethod: () -> Unit,
                 negativeMethod: () -> Unit,
                 title: String, message: String) : AlertDialog(context) {

    private var _dialog: AlertDialog = Builder(context)
            .setTitle(title) // タイトル
            .setMessage(message) // メッセージ
            .setPositiveButton("OK") { dialogInterface, i ->
                positiveMethod()
            }
            .setNegativeButton("NO") { dialogInterface, i ->
                negativeMethod()
            }
            .create()

    val dialog: AlertDialog
        get() = _dialog
}