package com.rapdict.takuro.rapdict.myDict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.R

import kotlinx.android.synthetic.main.fragment_mydict.*


class MyDictFragment : androidx.fragment.app.Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mydict_pager.adapter = MyDictTabAdapter(childFragmentManager)
        mydict_tab_layout.setupWithViewPager(mydict_pager)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_mydict, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}