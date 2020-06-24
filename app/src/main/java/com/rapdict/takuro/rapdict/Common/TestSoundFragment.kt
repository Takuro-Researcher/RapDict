package com.rapdict.takuro.rapdict.Common

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentUserExpBinding
import com.rapdict.takuro.rapdict.exp.UserExpViewModel
import kotlinx.android.synthetic.main.fragment_test_sound.*
import kotlinx.android.synthetic.main.fragment_user_exp.*
import org.koin.android.viewmodel.ext.android.viewModel

class TestSoundFragment : androidx.fragment.app.Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mediaPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.beat_97_dronly)
        test_sound_button.setOnClickListener {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()

        }

    }
    fun onCompletion(player: MediaPlayer){
        player.seekTo(0)
        player.start()
        System.out.println("ああ")
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