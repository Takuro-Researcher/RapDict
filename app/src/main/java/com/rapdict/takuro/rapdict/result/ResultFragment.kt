package com.rapdict.takuro.rapdict.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.databinding.FragmentResultBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.model.Answer
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ResultFragment : androidx.fragment.app.Fragment() {
    // TODO: Rename and change types of parameters

    private var binding:FragmentResultBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentResultBinding.inflate(inflater, container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val resultListViewModel: ResultListViewModel by viewModel()
        val adapter = ResultListAdapter(resultListViewModel,this)

        val recomIntent  = Intent(activity!!, MainActivity::class.java)

        val answer2wordsJson = arguments?.getString("ANSWER_LIST")
        val wordJson = arguments?.getString("WORD_LIST")
        val maptypeToken = object : TypeToken<Array<Map<Int,String>>>() {}
        val wordtypeToken = object : TypeToken<Array<Word>>() {}
        val answer2wordlist = Gson().fromJson<Array<Map<Int,String>>>(answer2wordsJson, maptypeToken.type)
        val wordList = Gson().fromJson<Array<Word>>(wordJson,wordtypeToken.type)
        val answerlist = convert(wordList,answer2wordlist)
        val resultViewModel: ResultViewModel by viewModel()
        binding?.data = resultViewModel
        resultListViewModel.draw(answerlist,wordList)
        adapter.notifyDataSetChanged()
        ResultRecyclerView.adapter = adapter
        ResultRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        add_answer_button.setOnClickListener {
            resultListViewModel.addCard()
            adapter.notifyDataSetChanged()
        }

        // 保存する
        save_button.setOnClickListener {
            val bool_list = resultListViewModel.checkedList
            //　新規追加データがあれば保存する
            answerlist.addAll(resultListViewModel.returnRegisterCard(answerlist.size))
            
            var register_index  = ArrayList<Int>()
            bool_list.forEachIndexed { index, data ->
                if(data.value == true){ register_index.add(index) }
            }
            val saveDialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("データ保存")
                setMessage(register_index.size.toString()+"個、韻を保存します")
                setPositiveButton("OK") { _, _ ->
                    var registe_answer = ArrayList<Answer>()
                    for (index in register_index){
                        var answer = answerlist.get(index)
                        registe_answer.add(answer)
                    }
                    // 保存
                    GlobalScope.launch {
                        val dao = App.db.answerDao()
                        registe_answer.forEach {
                            dao.insert(it)
                        }
                    }
                    startActivity(recomIntent)
                }
                setNegativeButton("NO",null)
            }
            val alertDialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("データ保存")
                setMessage("韻を選択してください")
                setPositiveButton("OK",{_, _ ->

                })
            }
            if (register_index.size ==0){
                alertDialog.show()
            }else{
                saveDialog.show()
            }

        }
        // 保存せずメイン画面へ戻る
        back_button.setOnClickListener {
            val dialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("ゲーム設定画面へ戻る")
                setMessage("(保存は一切行われません)")
                setPositiveButton("OK",{_, _ ->
                    startActivity(recomIntent)
                })
                setNegativeButton("NO",null)
            }
            dialog.show()
        }
    }
    fun convert(wordlist: Array<Word>, answer2wordList: Array<Map<Int,String>>):ArrayList<Answer>{
        val answerList = ArrayList<Answer>()
        answer2wordList.forEachIndexed{  index, item ->
            val word_index =item.keys.toList().get(0)
            val word_value =item.values.toList().get(0)
            val word:Word = wordlist.get(word_index)
            val answer = Answer(0,word_value,word.length,word.word,0)
            answerList.add(answer)
        }
        return answerList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }









}
