package com.rapdict.takuro.rapdict

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.content.DialogInterface
import android.widget.Toast
import android.widget.TextView

class Db_DialogFragment : DialogFragment() {
    //インスタンス側で変更可能な値
    var title = "title"
    var message = "message"
    var okText = "OK"
    var cancelText = "cancel"
    var onOkClickListener : DialogInterface.OnClickListener? = null
    var onCancelClickListener : DialogInterface.OnClickListener? = DialogInterface.OnClickListener { _, _ -> }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(okText, onOkClickListener)
                .setNegativeButton(cancelText, onCancelClickListener)
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        // onPause でダイアログを閉じる場合
        dismiss()
    }
}