package com.rapdict.takuro.rapdict.ui.myDict.myDictMake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.rapdict.takuro.rapdict.App.Companion.db
import com.rapdict.takuro.rapdict.databinding.FragmentMydictMakeBinding
import com.rapdict.takuro.rapdict.model.entity.Mydict
import com.rapdict.takuro.rapdict.ui.myDict.MyDictFragment
import com.rapdict.takuro.rapdict.ui.myDict.myDictChoice.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_mydict1.*
import kotlinx.android.synthetic.main.fragment_mydict_make.*
import kotlinx.coroutines.runBlocking


class MyDictMakeFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictMakeBinding? = null
    private val myDictChoiceViewModel: MyDictChoiceViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMydictMakeBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val myDictMakeViewModel: MyDictMakeViewModel by viewModels()
        binding?.data = myDictMakeViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dictMakeViewModel: MyDictMakeViewModel by viewModels()
        mydict_register_button.setOnClickListener {
            val text: String = mydict_name_edit.text.toString()
            val mydict = Mydict(0, text)
            val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                setCancelable(false)
                setTitle("【" + mydict.name + "】辞書保存")
                setMessage("※画面移動します")
                setPositiveButton("OK") { _, _ ->
                    runBlocking {
                        val dao = db.mydictDao()
                        dao.insert(mydict)
                        myDictChoiceViewModel.initLoad()
                    }
                    val fragment = parentFragment as MyDictFragment
                    // テキストボックスを空にする処理
                    dictMakeViewModel.dictName.value = ""
                    mydict_name_edit.text.clear()
                    fragment.mydict_tab_layout?.getTabAt(1)?.select()
                    fragment.adapterAble(4)

                }
                setNegativeButton("NO", null)
            }
            alertDialog.show()
        }
    }
}