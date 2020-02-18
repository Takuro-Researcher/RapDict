package com.rapdict.takuro.rapdict.exp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentUserExpBinding
import kotlinx.android.synthetic.main.fragment_user_exp.*
import org.koin.android.viewmodel.ext.android.viewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserExpFragment : androidx.fragment.app.Fragment() {
    // TODO: Rename and change types of parameters

    private var listener: OnFragmentInteractionListener? = null
    private var binding: FragmentUserExpBinding? = null
    private var userExpViewModel: UserExpViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val spf = this.activity!!.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
//        val userName = spf.getString("user_name","ゲスト")
//        user_name.text = userName
//        shougou.text
        progressbar.max =100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserExpBinding.inflate(inflater, container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userExpviewModel:UserExpViewModel by viewModel()
        binding?.userData   = userExpviewModel
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                UserExpFragment().apply {

                }
        fun createInstance(): UserExpFragment {
            val carDetailFragment = UserExpFragment()
            val args = Bundle()
            carDetailFragment.arguments = args
            return carDetailFragment
        }

    }
}
