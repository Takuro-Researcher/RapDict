package apps.test.marketableskill.biz.recyclerview


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rapdict.takuro.rapdict.*
import com.rapdict.takuro.rapdict.databinding.ItemListBinding
import com.rapdict.takuro.rapdict.dict.ItemListViewModel


open class ListAdapter(private val viewModel : ItemListViewModel, private val parentLifecycleOwner: LifecycleOwner ) : androidx.recyclerview.widget.RecyclerView.Adapter<ListViewHolder>() {

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
    override fun getItemCount(): Int {
        return viewModel.rawList.size
    }
}