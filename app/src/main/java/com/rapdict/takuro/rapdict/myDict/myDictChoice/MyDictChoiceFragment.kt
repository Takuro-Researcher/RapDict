package com.rapdict.takuro.rapdict.myDict.myDictChoice

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.databinding.FragmentMydictChoiceBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.fragment_mydict_choice.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyDictChoiceFragment : androidx.fragment.app.Fragment() {
    private var binding :FragmentMydictChoiceBinding ?= null
    private val viewModel: MyDictChoiceViewModel by activityViewModels()
    private lateinit var deleteDialog :AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val backIntent = Intent(activity,MainActivity::class.java)
        deleteDialog = AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setNegativeButton("NO",null)
            setMessage("※作った言葉はすべて消えます")
            setPositiveButton("OK") { _, _ ->
                viewModel.removeMyDict()
                startActivity(backIntent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMydictChoiceBinding.inflate(inflater, container,false)
        binding?.lifecycleOwner = this
        return binding?.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.data = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dbUid.observe(viewLifecycleOwner, Observer<Int> {
            // 辞書の表示文字を変更するためのメソッド
            viewModel.countChange()
            // ダイアログ内の辞書名を変更する
            val message = "単語帳【"+viewModel.useDict+"】を削除する"
            deleteDialog.setMessage(message)
        })
        mydict_delete_button.setOnClickListener {
            deleteDialog.show()
        }
    }

}