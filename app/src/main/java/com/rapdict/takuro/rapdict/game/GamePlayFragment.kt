package com.rapdict.takuro.rapdict.game

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.databinding.FragmentGameBinding
import com.rapdict.takuro.rapdict.result.ResultFragment
import kotlinx.android.synthetic.main.fragment_game.*
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

class GamePlayFragment : androidx.fragment.app.Fragment() {
    private var param1: String? = null
    private var binding: FragmentGameBinding? =null
    private val dataFormat = SimpleDateFormat("ss.SS", Locale.US)
    internal var finish_q =0
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
        binding!!.fragment = this
        return binding!!.root
    }
    override fun onActivityCreated(savedInstanceState:Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gameViewModel: GamePlayViewModel by viewModel()
        gameViewModel.draw(arguments!!.getInt("RETURN"))
        binding?.data = gameViewModel
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timerNum = arguments!!.getInt("TIME")*1000.toLong()
        val questionNum = arguments!!.getInt("QUESTION")
        val words =ArrayList<Word>()
        val rhymes = JSONObject(arguments!!.getString("RHYMES")).get("rhymes") as JSONArray
        for(i in 0 until rhymes.length()){
            val jsonWord = rhymes.getJSONObject(i)
            val questionWord = Word(
                    jsonWord.getInt("id"),
                    jsonWord.getString("furigana"),
                    jsonWord.getString("word"),
                    jsonWord.getInt("length")
            )
            words.add(questionWord)
        }
        val transaction2 = fragmentManager?.beginTransaction()
        val resultFragment = ResultFragment()
        val bundle = Bundle()
        val answerList = ArrayList<AnswerData>()


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

    }

    override fun onStart() {
        super.onStart()
        // 回答時にストップウォッチの停止処理を記述
        var editTextOnFocus: View.OnFocusChangeListener = object :View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    timer!!.cancel()
                }
            }
        }
        rhyme_edit_one.onFocusChangeListener = editTextOnFocus
        rhyme_edit_two.onFocusChangeListener = editTextOnFocus
        rhyme_edit_three.onFocusChangeListener = editTextOnFocus
        rhyme_edit_four.onFocusChangeListener = editTextOnFocus
    }

    override fun onStop() {
        super.onStop()
        finish_q = 100
        timer!!.cancel()
    }


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
        val answerTexts = mutableListOf<String>()
        if (answerNum >= 1){ answerTexts.add(rhyme_edit_one.text.toString()) }
        if (answerNum >= 2){ answerTexts.add(rhyme_edit_two.text.toString()) }
        if (answerNum >= 3){ answerTexts.add(rhyme_edit_three.text.toString()) }
        if (answerNum >= 4){ answerTexts.add(rhyme_edit_four.text.toString()) }
        val answerArray = ArrayList<AnswerData>()
        for( answer in answerTexts){
            if (answer.isNullOrBlank()){ continue }
            val answerData = AnswerData(word_id, answer,word,0)
            answerArray.add(answerData)
        }
        // デバッグ用にシーズ
        answerArray.add(AnswerData(1,"テストだよ","縁遠かろう",0))
        return answerArray
    }


    fun editTextClear(){
        var editTextNum = arguments!!.getInt("RETURN")
        if (editTextNum >= 1){ rhyme_edit_one.editableText.clear() }
        if (editTextNum >= 2){ rhyme_edit_two.editableText.clear() }
        if (editTextNum >= 3){ rhyme_edit_three.editableText.clear() }
        if (editTextNum >= 4){ rhyme_edit_four.editableText.clear() }
    }
}
