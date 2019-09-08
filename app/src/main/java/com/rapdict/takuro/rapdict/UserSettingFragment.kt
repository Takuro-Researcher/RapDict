package com.rapdict.takuro.rapdict

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user_setting.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UserSettingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [UserSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class UserSettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null
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
        return inflater.inflate(R.layout.fragment_user_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wordAccess=WordAccess()
        //踏んだ韻を数える処理
        helper = SQLiteOpenHelper(view.context)
        db = helper!!.writableDatabase
        val rhyme_count= wordAccess.getCount(db!!)

        val spf = this.getActivity()!!.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        val userName = spf.getString("user_name","ゲスト")
        val useExp =spf.getInt("exp",0)

        val editor  = spf.edit()
        //データを描画
        edit_user_name.setText(userName)
        user_exp_num.text = useExp.toString()
        rhyme_num.text = rhyme_count.toString()

        //名前変更処理
        rename_button.setOnClickListener{
            val re_name = edit_user_name.text.toString()
            editor.putString("user_name",re_name)
            editor.apply()
            val fm=fragmentManager
            fm?.beginTransaction()?.
                    replace(R.id.fragmentFrameLayout,UserExpFragment())?.
                    commit()
        }
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
                UserSettingFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
