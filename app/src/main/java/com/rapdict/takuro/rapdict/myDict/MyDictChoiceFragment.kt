package com.rapdict.takuro.rapdict.myDict

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.databinding.FragmentMydictChoiceBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.fragment_mydict_choice.*
import kotlinx.coroutines.runBlocking

class MyDictChoiceFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictChoiceBinding?= null
    private var viewModel:MyDictChoiceViewModel? =null
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
        val myDictChoiceViewModel = ViewModelProviders.of(parentFragment!!).get(MyDictChoiceViewModel::class.java)
        binding?.data = myDictChoiceViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(parentFragment!!).get(MyDictChoiceViewModel::class.java)

        mydict_delete_button.setOnClickListener {
            val backIntent = Intent(activity,MainActivity::class.java)
            val mydict_name = mydict_choice_spinner.selectedItem as String
            val dialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("単語帳【"+mydict_name+"】を削除する")
                setMessage("※作った言葉はすべて消えます")
                setPositiveButton("OK",{_, _ ->
                    val id:Int = viewModel!!.db_uid.value!!
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

    override fun onResume() {
        super.onResume()

        // null値でonItemSelectedが起動しないように初回起動しないようにした。本来こっちのほうがいい？
        val mListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                viewModel!!.changed_uid(mydict_choice_spinner.selectedItemPosition)
                viewModel!!.countChange(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        mydict_choice_spinner.setSelection(viewModel!!.choiceDictNamePosition,false)
        mydict_choice_spinner.onItemSelectedListener = mListener
    }


}