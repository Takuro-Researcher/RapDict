package com.rapdict.takuro.rapdict.game

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.databinding.FragmentGameBinding
import com.rapdict.takuro.rapdict.result.ResultFragment
import kotlinx.android.synthetic.main.fragment_game.*
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.ThreadLocalRandom

import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt


class GamePlayFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentGameBinding? =null
    internal var finish_q =0
    var mediaPlayer : MediaPlayer? =null
    private lateinit var mInterstitialAd: InterstitialAd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(activity) {}

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

        // bundleからJsonをPerseする
        val questionNum = arguments!!.getInt("QUESTION")
        val filepath = arguments!!.getInt("BAR")
        val words:ArrayList<Word> = arguments!!.getSerializable("WORDS") as ArrayList<Word>

        // 答えリストを作るための処理
        val answerList = ArrayList<Map<Int,String>>()
        game_question_num.text = questionNum.toString()
        game_question_text.text = words[finish_q].word
        game_furigana_text.text = words[finish_q].furigana
        // 広告を設定
        mInterstitialAd = InterstitialAd(activity).apply {
            adUnitId = "ca-app-pub-3940256099942544/1033173712"
            adListener = (object : AdListener() {
                override fun onAdLoaded() {}

                override fun onAdFailedToLoad(errorCode: Int) {}

                override fun onAdClosed() {
                    jumpedResult(answerList,words)
                }
            })
        }

        mInterstitialAd.loadAd(AdRequest.Builder().build())


        mediaPlayer = MediaPlayer.create(activity, filepath)
        onCompletion(mediaPlayer!!)

        onStart()

        mediaPlayer?.setOnCompletionListener {
            if (finish_q >= questionNum-1){
                it.pause()
                mInterstitialAd.show()
            }else{
                finish_q++
                changedQuestion(finish_q, words, questionNum)
                onCompletion(it)
            }
        }

        //問題変更ボタン処理
        game_next_button.setOnClickListener {
            answerList.addAll(saveAnswer(finish_q))
            finish_q++
            if (finish_q >= questionNum){
                mediaPlayer!!.pause()
                // 広告を見せる
                if(mInterstitialAd.isLoaded){
                    mInterstitialAd.show()
                }else{
                    jumpedResult(answerList,words)
                }
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
                    mediaPlayer?.pause()
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
        mediaPlayer?.stop()
        mediaPlayer?.reset();
        mediaPlayer?.release();
    }
    // 問題を変更する処理
    private fun changedQuestion(finish_q:Int, words:ArrayList<Word>, questionNum:Int){
        game_question_num.text = (questionNum - finish_q).toString()
        game_furigana_text.text = words[finish_q].furigana
        game_question_text.text = words[finish_q].word
    }

    private fun jumpedResult(answerList:ArrayList<Map<Int,String>>,wordsList:ArrayList<Word>){
        val bundle = Bundle()
        bundle.putString("ANSWER_LIST", Gson().toJson(answerList))
        bundle.putString("WORD_LIST",Gson().toJson(wordsList))
        val resultFragment = ResultFragment()
        resultFragment.arguments = bundle
        //画面遷移
        val transaction2 = fragmentManager?.beginTransaction()
        transaction2?.replace(R.id.fragmentGame, resultFragment)
        transaction2?.commit()
        mediaPlayer?.pause()
    }
    // answerList をアクティビティ内に保存する
    private fun saveAnswer(word_id:Int):ArrayList<Map<Int,String>>{
        val answerNum = arguments!!.getInt("RETURN")
        val answerTexts = mutableListOf<String>()
        if (answerNum >= 1){ answerTexts.add(rhyme_edit_one.text.toString()) }
        if (answerNum >= 2){ answerTexts.add(rhyme_edit_two.text.toString()) }
        if (answerNum >= 3){ answerTexts.add(rhyme_edit_three.text.toString()) }
        if (answerNum >= 4){ answerTexts.add(rhyme_edit_four.text.toString()) }
        val answer2word = ArrayList<Map<Int,String>>()
        answerTexts.forEach {
            if(it.isNotEmpty() ){
                answer2word.add(mapOf(word_id to it))
            }
        }
        return answer2word
    }

    fun editTextClear(){
        var editTextNum = arguments!!.getInt("RETURN")
        if (editTextNum >= 1){ rhyme_edit_one.editableText.clear() }
        if (editTextNum >= 2){ rhyme_edit_two.editableText.clear() }
        if (editTextNum >= 3){ rhyme_edit_three.editableText.clear() }
        if (editTextNum >= 4){ rhyme_edit_four.editableText.clear() }
    }
}
