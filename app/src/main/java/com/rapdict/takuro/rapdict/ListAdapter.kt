package apps.test.marketableskill.biz.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rapdict.takuro.rapdict.Dict__Activity
import com.rapdict.takuro.rapdict.RhymeData
import com.rapdict.takuro.rapdict.ListViewHolder
import com.rapdict.takuro.rapdict.R

open class ListAdapter(private val mParentActivity : Dict__Activity, private val mValues: ArrayList<RhymeData>) : RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        //レイアウトの設定(inflate)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)

        //Holderの生成
        val holder = ListViewHolder(view)

        return holder

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val item = mValues[position]
        holder.rhyme_text.text = item.rhyme_text
        holder.raw_text.text = item.raw_text

    }

    override fun getItemCount(): Int {

        return mValues.size

    }
}