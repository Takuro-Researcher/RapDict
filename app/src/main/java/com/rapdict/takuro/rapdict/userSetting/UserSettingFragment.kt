
package com.rapdict.takuro.rapdict.userSetting

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentUserSettingBinding
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper
import com.rapdict.takuro.rapdict.helper.WordAccess
import com.rapdict.takuro.rapdict.exp.UserExpFragment
import com.rapdict.takuro.rapdict.exp.UserExpViewModel
import kotlinx.android.synthetic.main.fragment_user_setting.*
import org.koin.android.viewmodel.ext.android.viewModel


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
class UserSettingFragment : androidx.fragment.app.Fragment() {
    // TODO: Rename and change types of parameters
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null
    private var binding: FragmentUserSettingBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserSettingBinding.inflate(inflater, container,false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userSettingViewModel: UserSettingViewModel by viewModel()
        binding?.data= userSettingViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wordAccess= WordAccess()

        //踏んだ韻を数える処理
        helper = SQLiteOpenHelper(view.context)
        db = helper!!.writableDatabase
        val rhymeCount= wordAccess.getCount(db!!)

        val spf = this.activity!!.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        val spfCommon =SpfCommon(PreferenceManager.getDefaultSharedPreferences(this.activity))
        val editor  = spf.edit()
        //データを描画

        rhyme_num.text = rhymeCount.toString()

        //名前変更処理
        rename_button.setOnClickListener{
            val reName = edit_user_name.text.toString()
            spfCommon.userSave(reName)
            val fm=fragmentManager
            fm?.beginTransaction()?.
                    replace(R.id.fragmentFrameLayout, UserExpFragment())?.
                    commit()
        }
    }
}
