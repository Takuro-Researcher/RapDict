package com.rapdict.takuro.rapdict

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.fragment_game_setting.*

class GameSettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val question = make_list_num(10,30,10)
        val time = make_list_num(3,15,3)
        val min = make_list_num(3,8,1)
        val max =make_list_num(4,14,1)
        val ret = make_list_num(1,4,1)

        val questionAdapter= ArrayAdapter(context!!,android.R.layout.simple_spinner_item,question);
        val timeAdapter = ArrayAdapter(context!!,android.R.layout.simple_spinner_item,time);
        val minAdapter = ArrayAdapter(context!!,android.R.layout.simple_spinner_item,min);
        val returnAdapter = ArrayAdapter(context!!,android.R.layout.simple_spinner_item,ret);
        val maxAdapter = ArrayAdapter(context!!,android.R.layout.simple_spinner_item,max);

        val question_spinner = question_spinner as Spinner
        val time_spinner = time_spinner as Spinner
        val min_spinner = min_spinner as Spinner
        val max_spiiner = max_spinner as Spinner
        val return_spinner = return_spinner as Spinner

        question_spinner.adapter = questionAdapter
        time_spinner.adapter = timeAdapter
        min_spinner.adapter = minAdapter
        max_spiiner.adapter =maxAdapter
        return_spinner.adapter =returnAdapter

        //最小文字>最大文字とならないように、スピナーの値を順次変更する
        min_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var min_value: Int = 0
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val spinner = adapterView as Spinner
                min_value = spinner.selectedItem as Int
                maxAdapter.clear();
                for (f in min_value + 1..10) {
                    maxAdapter.add(f)
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        start_button.setOnClickListener {
            val intent: Intent = Intent(view.context,GameActivity::class.java)
            intent.putExtra("QUESTION",question_spinner.selectedItem as Int)
            intent.putExtra("TIME",time_spinner as Int)
            intent.putExtra("MIN_WORD",min_spinner.selectedItem as Int)
            intent.putExtra("MAX_WORD",max_spiiner.selectedItem as Int)
            intent.putExtra("RETURN",return_spinner.selectedItem as Int)
            startActivity(intent)
        }



    }
    fun make_list_num(start:Int,end:Int,inc:Int):ArrayList<Int>{
        val List = ArrayList<Int>()
        for (i in start..end step inc){
            List.add(i)
        }
        return List
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                GameSettingFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
