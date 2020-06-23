package com.rapdict.takuro.rapdict.makeQuestion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.main.MainActivity
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
        val recomIntent  = Intent(activity!!, MainActivity::class.java)

        add_question_button.setOnClickListener {
            questionListViewModel.addCard()
            adapter.notifyDataSetChanged()
        }

        register_question_button.setOnClickListener {
            val q_list = questionListViewModel.questionList
            val f_list = questionListViewModel.furiganaList
            val word_list = ArrayList<Word>()

            f_list.zip(q_list).forEach {
                val furigana = it.first.value
                val question = it.second.value
                if(furigana!!.isEmpty() or question!!.isEmpty()){
                    return@forEach
                }
                word_list.add(Word(0, furigana,question,furigana.length))
            }

            System.out.println(word_list)

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