package com.rapdict.takuro.rapdict.myDict

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CustomTextWatcher
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentMydictMakeBinding
import com.rapdict.takuro.rapdict.databinding.FragmentUserSettingBinding
import com.rapdict.takuro.rapdict.model.Mydict
import com.rapdict.takuro.rapdict.userSetting.UserSettingViewModel
import kotlinx.android.synthetic.main.fragment_mydict_make.*
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel


class MyDictMakeFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictMakeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMydictMakeBinding.inflate(inflater, container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val myDictMakeViewModel: MyDictMakeViewModel by viewModel()
        binding?.data= myDictMakeViewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mydict_register_button.setOnClickListener {
            val text:String = mydict_name_edit.text.toString()
            val mydict = Mydict(0,text)
            val alertDialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("データ保存")
                setMessage("【"+mydict.answer+"】辞書保存")
                setPositiveButton("OK",{_, _ ->
                    runBlocking {
                        val dao = db.mydictDao()
                        dao.insert(mydict)
                    }
                })
                setNegativeButton("NO",null)
            }
            alertDialog.show()
        }
    }
}