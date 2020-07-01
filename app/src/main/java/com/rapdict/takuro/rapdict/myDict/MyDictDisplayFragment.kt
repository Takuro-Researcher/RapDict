package com.rapdict.takuro.rapdict.myDict

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentMydictChoiceBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.display_list.*
import kotlinx.android.synthetic.main.fragment_mydict_choice.*
import kotlinx.android.synthetic.main.fragment_mydict_display.*
import kotlinx.android.synthetic.main.fragment_mydict_question_make.*
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel

class MyDictDisplayFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictChoiceBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mydict_display,container,false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val displayListViewModel:DisplayListViewModel by viewModel()
        val adapter = DisplayListAdapter(displayListViewModel,this)
        val myDictChoiceViewModel = ViewModelProviders.of(parentFragment!!).get(MyDictChoiceViewModel::class.java)
        DisplayRecyclerView.adapter = adapter
        DisplayRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        val uid:Int = myDictChoiceViewModel.db_uid.value!!
        displayListViewModel.clear()
        displayListViewModel.mydict_bind(uid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}