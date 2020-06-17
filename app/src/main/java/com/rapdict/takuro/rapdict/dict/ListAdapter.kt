package apps.test.marketableskill.biz.recyclerview


import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.*
import com.rapdict.takuro.rapdict.databinding.ItemListBinding
import com.rapdict.takuro.rapdict.dict.ListViewModel


open class ListAdapter(private val viewModel : ListViewModel, private val parentLifecycleOwner: LifecycleOwner ) : androidx.recyclerview.widget.RecyclerView.Adapter<ListViewHolder>() {

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
        val favorite =holder.binding.favoriteStar
        favorite.progress = viewModel.favoriteProgressList[position].value!!
        holder.binding.favoriteStar.setOnClickListener {
            if (viewModel.favoList[position].value!!){
                //お気に入り解除
                favorite.progress = 0F
                viewModel.favoList[position].value = false
                viewModel.colorList[position].value = viewModel.favo2background(false)
                viewModel.updateFavorite(position,false)
            }else{
                // お気に入り
                favorite.playAnimation()
                viewModel.favoList[position].value = true
                viewModel.idList[position].value
                viewModel.colorList[position].value = viewModel.favo2background(true)
                viewModel.updateFavorite(position,true)
            }


        }
        //ここでviewholderのlifecycleOwnerにセットする！
        holder.binding.lifecycleOwner = parentLifecycleOwner
    }
    override fun getItemCount(): Int {
        return viewModel.questionList.size
    }
}