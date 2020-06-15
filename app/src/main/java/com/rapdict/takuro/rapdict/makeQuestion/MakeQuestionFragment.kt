package com.rapdict.takuro.rapdict.makeQuestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentResultBinding
import kotlinx.android.synthetic.main.make_question_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MakeQuestionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.make_question_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        val questionListViewModel:QuestionListViewModel by viewModel()
        val adapter = QuestionListAdapter(questionListViewModel,this)
        QuestionRecyclerView.adapter = adapter
        QuestionRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
    }

}