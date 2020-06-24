package com.rapdict.takuro.rapdict.Common

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.R
import kotlinx.android.synthetic.main.fragment_test_sound.*


class TestSoundFragment : androidx.fragment.app.Fragment() {

    var mediaPlayer: MediaPlayer ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(activity, R.raw.beat_97)
        test_sound_button.setOnClickListener {
            mediaPlayer?.let { it1 -> onCompletion(it1) }
        }
        //
        stop_button.setOnClickListener {
            mediaPlayer?.pause()
        }
        var counter: Int = 0
        //音声停止時の挙動。疑似的
        mediaPlayer?.setOnCompletionListener {
            counter++
            if(counter == 4){
                mediaPlayer?.pause()
                counter = 0
            }else{
                onCompletion(it)
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.reset();
        mediaPlayer?.release();
    }
    fun onCompletion(player: MediaPlayer){
        player.seekTo(0)
        player.start()
        System.out.println("あ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_test_sound, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}