package com.rapdict.takuro.rapdict.game

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.Common.InsertOneFragment
import com.rapdict.takuro.rapdict.Common.HttpApiRequest
import com.rapdict.takuro.rapdict.databinding.FragmentGameBinding
import com.rapdict.takuro.rapdict.result.ResultFragment
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_insert_four.*
import kotlinx.android.synthetic.main.fragment_insert_one.*
import kotlinx.android.synthetic.main.fragment_insert_three.*
import kotlinx.android.synthetic.main.fragment_insert_two.*
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import sample.intent.AnswerData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

class GameFragment : androidx.fragment.app.Fragment() {
    private var param1: String? = null
    private var binding: FragmentGameBinding? =null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater,container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }
    override fun onActivityCreated(savedInstanceState:Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gameViewModel: GameViewModel by viewModel()
        binding?.data = gameViewModel
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val httpApiRequest = HttpApiRequest()
        val words =ArrayList<Word>()
        httpApiRequest.setOnCallBack(object : HttpApiRequest.CallBackTask(){
            override fun CallBack(result: String) {
                super.CallBack(result)
                val parentJsonObj = JSONObject(result)
                val rhymes = parentJsonObj.get("rhymes") as JSONArray
                rhymes.getJSONObject(0)
                for(i in 0 until rhymes.length()){
                    val jsonRhyme = rhymes.getJSONObject(i)
                    val rhyme =Word()
                    rhyme.id = jsonRhyme.getInt("id")
                    rhyme.furigana = jsonRhyme.getString("furigana")
                    rhyme.word = jsonRhyme.getString("name")
                    rhyme.length = jsonRhyme.getInt("length")
                    words.add(rhyme)
                }
            }
        })
        // TODO 実際の描画やViewModelの検討など

        val answerNum = arguments!!.getInt("RETURN")
        val timerNum = arguments!!.getInt("TIME")*1000.toLong()
        val questionNum = arguments!!.getInt("QUESTION")
        val minNum = arguments!!.getInt("MIN_WORD")
        val maxNum = arguments!!.getInt("MAX_WORD")

        val transaction2 = fragmentManager?.beginTransaction()
        val resultFragment = ResultFragment()
        val bundle = Bundle()
        val answerList = ArrayList<AnswerData>()

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
                    bundle.putString("ANSWER_LIST", Gson().toJson(answerList))
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
            answerList.addAll(saveAnswer(words[finish_q].id!!, words[finish_q].word!!))
            finish_q++
            if (finish_q >= questionNum){
                timer!!.cancel()
                bundle.putString("ANSWER_LIST", Gson().toJson(answerList))
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
            editTexts[i]!!.setOnFocusChangeListener { _, hasFocus ->
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
        var answerData:AnswerData
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
}
