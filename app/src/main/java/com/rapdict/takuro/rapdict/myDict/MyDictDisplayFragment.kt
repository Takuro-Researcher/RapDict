package com.rapdict.takuro.rapdict.myDict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentMydictChoiceBinding
import kotlinx.android.synthetic.main.fragment_mydict_display.*

class MyDictDisplayFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictChoiceBinding? = null
    private val myDictChoiceViewModel :MyDictChoiceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mydict_display, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val displayListViewModel: DisplayListViewModel by viewModels()
        val adapter = DisplayListAdapter(displayListViewModel, this)
        DisplayRecyclerView.adapter = adapter
        DisplayRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        val uid: Int = myDictChoiceViewModel.db_uid.value!!
        displayListViewModel.clear()
        displayListViewModel.mydict_bind(uid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}