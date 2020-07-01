package com.rapdict.takuro.rapdict.myDict

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.fragment_mydict_question_make.*
import org.koin.android.viewmodel.ext.android.viewModel

class MyDictMakeQuestionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mydict_question_make, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        val questionListViewModel:QuestionListViewModel by viewModel()
        val adapter = QuestionListAdapter(questionListViewModel,this)
        val myDictChoiceViewModel = ViewModelProviders.of(parentFragment!!).get(MyDictChoiceViewModel::class.java)

        QuestionRecyclerView.adapter = adapter
        QuestionRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        val recomIntent  = Intent(activity!!, MainActivity::class.java)

        add_question_button.setOnClickListener {
            questionListViewModel.addCard()
            adapter.notifyItemInserted(adapter.itemCount)
        }

        register_question_button.setOnClickListener {
            val q_list = questionListViewModel.questionList
            val f_list = questionListViewModel.furiganaList
            val word_list = ArrayList<Word>()
            System.out.println(myDictChoiceViewModel.db_uid.value)

            f_list.zip(q_list).forEach {
                val furigana = it.first.value
                val question = it.second.value
                if(furigana!!.isEmpty() or question!!.isEmpty()){
                    return@forEach
                }
                word_list.add(Word(0, furigana,question,furigana.length,myDictChoiceViewModel.db_uid.value))
            }
            val saveDialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("問題保存")
                setMessage(word_list.size.toString()+"個、自分の問題として保存\nどちらか空白の場合保存できません")
                setPositiveButton("OK",{_, _ ->
                    //TODO 保存動作を確認
                    startActivity(recomIntent)
                })
                setNegativeButton("NO",null)
            }
            saveDialog.show()
        }
    }

}