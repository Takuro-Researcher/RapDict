package com.rapdict.takuro.rapdict.ui.game

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentGameBinding
import com.rapdict.takuro.rapdict.model.entity.Word
import com.rapdict.takuro.rapdict.ui.main.MainActivity
import com.rapdict.takuro.rapdict.ui.result.ResultFragment
import kotlinx.android.synthetic.main.fragment_game.*
import java.util.*


class GamePlayFragment : androidx.fragment.app.Fragment() {
    lateinit private var binding: FragmentGameBinding

    internal var finish_q = 0
    lateinit private var mediaPlayer: MediaPlayer
    private val gameViewModel: GamePlayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
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
            if (isFinish) {
                mediaPlayer.pause()
                val resultFragment = ResultFragment()
                val bundle = Bundle()
                // 次画面に渡すために値を取り付ける
                bundle.let {
                    it.putString("ANSWER_LIST", Gson().toJson(gameViewModel.answerMap))
                    it.putParcelableArrayList("WORD_LIST", gameViewModel.words as ArrayList<Parcelable>)
                    resultFragment.arguments = bundle
                }
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
            gameViewModel.saveAnswer()
            gameViewModel.changeQuestion()
            // UI 更新
            rhyme_edit_one.editableText.clear()
            game_main.setFocusable(true)
            game_main.setFocusableInTouchMode(true)
            game_main.requestFocus()
            onCompletion(mediaPlayer)
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
