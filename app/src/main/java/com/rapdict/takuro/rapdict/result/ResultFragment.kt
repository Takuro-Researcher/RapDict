package com.rapdict.takuro.rapdict.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import kotlinx.android.synthetic.main.fragment_result.*
import sample.intent.AnswerData

class ResultFragment : androidx.fragment.app.Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val helper = SQLiteOpenHelper(activity!!.applicationContext)
        val db = helper.readableDatabase
        val answerList = arguments?.getString("ANSWER_LIST")
        val typeToken = object : TypeToken<Array<AnswerData>>() {}
        val list = Gson().fromJson<Array<AnswerData>>(answerList, typeToken.type)


        val answerView = AnswerView()
        // 保存データ
        list?.forEach {
              answerView.answer_saveData(db, it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_rhyme_button.setOnClickListener {
            System.out.println("Helloooooo!!")
        }
    }









}
