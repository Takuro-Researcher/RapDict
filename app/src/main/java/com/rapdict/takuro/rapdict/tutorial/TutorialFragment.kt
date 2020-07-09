package com.rapdict.takuro.rapdict.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.database.Mydict
import com.rapdict.takuro.rapdict.dict.DictActivity
import com.rapdict.takuro.rapdict.myDict.GameSettingFragment
import com.rapdict.takuro.rapdict.myDict.MyDictFragment
import kotlinx.android.synthetic.main.fragment_tutorial.*
import kotlinx.coroutines.runBlocking

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
            fragmentManager!!.beginTransaction()
                    .replace(R.id.fragmentFrameLayout, GameSettingFragment())
                    .commit()
        }
        dict_description.setOnClickListener {
            val intent = Intent(activity, DictActivity::class.java)
            startActivity(intent)
        }

        mydict_description.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.fragmentFrameLayout, MyDictFragment())
                    .commit()
        }
    }
}