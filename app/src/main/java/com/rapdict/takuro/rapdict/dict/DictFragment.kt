package com.rapdict.takuro.rapdict.dict

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import apps.test.marketableskill.biz.recyclerview.DictListAdapter
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.rapdict.takuro.rapdict.databinding.FragmentDictBinding
import kotlinx.android.synthetic.main.fragment_dict.*


class DictFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentDictBinding? = null
    private val dictViewModel: DictViewModel by viewModels()
    private lateinit var deleteDialog : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deleteDialog = AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setNegativeButton("NO",null)
            setMessage("韻を消します")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDictBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.data = dictViewModel

        val adapter = DictListAdapter(dictViewModel, this)
        DictRecyclerView.adapter = adapter
        DictRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        dictViewModel.dataGetAction.observe(viewLifecycleOwner, Observer {
            dictViewModel.loadData()
        })

        dictViewModel.radioType.observe(viewLifecycleOwner, Observer {
            dictViewModel.convertRidList()
            dictViewModel.dataGetAction.value = Unit
        })
        dictViewModel.dictDataList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            for (dictData in it) {
                dictData.isFavorite.observe(viewLifecycleOwner, Observer {
                    dictViewModel.updateFavorite(dictData)
                })
                dictData.isDelete.observe(viewLifecycleOwner, Observer {
                    // 最初はFalseで設定しているため起動しない。
                    if(it){
                        deleteDialog.setPositiveButton("OK") { _, _ ->
                            dictViewModel.deleteData(dictData)
                        }.show()
                    }
                })
            }
        })

        //range Progress bar で必要な設定
        range_progress_seek_bar.setIndicatorTextDecimalFormat("0")
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
                dictViewModel.min = minInt
                dictViewModel.max = maxInt
                dictViewModel.dataGetAction.value = Unit
            }
        })
    }

}



