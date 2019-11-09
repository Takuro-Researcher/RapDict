package com.rapdict.takuro.rapdict

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InsertOneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val bundle =arguments
        val num :Int = bundle?.get(ARG_PARAM1) as Int
        val selectDesign = selectFragment(num)

        return inflater.inflate(selectDesign, container, false)
    }
    fun selectFragment(num:Int):Int{
        when(num){
            1->{
                return R.layout.fragment_insert_one
            }
            2->{
                return R.layout.fragment_insert_two
            }
            3->{
                return R.layout.fragment_insert_three
            }
            4->{
                return R.layout.fragment_insert_four
            }
        }

        return R.layout.fragment_insert_one
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
        fun newInstance(editNum:Int) =
                InsertOneFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, editNum)
                    }
                }
    }
}
