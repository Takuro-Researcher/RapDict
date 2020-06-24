package com.rapdict.takuro.rapdict.game

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.databinding.FragmentGameBinding
import com.rapdict.takuro.rapdict.model.Answer
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
    internal var finish_q =0
    var mediaPlayer : MediaPlayer? =null


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

        val resultFragment = ResultFragment()

        val answerList = ArrayList<Answer>()
        game_question_num.text = questionNum.toString()
        game_question_text.text = words[finish_q].word
        game_furigana_text.text = words[finish_q].furigana

        mediaPlayer = MediaPlayer.create(activity, R.raw.beat_97)
        onCompletion(mediaPlayer!!)
        
        mediaPlayer?.setOnCompletionListener {
            if (finish_q >= questionNum-1){
                it.pause()
                val bundle = Bundle()
                bundle.putString("ANSWER_LIST", Gson().toJson(answerList))
                resultFragment.arguments = bundle
                //画面遷移
                val transaction2 = fragmentManager?.beginTransaction()
                transaction2?.replace(R.id.fragmentGame, resultFragment)
                transaction2?.commit()
            }else{
                finish_q++
                changedQuestion(finish_q, words, questionNum)
                onCompletion(it)
            }
        }



        //問題変更ボタン処理
        game_next_button.setOnClickListener {
            answerList.addAll(saveAnswer(words[finish_q].word!!,words[finish_q].furigana.length ))
            finish_q++
            if (finish_q >= questionNum){
                val bundle = Bundle()
                bundle.putString("ANSWER_LIST", Gson().toJson(answerList))
                resultFragment.arguments = bundle
                //画面遷移
                val transaction2 = fragmentManager?.beginTransaction()
                transaction2?.replace(R.id.fragmentGame, resultFragment)
                transaction2?.commit()
            }else{
                changedQuestion(finish_q,words,questionNum)
                editTextClear()
                game_main.setFocusable(true)
                game_main.setFocusableInTouchMode(true)
                game_main.requestFocus()
                onCompletion(mediaPlayer!!)
            }
        }

    }

    //media playerを最初から再生させる。
    fun onCompletion(player: MediaPlayer){
        player.seekTo(0)
        player.start()
    }

    override fun onStart() {
        super.onStart()
        // 回答時にビートを停止する
        var editTextOnFocus: View.OnFocusChangeListener = object :View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {

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
    }


    // 問題を変更する処理
    fun changedQuestion(finish_q:Int, words:ArrayList<Word>, questionNum:Int){
        game_question_num.text = (questionNum - finish_q).toString()
        game_furigana_text.text = words[finish_q].furigana
        game_question_text.text = words[finish_q].word
        // WIPビートを再生する
    }
    // answerList をアクティビティ内に保存する
    private fun saveAnswer(word:String,word_length:Int):ArrayList<Answer>{
        val answerNum = arguments!!.getInt("RETURN")
        val answerTexts = mutableListOf<String>()
        if (answerNum >= 1){ answerTexts.add(rhyme_edit_one.text.toString()) }
        if (answerNum >= 2){ answerTexts.add(rhyme_edit_two.text.toString()) }
        if (answerNum >= 3){ answerTexts.add(rhyme_edit_three.text.toString()) }
        if (answerNum >= 4){ answerTexts.add(rhyme_edit_four.text.toString()) }
        val answerArray = ArrayList<Answer>()
        for( answer in answerTexts){
            if (answer.isNullOrBlank()){ continue }
            val answerData = Answer(0,answer,word_length,word,0)
            answerArray.add(answerData)
        }
        // デバッグ用にシーズ
        // answerArray.add(AnswerData(1,"テストだよ"+finish_q.toString(),"縁遠かろう",0))
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
