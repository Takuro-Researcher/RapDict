package apps.test.marketableskill.biz.recyclerview


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rapdict.takuro.rapdict.databinding.ListDictBinding
import com.rapdict.takuro.rapdict.dict.DictData
import com.rapdict.takuro.rapdict.dict.DictViewModel

private object DiffCallback : DiffUtil.ItemCallback<DictData>() {
    override fun areItemsTheSame(oldItem: DictData, newItem: DictData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DictData, newItem: DictData): Boolean {
        return oldItem == newItem
    }
}

class DictListAdapter(private val viewModel: DictViewModel,
                      private val parentLifecycleOwner: LifecycleOwner
) : androidx.recyclerview.widget.ListAdapter<DictData, DictListAdapter.DictViewHolder>(DiffCallback) {
    class DictViewHolder(private val binding: ListDictBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DictData, viewLifecycleOwner: LifecycleOwner, viewModel: DictViewModel, position: Int) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                dict = item
                data = viewModel
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DictViewHolder(ListDictBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: DictViewHolder, position: Int) {
        holder.bind(getItem(position), parentLifecycleOwner, viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModel.dictDataList.value?.size ?: 0
    }
}