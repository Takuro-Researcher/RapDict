package com.rapdict.takuro.rapdict.myDict.myDictDisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rapdict.takuro.rapdict.databinding.FragmentMydictDisplayBinding
import com.rapdict.takuro.rapdict.myDict.myDictChoice.MyDictChoiceViewModel
import kotlinx.android.synthetic.main.fragment_mydict_display.*

class MyDictDisplayFragment : androidx.fragment.app.Fragment() {
    private var binding: FragmentMydictDisplayBinding? = null
    private val myDictChoiceViewModel : MyDictChoiceViewModel by activityViewModels()
    private val myDictDisplayViewModel: MyDictDisplayViewModel by viewModels()
    private lateinit var deleteDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deleteDialog = AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setNegativeButton("NO", null)
            setMessage("韻を消します")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMydictDisplayBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = MyDictDisplayListAdapter(myDictDisplayViewModel, this)
        binding?.data = myDictDisplayViewModel

        DisplayRecyclerView.adapter = adapter
        DisplayRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        myDictDisplayViewModel.myDictDisplayWords.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.submitList(it)
            //　表示する韻がない場合の表示を行う
            if (it.size == 0) {
                myDictDisplayViewModel.displayWordNotExists.value = View.VISIBLE
            } else {
                myDictDisplayViewModel.displayWordNotExists.value = View.GONE
            }
            for (data in it) {
                data.isDelete.observe(viewLifecycleOwner, Observer {
                    if (it) {
                        deleteDialog.setPositiveButton("OK") { _, _ ->
                            myDictDisplayViewModel.delete(data)
                        }.show()
                        data.isDelete.value = false
                    }
                })
            }
            myDictChoiceViewModel.dictCount.value = it.size
        })


        val uid: Int = myDictChoiceViewModel.dbUid.value!!
        myDictDisplayViewModel.bindData(uid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}