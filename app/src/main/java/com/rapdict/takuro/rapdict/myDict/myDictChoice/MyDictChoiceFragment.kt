package com.rapdict.takuro.rapdict.myDict.myDictChoice

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rapdict.takuro.rapdict.Common.App.Companion.db
import com.rapdict.takuro.rapdict.Common.SpfCommon
import com.rapdict.takuro.rapdict.databinding.FragmentMydictChoiceBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.fragment_mydict_choice.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyDictChoiceFragment : androidx.fragment.app.Fragment() {
    private var binding :FragmentMydictChoiceBinding ?= null
    private val viewModel: MyDictChoiceViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMydictChoiceBinding.inflate(inflater, container,false)
        binding?.lifecycleOwner = this
        return binding?.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.data = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dbUid.observe(viewLifecycleOwner, Observer<Int> { dbUid ->
            viewModel.countChange()
        })

        mydict_delete_button.setOnClickListener {
            val backIntent = Intent(activity,MainActivity::class.java)
            val mydict_name = mydict_choice_spinner.selectedItem as String
            val dialog = AlertDialog.Builder(requireActivity()).apply{
                setCancelable(false)
                setTitle("単語帳【"+mydict_name+"】を削除する")
                setMessage("※作った言葉はすべて消えます")
                setPositiveButton("OK") { _, _ ->
                    val id:Int = viewModel!!.dbUid.value!!
                    GlobalScope.launch {
                        val dao = db.mydictDao()
                        dao.deleteByIds(id)
                        val spfCommon = SpfCommon(PreferenceManager.getDefaultSharedPreferences(activity))
                        val settingData = spfCommon.settingRead()
                        // 設定データがもし削除したDBなら参照しないようにする。
                        if(settingData != null){
                            if(settingData.dictUid ==id){
                                settingData.dictUid = -1
                                spfCommon.settingSave(settingData)
                            }
                        }
                    }
                    startActivity(backIntent)
                }
                setNegativeButton("NO",null)
            }
            dialog.show()
        }
    }

}