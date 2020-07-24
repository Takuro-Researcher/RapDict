package com.rapdict.takuro.rapdict.myDict

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.fragment_mydict1.*
import kotlinx.android.synthetic.main.fragment_mydict_question_make.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
            var db_name =""
            runBlocking {
                val dao = db.mydictDao()
                val data =dao.findOneByIds(myDictChoiceViewModel.db_uid.value!!)
                db_name =data.name!!
            }

            val q_list = questionListViewModel.questionList
            val f_list = questionListViewModel.furiganaList
            val word_list = ArrayList<Word>()
            var furiganaAttention = false
            var furiganaMsg =""
            f_list.zip(q_list).forEach {
                var furigana = it.first.value
                var question = it.second.value
                if(question!!.isEmpty()){
                    return@forEach
                }
                if(furigana!!.isEmpty()){
                    furiganaAttention = true
                    furigana = question
                }
                word_list.add(Word(0, furigana,question,furigana.length,myDictChoiceViewModel.db_uid.value))
            }


            if(furiganaAttention){ furiganaMsg ="読み方が空のものがあります。空の場合、好きな言葉を読み方に適用します"}

            val saveDialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("問題保存【"+db_name+"】")
                setMessage(word_list.size.toString()+"個、自分の問題として保存\n好きな言葉が空のものは保存されません\n"+furiganaMsg+"\n※画面移動します")
                setPositiveButton("OK",{_, _ ->
                    runBlocking {
                        val dao = db.wordDao()
                        word_list.forEach { dao.insert(it) }
                        myDictChoiceViewModel.count_visible()
                    }
                    questionListViewModel.clear()
                    
                    val fragment = parentFragment as MyDictFragment
                    fragment.mydict_tab_layout?.getTabAt(1)?.select()
                })
                setNegativeButton("NO",null)
            }
            saveDialog.show()
        }
    }

}