package com.rapdict.takuro.rapdict.exp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.rapdict.takuro.rapdict.databinding.FragmentUserExpBinding
import kotlinx.android.synthetic.main.fragment_user_exp.*


class UserExpFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentUserExpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressbar.max = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserExpBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userExpviewModel: UserExpViewModel by viewModels()
        binding?.userData = userExpviewModel
    }
}
