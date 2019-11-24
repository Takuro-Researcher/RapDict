package com.rapdict.takuro.rapdict

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_insert_four.*
import kotlinx.android.synthetic.main.fragment_insert_one.*
import kotlinx.android.synthetic.main.fragment_insert_three.*
import kotlinx.android.synthetic.main.fragment_insert_two.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val dataFormat = SimpleDateFormat("ss.SS", Locale.US)
    internal var finish_q =0
    private var timer:CountDownTimer?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        // 初期描画
        game_question_num.text = questionNum.toString()
        game_question_text.text = words[finish_q].word
        game_furigana_text.text = words[finish_q].furigana

        timer = object:CountDownTimer(timerNum,100.toLong()){
            override fun onTick(millisUntilFinished: Long) {
                game_sec_display.text = dataFormat.format(millisUntilFinished)
            }
            override fun onFinish() {
                if (finish_q >= questionNum){
                    cancel()
                }else{
                    finish_q++
                    changedQuestion(finish_q, words, questionNum)
                }
            }
        }.start()
        //問題変更ボタン処理
        game_next_button.setOnClickListener {
            finish_q++
            changedQuestion(finish_q,words,questionNum)
            if (finish_q >= questionNum){
                timer!!.cancel()
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
        val editTexts = arrayOfNulls<EditText>(4)

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
            editTexts[i]!!.setOnClickListener {
                timer!!.cancel()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        finish_q = 100
        timer!!.cancel()
    }

    fun changedQuestion(finish_q:Int, words:ArrayList<Word>, questionNum:Int){
        game_question_num.text = (questionNum - finish_q).toString()
        game_furigana_text.text = words[finish_q].furigana
        game_question_text.text = words[finish_q].word
        timer!!.start()
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
