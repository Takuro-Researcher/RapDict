package com.rapdict.takuro.rapdict.myDict

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.tabs.TabLayout
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.database.Mydict
import com.rapdict.takuro.rapdict.databinding.FragmentMydictMakeBinding
import com.rapdict.takuro.rapdict.game.GamePlayFragment
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.fragment_mydict1.*
import kotlinx.android.synthetic.main.fragment_mydict_make.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        val backIntent = Intent(activity,MainActivity::class.java)
        mydict_register_button.setOnClickListener {
            val text:String = mydict_name_edit.text.toString()
            val mydict = Mydict(0,text)
            val alertDialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("【"+mydict.name+"】辞書保存")
                setMessage("※画面移動します")
                setPositiveButton("OK",{_, _ ->
                    GlobalScope.launch {
                        val dao = db.mydictDao()
                        dao.insert(mydict)
                    }
                    var fragment = parentFragment as MyDictFragment
                    fragment.mydict_tab_layout?.getTabAt(1)?.select()
                    fragment.adapterAble(4)



                })
                setNegativeButton("NO",null)
            }
            alertDialog.show()
        }
    }
}