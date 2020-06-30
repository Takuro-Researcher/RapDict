package com.rapdict.takuro.rapdict.myDict

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingBinding
import com.rapdict.takuro.rapdict.databinding.FragmentMydictChoiceBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.model.Answer
import com.rapdict.takuro.rapdict.model.Mydict
import kotlinx.android.synthetic.main.fragment_mydict_choice.*
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel

class MyDictChoiceFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictChoiceBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMydictChoiceBinding.inflate(inflater, container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val myDictChoiceViewModel:MyDictChoiceViewModel by viewModel()
        binding?.data = myDictChoiceViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myDictChoiceViewModel : MyDictChoiceViewModel by viewModel()
        var count:Int =-1
        runBlocking {
            val dao = db.mydictDao()
            count = dao.count()
        }
        if(count>0){ myDictChoiceViewModel.init_load() }


        mydict_choice_spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                myDictChoiceViewModel.changed_uid(mydict_choice_spinner.selectedItemPosition)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        mydict_delete_button.setOnClickListener {
            val backIntent = Intent(activity,MainActivity::class.java)
            val mydict_name = mydict_choice_spinner.selectedItem as String
            val dialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("辞書【"+mydict_name+"】を削除する")
                setMessage("※作った言葉はすべて消えます")
                setPositiveButton("OK",{_, _ ->
                    val id:Int = myDictChoiceViewModel.db_uid.value!!
                    runBlocking {
                        val dao = db.mydictDao()
                        dao.deleteByIds(id)
                    }
                    startActivity(backIntent)
                })
                setNegativeButton("NO",null)
            }
            dialog.show()
        }
    }
}