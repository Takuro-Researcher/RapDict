package com.rapdict.takuro.rapdict.myDict.myDictMakeQuestion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.rapdict.takuro.rapdict.databinding.FragmentMydictQuestionMakeBinding
import com.rapdict.takuro.rapdict.main.MainActivity
import com.rapdict.takuro.rapdict.myDict.myDictChoice.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_mydict_question_make.*


class MyDictMakeQuestionFragment : Fragment() {

    private var binding: FragmentMydictQuestionMakeBinding? = null
    private val myDictChoiceViewModel : MyDictChoiceViewModel by activityViewModels()
    private val myDictMakeQuestionViewModel: MyDictMakeQuestionViewModel by viewModels()
    private lateinit var saveDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        saveDialog = AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setNegativeButton("NO", null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMydictQuestionMakeBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val intent  = Intent(requireActivity(), MainActivity::class.java)
        val adapter = MyDictMakeQuestionListAdapter(myDictMakeQuestionViewModel, this)
        binding?.data = myDictMakeQuestionViewModel

        QuestionRecyclerView.adapter = adapter
        QuestionRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        add_question_button.setOnClickListener {
            myDictMakeQuestionViewModel.addCard()
        }

        myDictMakeQuestionViewModel.myDictMakeWords.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.submitList(it)
        })

        register_question_button.setOnClickListener {
            // セーブする前に使用するDBと、ふりがなや文字の状況をまとめる
            val uid = myDictChoiceViewModel.dbUid.value
            myDictMakeQuestionViewModel.updateStatus(uid!!)
            val dialogTitle:String = "問題保存【"+myDictMakeQuestionViewModel.dbName +"】"
            var dialogMessage:String = myDictMakeQuestionViewModel.myDictMakeWords.value?.size.toString() +"個、保存する\n"
            if(myDictMakeQuestionViewModel.isFuriganaEmpty){
                dialogMessage += "フリガナが空の場合、言葉がそのままフリガナとして登録されます \n"
            }
            if(myDictMakeQuestionViewModel.isinCompleteFlag){
                dialogMessage += "言葉が空のものは保存されません\n"
            }
            dialogMessage += "※保存後、画面移動します"
            saveDialog.setTitle(dialogTitle)
            saveDialog.setMessage(dialogMessage)
            saveDialog.setPositiveButton("OK") { _, _ ->
                myDictMakeQuestionViewModel.registerQuestion(myDictChoiceViewModel.dbUid.value!!)
                myDictMakeQuestionViewModel.clear()
                startActivity(intent)
            }
            saveDialog.show()
        }
    }

}