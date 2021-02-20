package com.rapdict.takuro.rapdict.ui.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rapdict.takuro.rapdict.App.Companion.db
import com.rapdict.takuro.rapdict.Common.CommonTool
import com.rapdict.takuro.rapdict.R
import com.rapdict.takuro.rapdict.model.entity.Word
import com.rapdict.takuro.rapdict.model.repository.ApiRepository
import com.rapdict.takuro.rapdict.myDict.GameSettingData
import com.rapdict.takuro.rapdict.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.coroutines.runBlocking


open class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val backIntent = Intent(this, MainActivity::class.java)
        var jsonData: String? = intent.getStringExtra("DATA")
        val mapper = jacksonObjectMapper()
        val transaction = supportFragmentManager.beginTransaction()
        val data: GameSettingData = mapper.readValue(jsonData.toString())
        val bundle = Bundle().apply {
            this.putInt("RETURN", 1)
            this.putInt("BAR", CommonTool.choiceMusic(data.drumOnly, data.type, data.bar))
        }
        // 言葉を取得
        val recomdialog = AlertDialog.Builder(this)
        recomdialog.setCancelable(false)
        recomdialog.setMessage("韻が一つも取得できませんでした\n最小文字＆最大文字を変えて試してください")
        recomdialog.setPositiveButton("戻る") { _, _ ->
            backIntent.putExtra("MOVE", true)
            startActivity(backIntent)
        }

        setContentView(R.layout.activity_game)

        if (data.dictUid == -1) {
            runBlocking {
                // TODO 0件の時のエラーハンドリング
                var words = ApiRepository().getApiWords(data.min, data.max, data.question)
                if (words.size < data.question) {
                    words = CommonTool.paddList(words, data.question) as ArrayList<Word>
                }
                bundle.putSerializable("WORDS", words as ArrayList<Word>)
                bundle.putInt("QUESTION", data.question)
            }
            changedTexts()
        } else {
            var wordData:List<Word> = listOf<Word>()
            runBlocking {
                val dao = db.wordDao()
                wordData = dao.findByLenght(data.min, data.max, data.dictUid, data.question)
            }
            if (wordData.size == 0) {
                recomdialog.show()
            } else if (wordData.size < data.question) {
                wordData = CommonTool.paddList(wordData, data.question) as List<Word>
            }
            bundle.putSerializable("WORDS", wordData as ArrayList<Word>)
            bundle.putInt("QUESTION", wordData.size)
            changedTexts()
        }

        game_start_button.setOnClickListener {
            game_start_button.visibility = View.INVISIBLE
            waiting_display.visibility = View.INVISIBLE
            attention_sound.visibility = View.INVISIBLE
            val gameFragment = GamePlayFragment()
            gameFragment.arguments = bundle
            transaction.add(R.id.fragmentGame, gameFragment)
            transaction.commit()
        }
    }

    fun changedTexts() {
        waiting_display.text = application.getString(R.string.startingDisp)
        attention_sound.visibility = View.VISIBLE
        game_start_button.visibility = View.VISIBLE
        loading.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        val fragment: Fragment? = supportFragmentManager.findFragmentByTag("handlingBackPressed")
        if (fragment is OnBackKeyPressedListener) {
            (fragment as OnBackKeyPressedListener?)!!.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onUserLeaveHint() {
        //ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
        finish()
    }

    interface OnBackKeyPressedListener {
        fun onBackPressed()
    }
}
