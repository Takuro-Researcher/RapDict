package com.rapdict.takuro.rapdict.myDict.myDictDisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentMydictChoiceBinding
import com.rapdict.takuro.rapdict.databinding.FragmentMydictDisplayBinding
import com.rapdict.takuro.rapdict.databinding.FragmentMydictQuestionMakeBinding
import com.rapdict.takuro.rapdict.myDict.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_mydict_display.*

class MyDictDisplayFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictDisplayBinding? = null
    private val myDictChoiceViewModel : MyDictChoiceViewModel by activityViewModels()
    private val myDictDisplayViewModel: MyDictDisplayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMydictDisplayBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = MyDictDisplayListAdapter(myDictDisplayViewModel, this)
        binding?.data = myDictDisplayViewModel

        DisplayRecyclerView.adapter = adapter
        DisplayRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        myDictDisplayViewModel.myDictDisplayWords.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.submitList(it)
            myDictChoiceViewModel.count.value = it.size.toString()
        })
        val uid: Int = myDictChoiceViewModel.db_uid.value!!
        myDictDisplayViewModel.bindData(uid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}