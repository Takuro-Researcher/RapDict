package com.rapdict.takuro.rapdict.result

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import apps.test.marketableskill.biz.recyclerview.ListAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentGameSettingBinding
import com.rapdict.takuro.rapdict.databinding.FragmentResultBinding
import com.rapdict.takuro.rapdict.dict.ListViewModel
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.model.Answer
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.fragment_result.*
import org.koin.android.viewmodel.ext.android.viewModel
import sample.intent.AnswerData

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


        val helper = SQLiteOpenHelper(activity!!.applicationContext)
        val db = helper.readableDatabase
        val recomIntent  = Intent(activity!!, MainActivity::class.java)

        val answerListJson = arguments?.getString("ANSWER_LIST")
        val typeToken = object : TypeToken<Array<Answer>>() {}
        val answerlist = Gson().fromJson<Array<Answer>>(answerListJson, typeToken.type)

        val resultViewModel: ResultViewModel by viewModel()
        binding?.data = resultViewModel
        if(answerlist.size == 0){
            resultViewModel.draw(getString(R.string.result_no_header),getString(R.string.result_no_description))
        }

        resultListViewModel.draw(answerlist)
        adapter.notifyDataSetChanged()
        ResultRecyclerView.adapter = adapter
        ResultRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        // 保存する
        save_button.setOnClickListener {
            val bool_list = resultListViewModel.checkedList
            var register_index  = ArrayList<Int>()
            bool_list.forEachIndexed { index, data ->
                if(data.value == true){ register_index.add(index) }
            }

            val saveDialog = AlertDialog.Builder(activity!!).apply{
                setCancelable(false)
                setTitle("データ保存")
                setMessage(register_index.size.toString()+"個、韻を保存します")
                setPositiveButton("OK",{_, _ ->
                    var registe_answer = ArrayList<Answer>()
                    for (index in register_index){
                        var answer = answerlist.get(index)
                        registe_answer.add(answer)
                    }

                    startActivity(recomIntent)
                })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }









}
