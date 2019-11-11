package com.rapdict.takuro.rapdict

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_game.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GameFragment : Fragment() {
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        container?.removeAllViews()
        // 正常にfragmentを切り替えることには成功している。よって、デザインの可能性が高い。
        return inflater.inflate(R.layout.fragment_game, container,false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
    override fun onActivityCreated(savedInstanceState:Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var answerNum = arguments!!.getInt("ANSWER")
        val transaction = childFragmentManager.beginTransaction()
        val tableFragment = InsertOneFragment.newInstance(answerNum)
        transaction.add(R.id.edit_table, tableFragment)
        transaction.commit()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(answerNum:Int) =
                GameFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, answerNum)
                    }
                }
    }
}
