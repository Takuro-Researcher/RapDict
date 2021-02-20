package com.rapdict.takuro.rapdict.ui.myDict


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.rapdict.takuro.rapdict.App
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.ui.myDict.myDictChoice.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_mydict1.*
import kotlinx.coroutines.runBlocking


class MyDictFragment : androidx.fragment.app.Fragment() {
    private var viewModel: MyDictChoiceViewModel? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_mydict1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabs_count:Int = MyDictTabAdapter(childFragmentManager).count
        var mydict_count:Int = 0
        runBlocking {
            val dao = App.db.mydictDao()
            mydict_count = dao.count()
        }
        // 自分辞書が一件でも保存されていたら動く。
        if (mydict_count==0){
            adapterUnabled(tabs_count)
            mydict_pager.setPagingEnabled(false)
        }
        mydict_pager.adapter = MyDictTabAdapter(childFragmentManager)
        mydict_pager.offscreenPageLimit = 2
        mydict_tab_layout.setupWithViewPager(mydict_pager)
    }

    fun adapterUnabled(tabs_count:Int){
        for(i in 1..tabs_count-1) {
            (mydict_tab_layout.getChildAt(0) as LinearLayout).getChildAt(i).isEnabled = false
            (mydict_tab_layout.getChildAt(0) as LinearLayout).getChildAt(i).setBackgroundColor(resources.getColor(R.color.darkGray))
        }
    }
    fun adapterAble(tabs_count:Int){
        for(i in 1..tabs_count-1) {
            (mydict_tab_layout.getChildAt(0) as LinearLayout).getChildAt(i).isEnabled = true
            (mydict_tab_layout.getChildAt(0) as LinearLayout).getChildAt(i).setBackgroundColor(resources.getColor(R.color.whiteSmoke))
        }
        mydict_pager.setPagingEnabled(true)
    }
}