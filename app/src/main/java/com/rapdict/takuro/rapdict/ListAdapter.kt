package apps.test.marketableskill.biz.recyclerview

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.*
import com.rapdict.takuro.rapdict.activity.Dict__Activity
import com.rapdict.takuro.rapdict.common.SQLiteOpenHelper

open class ListAdapter(private val mParentActivity : Dict__Activity, private val mValues: ArrayList<RhymeData>) : RecyclerView.Adapter<ListViewHolder>() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        //レイアウトの設定(inflate)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)

        //Holderの生成
        val holder = ListViewHolder(view)
        helper = SQLiteOpenHelper(view.context)
        db = helper!!.writableDatabase

        //お気に入り処理
        holder.favorite.setOnClickListener {
            val position = holder.adapterPosition
            val rhymeData= mValues.get(position)
            if (rhymeData.favorite){
                holder.favorite.progress = 0F
                rhymeData.favorite =false
                helper!!.answer_update_fav(db!!,rhymeData.answerViewId ,false)
                holder.card.setBackgroundColor(favo2background(false))
            }else{
                holder.favorite.playAnimation()
                rhymeData.favorite =true
                helper!!.answer_update_fav(db!!,rhymeData.answerViewId ,true)
                holder.card.setBackgroundColor(favo2background(true))
            }
        }

        return holder

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = mValues[position]
        holder.rhymeText.text = item.rhyme_text
        holder.rawText.text = item.raw_text
        holder.card.setBackgroundColor(favo2background(item.favorite))
        holder.favorite.progress = if(item.favorite) 0.8F else 0F

    }
    fun rhymeRemove(position: Int){
        val rhymeData:RhymeData = mValues.get(position)
        mValues.removeAt(position)
        notifyItemRemoved(position)
        helper?.answer_delete(db!!,rhymeData.answerViewId)

    }
    fun favo2background(favorite:Boolean):Int{
        val colorId:Int
        return if (favorite){
            Color.YELLOW
        }else{
            Color.WHITE
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}