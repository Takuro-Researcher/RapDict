package com.rapdict.takuro.rapdict.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.myDict.GameSettingFragment
import com.rapdict.takuro.rapdict.ui.dict.DictFragment
import com.rapdict.takuro.rapdict.ui.myDict.MyDictFragment
import kotlinx.android.synthetic.main.fragment_tutorial.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TutorialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TutorialFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        game_description.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentFrameLayout, GameSettingFragment())
                    .commit()
        }
        dict_description.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentFrameLayout, DictFragment())
                    .commit()
        }

        mydict_description.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentFrameLayout, MyDictFragment())
                    .commit()
        }
    }
}