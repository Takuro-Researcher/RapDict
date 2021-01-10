package com.rapdict.takuro.rapdict.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rapdict.takuro.rapdict.Common.App
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.database.Answer
import com.rapdict.takuro.rapdict.databinding.FragmentResultBinding
import com.rapdict.takuro.rapdict.game.GameActivity
import com.rapdict.takuro.rapdict.game.GamePlayViewModel
import com.rapdict.takuro.rapdict.main.MainActivity
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ResultFragment : androidx.fragment.app.Fragment(), GameActivity.OnBackKeyPressedListener {
    // TODO: Rename and change types of parameters

    private var binding: FragmentResultBinding? = null
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var backDialog: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var saveDialog: AlertDialog.Builder
    private  val resultViewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(activity) {}
        val bundle = arguments
        if(bundle != null){
            val words = bundle.getSerializable("WORD_LIST") as List<Word>
            val answer_index = bundle.getSerializable("AMSWER_LIST") as Map<Int, String>
            resultViewModel.initializeAnswerWord(answer_index ,words)
        }

        val recomIntent = Intent(requireActivity(), MainActivity::class.java)
        backDialog = AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setTitle("ゲーム設定画面へ戻る")
            setMessage("(保存は一切行われません)")
            setPositiveButton("OK") { _, _ ->
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    startActivity(recomIntent)
                }
            }
            setNegativeButton("NO", null)
        }
        alertDialog = AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setTitle("データ保存")
            setMessage("韻を選択してください")
            setPositiveButton("OK", { _, _ -> })
        }
        saveDialog = AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setTitle("データ保存")
            setMessage(register_answer.size.toString() + "個、韻を保存します")
            setPositiveButton("OK") { _, _ ->
                GlobalScope.launch {
                    val dao = App.db.answerDao()
                    register_answer.forEach {
                        dao.insert(it)
                    }
                }
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    startActivity(recomIntent)
                }
            }
            setNegativeButton("NO", null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recomIntent = Intent(requireActivity(), MainActivity::class.java)
        CommonTool.fadeIn(result_form, requireActivity())

        val resultListViewModel: ResultListViewModel by viewModels()
        val adapter = ResultListAdapter(resultListViewModel, this)




        binding?.data = resultViewModel
        resultListViewModel.draw(answerList, wordList)
        adapter.notifyDataSetChanged()
        // 広告を設定
        mInterstitialAd = InterstitialAd(activity).apply {
            //adUnitId = "ca-app-pub-3940256099942544/1033173712"
            adUnitId = "ca-app-pub-9599597100424961/9164716689"
            adListener = (object : AdListener() {
                override fun onAdLoaded() {}
                override fun onAdFailedToLoad(errorCode: Int) {}
                override fun onAdClosed() {
                    startActivity(recomIntent)
                }
            })
        }
        mInterstitialAd.loadAd(AdRequest.Builder().build())


        ResultRecyclerView.adapter = adapter
        ResultRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        add_answer_button.setOnClickListener {
            resultListViewModel.addCard()
            adapter.notifyItemInserted(adapter.itemCount)
            val bool = resultViewModel.addAbleCheck()
            if (bool == false) {
                Toast.makeText(activity, "追加は5個までです", Toast.LENGTH_LONG).show()
            }

        }

        // 保存する
        save_button.setOnClickListener {

            //　新規追加データがあれば保存する
            answerList.addAll(resultListViewModel.returnRegisterCard(answerList.size))
            // 実際に登録するアンサーを検出する
            val register_answer = resultListViewModel.checkedList.let {
                val array: ArrayList<Answer> = ArrayList()
                it.forEachIndexed { index, data ->
                    if (data.value == true) {
                        array.add(answerList.get(index))
                    }
                }
                array
            }



            if (register_answer.size == 0) {
                alertDialog.show()
            } else {
                saveDialog.show()
            }

        }
        // 保存せずメイン画面へ戻る
        back_button.setOnClickListener {
            backDialog.show()
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBackPressed() {
        backDialog.show()
    }

}
