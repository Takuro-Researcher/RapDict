package apps.test.marketableskill.biz.recyclerview

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.*
import com.rapdict.takuro.rapdict.databinding.ItemListBinding
import com.rapdict.takuro.rapdict.dict.DictViewModel
import com.rapdict.takuro.rapdict.helper.SQLiteOpenHelper

open class ListAdapter(private val viewModel : DictViewModel, private val parentLifecycleOwner: LifecycleOwner ) : androidx.recyclerview.widget.RecyclerView.Adapter<ListViewHolder>() {
    private var helper: SQLiteOpenHelper? = null
    private var db: SQLiteDatabase? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val binding = DataBindingUtil.inflate<ItemListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_list,
                parent,
                false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.viewModel = viewModel
        holder.binding.position = position
        //ここでviewholderのlifecycleOwnerにセットする！
        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    fun rhymeRemove(position: Int){
        //TODO 韻を消す
//        val rhymeData:RhymeData = mValues.get(position)
//        mValues.removeAt(position)
//        notifyItemRemoved(position)
//        helper?.answer_delete(db!!,rhymeData.answerViewId)

    }
    fun favo2background(favorite:Boolean):Int{
        return if (favorite){
            Color.YELLOW
        }else{
            Color.WHITE
        }
    }

    override fun getItemCount(): Int {
        return viewModel.rawList.size
    }
}