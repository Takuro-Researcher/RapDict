
package com.rapdict.takuro.rapdict.userSetting

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.databinding.FragmentUserSettingBinding
import com.rapdict.takuro.rapdict.exp.UserExpFragment
import kotlinx.android.synthetic.main.fragment_user_setting.*
import org.koin.android.viewmodel.ext.android.viewModel


class UserSettingFragment : androidx.fragment.app.Fragment() {
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
        val spfCommon =SpfCommon(PreferenceManager.getDefaultSharedPreferences(this.activity))

        //名前変更処理
        rename_button.setOnClickListener{
            val reName = edit_user_name.text.toString()
            val reTarget = target_spinner.selectedItem as Int
            spfCommon.userSave(reName,reTarget)
            // 画面遷移
            val fm=fragmentManager
            fm?.beginTransaction()?.
                    replace(R.id.fragmentFrameLayout, UserExpFragment())?.
                    commit()
        }
    }
}
