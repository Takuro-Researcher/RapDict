package com.rapdict.takuro.rapdict.game

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.databinding.FragmentGameBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.result.ResultFragment
import kotlinx.android.synthetic.main.fragment_game.*


class GamePlayFragment : androidx.fragment.app.Fragment() {
    lateinit private var binding: FragmentGameBinding

    internal var finish_q = 0
    lateinit private var mediaPlayer: MediaPlayer
    private val gameViewModel: GamePlayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null){
            val filepath = bundle.getInt("BAR")
            mediaPlayer = MediaPlayer.create(activity, filepath)
            gameViewModel.arg_to_member(bundle.getInt("QUESTION"), bundle.getSerializable("WORDS") as List<Word>)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.data = gameViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bundleからデータを取得する

        val backIntent = Intent(activity, MainActivity::class.java)
        val recomdialog = AlertDialog.Builder(activity)
        recomdialog.setCancelable(false)
        recomdialog.setMessage("原因不明のエラーです..。再度お試しください")
        recomdialog.setPositiveButton("戻る") { _, _ ->
            startActivity(backIntent)
        }

        gameViewModel.isFinish.observe(viewLifecycleOwner, Observer<Boolean> { isFinish ->
            if(isFinish){
                mediaPlayer.pause()
                jumpedTest()


            }
        })


        val answerList = ArrayList<Map<Int, String>>()
        // 初回だけ確認する。
        try {

        } catch (e: Exception) {
            recomdialog.show()
        }
        // 答えリストを作るための処理
        onCompletion(mediaPlayer)
        onStart()

        // 音楽終了時の設定
        mediaPlayer.setOnCompletionListener {
            gameViewModel.changeQuestion()
            onCompletion(mediaPlayer)
        }

        //問題変更ボタン処理
        game_next_button.setOnClickListener {
            answerList.addAll(saveAnswer(finish_q))
            gameViewModel.changeQuestion()
            // UI 更新
            rhyme_edit_one.editableText.clear()
            game_main.setFocusable(true)
            game_main.setFocusableInTouchMode(true)
            game_main.requestFocus()
            onCompletion(mediaPlayer)
        }
    }

    //media playerを最初から再生させる。
    fun onCompletion(player: MediaPlayer) {
        player.seekTo(0)
        player.start()
    }

    override fun onStart() {
        super.onStart()
        // 回答時にビートを停止する
        val editTextOnFocus: View.OnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    mediaPlayer.pause()
                }
            }
        }
        rhyme_edit_one.onFocusChangeListener = editTextOnFocus
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
    }

    private fun jumpedTest(){
        val resultFragment = ResultFragment()
        val bundle = arguments
        resultFragment.arguments = bundle
        //画面遷移
        val transaction2 = fragmentManager?.beginTransaction()
        transaction2?.replace(R.id.fragmentGame, resultFragment, "handlingBackPressed")
        transaction2?.commit()
        mediaPlayer.pause()
    }

    private fun jumpedResult(answerList: ArrayList<Map<Int, String>>, wordsList: ArrayList<Word>) {
        val bundle = Bundle()
        bundle.putString("ANSWER_LIST", Gson().toJson(answerList))
        bundle.putString("WORD_LIST", Gson().toJson(wordsList))
        val resultFragment = ResultFragment()
        resultFragment.arguments = bundle
        //画面遷移
        val transaction2 = fragmentManager?.beginTransaction()
        transaction2?.replace(R.id.fragmentGame, resultFragment, "handlingBackPressed")
        transaction2?.commit()
        mediaPlayer.pause()
    }

    // answerList をアクティビティ内に保存する
    private fun saveAnswer(word_id: Int): ArrayList<Map<Int, String>> {


        val answerTexts = mutableListOf<String>()

        val answer2word = ArrayList<Map<Int, String>>()
        answerTexts.forEach {
            if (it.isNotEmpty()) {
                answer2word.add(mapOf(word_id to it))
            }
        }
        return answer2word
    }

}
