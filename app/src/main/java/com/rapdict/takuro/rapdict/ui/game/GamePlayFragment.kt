package com.rapdict.takuro.rapdict.ui.game

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentGameBinding
import com.rapdict.takuro.rapdict.model.entity.Word
import com.rapdict.takuro.rapdict.ui.result.ResultFragment
import kotlinx.android.synthetic.main.fragment_game.*
import java.util.*


class GamePlayFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentGameBinding

    private lateinit var mediaPlayer: MediaPlayer

    private val gameViewModel: GamePlayViewModel by viewModels {
        val questionNum = arguments?.getInt("QUESTION") ?: 0
        val questionWords: List<Word> = arguments?.getSerializable("WORDS") as List<Word>
        GamePlayViewModelFactory(questionNum = questionNum, questionWords = questionWords)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filepath = arguments?.getInt("BAR")
        mediaPlayer = MediaPlayer.create(activity, filepath!!)
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

        gameViewModel.isFinish.observe(viewLifecycleOwner, Observer<Boolean> { isFinish ->
            if (isFinish) {
                mediaPlayer.pause()
                val resultFragment = ResultFragment()
                val bundle = Bundle().apply {
                    putString("ANSWER_LIST", Gson().toJson(gameViewModel.answerMap))
                    putParcelableArrayList("WORD_LIST", gameViewModel.questionWords as ArrayList<Parcelable>)
                }
                resultFragment.arguments = bundle

                //画面遷移
                fragmentManager?.beginTransaction().let {
                    it?.replace(R.id.fragmentGame, resultFragment, "handlingBackPressed")
                    it?.commit()
                }
            }
        })

        // 音楽終了時の設定
        mediaPlayer.setOnCompletionListener {
            gameViewModel.changeQuestion()
            onCompletion(mediaPlayer)
        }

        //問題変更ボタン処理
        game_next_button.setOnClickListener {
            gameViewModel.nextRhyme()
            game_main.run {
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
            }
            onCompletion(mediaPlayer)
            // テキストキーボードを閉じる
            val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        // 初回の問題プレイヤー再生
        onCompletion(mediaPlayer)
        onStart()
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
}

class GamePlayViewModelFactory(private val questionWords: List<Word>?, private val questionNum: Int) :
        ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return questionWords?.let { GamePlayViewModel(it, questionNum) } as T
    }
}
