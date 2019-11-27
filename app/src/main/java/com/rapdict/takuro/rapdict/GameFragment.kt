package com.rapdict.takuro.rapdict

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils.isEmpty
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_insert_four.*
import kotlinx.android.synthetic.main.fragment_insert_one.*
import kotlinx.android.synthetic.main.fragment_insert_three.*
import kotlinx.android.synthetic.main.fragment_insert_two.*
import sample.intent.AnswerData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

class GameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val dataFormat = SimpleDateFormat("ss.SS", Locale.US)
    internal var finish_q =0
    internal var editTexts =arrayOfNulls<EditText>(4)
    private var timer:CountDownTimer?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        container?.removeAllViews()

        // 正常にfragmentを切り替えることには成功している。よって、デザインの可能性が高い。
        return inflater.inflate(R.layout.fragment_game, container,false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
    override fun onActivityCreated(savedInstanceState:Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val helper =SQLiteOpenHelper(activity!!.applicationContext)
        val db = helper.readableDatabase
        val wordAccess = WordAccess()

        val answerNum = arguments!!.getInt("RETURN")
        val timerNum = arguments!!.getInt("TIME")*1000.toLong()
        val questionNum = arguments!!.getInt("QUESTION")
        val minNum = arguments!!.getInt("MIN_WORD")
        val maxNum = arguments!!.getInt("MAX_WORD")
        val words = wordAccess.getWords(db, minNum, maxNum, questionNum)

        val transaction2 = fragmentManager?.beginTransaction()
        val resultFragment =ResultFragment()
        val bundle:Bundle = Bundle()
        val answer_list = ArrayList<AnswerData>()

        // 初期描画
        game_question_num.text = questionNum.toString()
        game_question_text.text = words[finish_q].word
        game_furigana_text.text = words[finish_q].furigana

        timer = object:CountDownTimer(timerNum,100.toLong()){
            override fun onTick(millisUntilFinished: Long) {
                game_sec_display.text = dataFormat.format(millisUntilFinished)
            }
            override fun onFinish() {

                if (finish_q >= questionNum-1){
                    cancel()
                    bundle.putSerializable("ANSWER_LIST", answer_list)
                    resultFragment.arguments = bundle
                    transaction2?.replace(R.id.fragmentGame, resultFragment)
                    transaction2?.commit()
                }else{
                    finish_q++
                    changedQuestion(finish_q, words, questionNum)
                }
            }
        }.start()
        //問題変更ボタン処理
        game_next_button.setOnClickListener {
            answer_list.addAll(saveAnswer(words[finish_q].word_id!!, words[finish_q].word!!))
            finish_q++
            if (finish_q >= questionNum){
                timer!!.cancel()
                bundle.putSerializable("ANSWER_LIST", answer_list)
                resultFragment.arguments = bundle
                transaction2?.replace(R.id.fragmentGame, resultFragment)
                transaction2?.commit()
            }else{
                changedQuestion(finish_q,words,questionNum)
                editTextClear()
                game_main.setFocusable(true)
                game_main.setFocusableInTouchMode(true)
                game_main.requestFocus()
            }
        }
        val transaction = childFragmentManager.beginTransaction()
        val tableFragment = InsertOneFragment.newInstance(answerNum)
        transaction.add(R.id.edit_table, tableFragment)
        transaction.commit()

    }

    override fun onStart() {
        super.onStart()
        // 回答時、停止処理を記述
        val answerNum = arguments!!.getInt("RETURN")

        when(answerNum){
            1 -> {
                editTexts[0] = rhyme_one_one
            }
            2 -> {
                editTexts[0] = rhyme_two_one
                editTexts[1] = rhyme_two_two
            }
            3 -> {
                editTexts[0] = rhyme_three_one
                editTexts[1] = rhyme_three_two
                editTexts[2] = rhyme_three_three
            }
            4 -> {
                editTexts[0] = rhyme_four_one
                editTexts[1] = rhyme_four_two
                editTexts[2] = rhyme_four_three
                editTexts[3] = rhyme_four_four
            }
        }
        for (i in 0..answerNum-1){
            editTexts[i]!!.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    timer?.cancel()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        finish_q = 100
        timer!!.cancel()
    }
    // 踏んだ韻をため込む処理

    // 問題を変更する処理
    fun changedQuestion(finish_q:Int, words:ArrayList<Word>, questionNum:Int){
        game_question_num.text = (questionNum - finish_q).toString()
        game_furigana_text.text = words[finish_q].furigana
        game_question_text.text = words[finish_q].word
        timer!!.start()
    }
    // answerList を一時保存

    private fun saveAnswer(word_id:Int, word:String):ArrayList<AnswerData>{
        val answerNum = arguments!!.getInt("RETURN")
        val answerArray = ArrayList<AnswerData>()
        var answerData =AnswerData()
        for (i in 0 until answerNum){
            if ( !isEmpty(editTexts[i]?.text) ){
                answerData = AnswerData()
                System.out.println(editTexts[i]?.text.toString() )
                answerData.answerSet(word_id, editTexts[i]?.text.toString(), word)
                answerArray.add(answerData)
            }
        }
        return answerArray
    }
    fun editTextClear(){
        val answerNum = arguments!!.getInt("RETURN")
        for (i in 0 until answerNum){
            editTexts[i]?.editableText?.clear()
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(answerNum:Int) =
                GameFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, answerNum)
                    }
                }
    }
}
