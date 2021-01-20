package com.rapdict.takuro.rapdict.dict

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.activity_dict.*


class DictFragment : androidx.fragment.app.Fragment() {

    @SuppressLint("NewApi", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_dict, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //検索用のレンジプログレスバーの設定
        range_progress_seek_bar.setIndicatorTextDecimalFormat("0")
        val recomIntent = Intent(requireActivity(), MainActivity::class.java)

        val recomdialog = AlertDialog.Builder(requireActivity())
        recomdialog.setCancelable(false)
        recomdialog.setMessage("韻を踏みに行きましょう")
        recomdialog.setPositiveButton("戻る") { _, _ ->
            startActivity(recomIntent)
        }
        //韻呼び出し
        range_progress_seek_bar.setRange(0F, 20F, 1.0f)

        range_progress_seek_bar.setOnRangeChangedListener(object : OnRangeChangedListener {
            var minInt = 0
            var maxInt = 1

            // シークを動かした瞬間に発行するイベント。　データ
            override fun onRangeChanged(rangeSeekBar: RangeSeekBar, min: Float, max: Float, isFromUser: Boolean) {
                minInt = Math.round(min.toDouble()).toInt()
                maxInt = Math.round(max.toDouble()).toInt()
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}

            // Rangeのトラックを外した瞬間に発行するイベント
            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                // TODO この瞬間にAPIを叩くことにする
                // TODO ViewModelのメソッドでDBにアクセスし、LiveDataを更新するイメージで間違いはない
            }
        })
    }

}



